# 오프라인 액션 큐잉 워크플로우

> **목표:** 직원이 오프라인 환경에서도 "완료" 등 안전한 액션을 수행할 수 있도록 IndexedDB 기반 큐잉 및 자동 동기화 구현
>
> **핵심 시나리오:** 직원이 수건을 들고 객실로 이동 → Wi-Fi 끊김 → 전달 후 "완료" 버튼 클릭 → 네트워크 복귀 시 자동 반영

---

## 액션 분류 기준

| 분류 | 액션 | 오프라인 처리 | 이유 |
|------|------|-------------|------|
| 🔴 온라인 전용 | 태스크 수락 | ❌ 차단 + 안내 토스트 | 다수 직원 동시 수락 충돌 위험 (낙관적 락 필요) |
| 🔴 온라인 전용 | 요청 접수 (고객) | ❌ 차단 + 안내 토스트 | AI 호출 필수, 서버 의존적 |
| 🔴 온라인 전용 | 부서 전달 (재배정) | ❌ 차단 + 안내 토스트 | 실시간 부서 알림 필요 |
| 🟢 오프라인 가능 | 태스크 완료 | ✅ 큐잉 후 동기화 | 이미 수락한 본인만 완료 가능 → 충돌 없음 |
| 🟢 오프라인 가능 | 메모 작성 | ✅ 큐잉 후 동기화 | 로컬 데이터, 서버 의존성 낮음 |

---

## 아키텍처

```
┌─────────────────────────────────────────────────────┐
│                     프론트엔드                        │
│                                                      │
│  [버튼 클릭] → useOfflineQueue 훅                     │
│       │                                              │
│       ├─ navigator.onLine === true                   │
│       │   → fetch(PATCH /api/requests/{id}/complete) │
│       │   → 성공 ✅                                   │
│       │                                              │
│       └─ navigator.onLine === false                  │
│           → IndexedDB "offline_queue" 테이블에 저장   │
│           → UI에 ⏳ 대기 표시                         │
│           → online 이벤트 리스너 대기                  │
│                │                                     │
│                └─ 네트워크 복귀 시                     │
│                    → 큐 전체 flush                    │
│                    → 성공: 큐 삭제 + ✅ 토스트          │
│                    → 실패: 에러 안내                   │
└─────────────────────────────────────────────────────┘
```

---

## Phase 1: 온라인/오프라인 감지 훅

### 1-1. `useNetworkStatus.ts`

```
경로: frontend/src/app/hooks/useNetworkStatus.ts
```

```typescript
// 반환값
{
  isOnline: boolean,      // 현재 네트워크 상태
  wasOffline: boolean,    // 방금 오프라인에서 복귀했는지
}
```

**구현 핵심:**
- `navigator.onLine` + `window.addEventListener('online' | 'offline')` 사용
- 상태 변경 시 `useState`로 UI 자동 갱신

### Phase 1 완료 후 검증

```bash
# 1. 프론트엔드 실행
cd frontend && npm run dev

# 2. 브라우저 DevTools → Network → Offline 체크
#    → 화면에 오프라인 상태 표시 확인

# 3. Offline 해제
#    → 화면에 온라인 복귀 표시 확인
```

---

## Phase 2: IndexedDB 큐 관리

### 2-1. `offlineQueue.ts` (유틸리티)

```
경로: frontend/src/app/utils/offlineQueue.ts
```

**IndexedDB 스키마:**

```typescript
interface QueuedAction {
  id: string;             // UUID (중복 방지)
  type: 'COMPLETE';       // 액션 타입 (추후 MEMO 등 확장 가능)
  endpoint: string;       // "PATCH /api/requests/3/complete"
  method: 'PATCH' | 'POST';
  body?: object;
  createdAt: number;      // timestamp
  retryCount: number;     // 재시도 횟수
  maxRetries: number;     // 최대 3회
}
```

**제공 함수:**

| 함수 | 역할 |
|------|------|
| `enqueue(action)` | 큐에 액션 추가 |
| `dequeue(id)` | 성공한 액션 제거 |
| `getAll()` | 큐에 쌓인 전체 액션 조회 |
| `flush()` | 큐 전체를 순차적으로 서버에 전송 |
| `clear()` | 큐 비우기 |

### Phase 2 완료 후 검증

```bash
# 1. 브라우저 DevTools → Application → IndexedDB 탭 확인

# 2. 오프라인 모드에서 완료 버튼 클릭
#    → IndexedDB에 QueuedAction 레코드 추가 확인

# 3. 온라인 복귀
#    → IndexedDB 레코드 삭제 확인 (flush 성공)
```

---

## Phase 3: 오프라인 큐 훅 (`useOfflineQueue`)

### 3-1. `useOfflineQueue.ts`

```
경로: frontend/src/app/hooks/useOfflineQueue.ts
```

```typescript
// 사용법
const { executeOrQueue, pendingCount, isSyncing } = useOfflineQueue();

// 직원이 완료 버튼 클릭 시
const handleComplete = (requestId: number) => {
  executeOrQueue({
    type: 'COMPLETE',
    endpoint: `/api/requests/${requestId}/complete`,
    method: 'PATCH',
  });
};
```

**내부 동작:**

```
executeOrQueue(action) 호출
  │
  ├─ 온라인?
  │   → fetch(action.endpoint, { method: action.method })
  │   → 성공 → UI 갱신
  │   → 실패 (네트워크 에러) → enqueue(action) → 오프라인 처리로 전환
  │
  └─ 오프라인?
      → enqueue(action)
      → pendingCount + 1
      → UI에 "⏳ 대기 중 (1건)" 배지 표시
```

**자동 동기화:**

```
window.addEventListener('online', async () => {
  isSyncing = true;
  const actions = await getAll();
  
  for (const action of actions) {
    try {
      await fetch(action.endpoint, { method: action.method });
      await dequeue(action.id);    // 성공 → 큐에서 제거
      toast("✅ 동기화 완료");
    } catch (error) {
      action.retryCount++;
      if (action.retryCount >= action.maxRetries) {
        await dequeue(action.id);  // 최대 재시도 초과 → 포기
        toast("⚠️ 동기화 실패: 수동으로 확인해주세요");
      }
    }
  }
  
  isSyncing = false;
});
```

### Phase 3 완료 후 검증

```bash
# 1. 백엔드 + 프론트엔드 실행
cd backend && ./gradlew bootRun
cd frontend && npm run dev

# 2. 직원 대시보드 또는 테스트 페이지 접속

# 3. DevTools → Network → Offline 체크

# 4. "완료" 버튼 클릭
#    → "⏳ 네트워크 복귀 시 자동 반영됩니다" 토스트 확인
#    → IndexedDB에 큐 레코드 확인

# 5. Offline 해제
#    → "✅ 동기화 완료" 토스트 확인
#    → 서버에서 상태가 DONE으로 변경 확인
curl http://localhost:8080/api/requests/1
#    → status: "DONE" 확인
```

---

## Phase 4: UI 피드백

### 4-1. 온라인 전용 버튼 차단

```typescript
// 수락 버튼 (온라인 전용)
<button 
  disabled={!isOnline}
  onClick={handleAccept}
>
  {isOnline ? '수락' : '🔴 오프라인'}
</button>
```

### 4-2. 오프라인 가능 버튼 큐잉 표시

```typescript
// 완료 버튼 (오프라인 가능)
<button onClick={() => executeOrQueue(completeAction)}>
  완료
</button>

// 하단 상태 바
{pendingCount > 0 && (
  <div className="offline-badge">
    ⏳ 동기화 대기 {pendingCount}건
  </div>
)}
```

### 4-3. 전역 오프라인 배너

```
┌────────────────────────────────────────┐
│ ⚠️ 오프라인 상태입니다.                 │
│ 일부 기능이 제한됩니다.                  │
│ 완료 처리는 복귀 시 자동 반영됩니다.      │
└────────────────────────────────────────┘
```

### Phase 4 완료 후 검증

```bash
# 1. 브라우저에서 직원 대시보드 접속

# 2. DevTools → Network → Offline 체크
#    → 상단에 오프라인 배너 표시 확인
#    → "수락" 버튼 비활성화 확인
#    → "완료" 버튼은 클릭 가능 확인

# 3. "완료" 클릭
#    → "⏳ 동기화 대기 1건" 배지 표시 확인

# 4. Offline 해제
#    → 배너 사라짐 확인
#    → "✅ 동기화 완료" 토스트 확인
#    → 배지 사라짐 확인
#    → "수락" 버튼 활성화 확인
```

---

## 타임라인 요약

```
Phase 1 ━━━▶ Phase 2 ━━━━━━━━━▶ Phase 3 ━━━━━━━━━▶ Phase 4
(감지 훅)     (IndexedDB 큐)      (통합 훅)           (UI)
 0.5일         1일                 1일                 0.5일

총 예상: 3일
```

> [!IMPORTANT]
> **선행 조건:** RQ 워크플로우의 Phase 2 (백엔드 API) 완료 후 진행 가능
> **적용 시점:** 직원 대시보드(Staff Task 도메인) 개발과 병행하거나, E2E 테스트 전에 적용 권장

## 파일 구조

```
frontend/src/app/
├── hooks/
│   ├── useNetworkStatus.ts    ← Phase 1
│   └── useOfflineQueue.ts     ← Phase 3
├── utils/
│   └── offlineQueue.ts        ← Phase 2 (IndexedDB 유틸)
└── components/
    └── OfflineBanner.tsx       ← Phase 4 (전역 배너)
```

## 확장 가능성

| 기능 | 설명 | 우선순위 |
|------|------|---------|
| 메모 오프라인 저장 | 직원 메모를 IndexedDB에 임시 저장 | 낮음 |
| Service Worker 연동 | PWA 배포 시 백그라운드 동기화 | 낮음 (AN-114와 연관) |
| 큐 충돌 감지 | 동기화 시 서버 상태와 비교 후 충돌 알림 | 중간 |
