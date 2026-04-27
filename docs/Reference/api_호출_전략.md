# 프론트엔드 API 호출 규칙

> BFF(Next.js Route Handler) 프록시 구조 기반, co-location fetch 훅 패턴

---

## 1. 통신 구조

```
브라우저 (클라이언트 컴포넌트)
  └→ use*.ts 훅에서 fetch('/api/...')
      └→ Next.js Route Handler (app/api/)
          ├─ [인증] app/api/auth/*/route.ts  — 전용 핸들러 (iron-session 처리)
          └─ [일반] app/api/[...path]/route.ts — catch-all 프록시
              └→ JWT 주입 + 헤더 설정 → Spring Boot (:8080)
```

- JWT는 iron-session 암호화 쿠키에만 존재하며 브라우저에 노출되지 않는다
- 모든 fetch는 `/api/*` 경로로 요청하고, Route Handler가 백엔드로 중계한다

---

## 2. 핵심 원칙

### 컴포넌트에서 fetch를 직접 호출하지 않는다

모든 API 호출은 반드시 `use*.ts` 훅 함수 안에서 수행한다.

```tsx
// ✅ Good — 훅을 통한 호출
function CartPage() {
  const { cartItems, loading } = useCart();
  return <CartList items={cartItems} />;
}

// ❌ Bad — 컴포넌트에서 직접 fetch
function CartPage() {
  const [items, setItems] = useState([]);
  useEffect(() => {
    fetch('/api/cart').then(res => res.json()).then(setItems);
  }, []);
}
```

### 에러 처리는 `handleResponse`로 통일한다

```ts
import { handleResponse } from '@/lib/api';

const res = await fetch('/api/events');
const json = await handleResponse<EventDetail[]>(res);
```

---

## 3. 훅 파일 위치 규칙 (Co-location)

| 사용 범위 | 훅 위치 | 예시 |
|---|---|---|
| 컴포넌트 1개에서만 사용 | 해당 컴포넌트 바로 옆 | `events/[id]/_components/ReviewSection/useEventReviews.ts` |
| 같은 라우트의 하위 여러 곳에서 사용 | 공통 부모 폴더 | `events/useEvents.ts` |
| 앱 전체에서 사용 | `app/` 루트 | `app/useCategories.ts` |

> **`hooks/` 별도 폴더는 만들지 않는다.** 모든 훅은 라우트 폴더 구조 안에서만 관리한다.

**중복이 생기면 삭제하고 부모로 올린다.**

```
app/
├── useCategories.ts            ← 앱 전체 공통 (app 루트)
├── events/
│   ├── useEvents.ts            ← events 하위 공통
│   ├── page.tsx
│   └── [id]/
│       ├── page.tsx
│       └── _components/
│           └── ReviewSection/
│               ├── ReviewSection.tsx
│               └── useEventReviews.ts  ← 이 컴포넌트 전용
└── mypage/
    └── events/
        └── useMyEvents.ts      ← 마이페이지 전용 (다른 데이터)
```

---

## 4. 공유 유틸 (`lib/api.ts`)

lib에는 fetch 래핑이 아닌, **에러 처리 헬퍼만** 둔다.

```ts
// lib/api.ts

/** 응답 에러 공통 처리 — !res.ok이면 throw */
export async function handleResponse<T>(res: Response): Promise<T> {
  if (!res.ok) {
    const body = await res.json().catch(() => ({}));
    throw new Error(body.message || `HTTP ${res.status}`);
  }
  return res.json();
}
```

- `fetch`는 각 훅에서 직접 호출한다
- `handleResponse`는 에러 처리 + JSON 파싱만 담당한다

---

## 5. 훅 작성 템플릿

### 조회 (GET)

```ts
'use client';
import { useState, useEffect, useCallback } from 'react';
import { handleResponse } from '@/lib/api';

// 이 훅에서 쓰는 타입은 훅 파일 안에 정의
interface CartItem {
  id: number;
  name: string;
  quantity: number;
}

export function useCart() {
  const [items, setItems] = useState<CartItem[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetchItems = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const res = await fetch('/api/cart');
      const json = await handleResponse<CartItem[]>(res);
      setItems(json);
    } catch (err: any) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => { fetchItems(); }, [fetchItems]);

  return { items, loading, error, refresh: fetchItems };
}
```

### 변경 (POST / PUT / PATCH / DELETE)

```ts
// 생성
const createItem = async (body: CreateRequest) => {
  const res = await fetch('/api/items', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(body),
  });
  return handleResponse<CreatedItem>(res);
};

// 수정
const updateItem = async (id: number, body: UpdateRequest) => {
  const res = await fetch(`/api/items/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(body),
  });
  return handleResponse<UpdatedItem>(res);
};

// 삭제
const deleteItem = async (id: number) => {
  const res = await fetch(`/api/items/${id}`, { method: 'DELETE' });
  return handleResponse<void>(res);
};
```

### 파일 업로드 (multipart/form-data)

```ts
const uploadFile = async (file: File) => {
  const formData = new FormData();
  formData.append('file', file);

  const res = await fetch('/api/files/upload', {
    method: 'POST',
    body: formData,
    // ⚠️ Content-Type 생략 — 브라우저가 boundary를 자동 설정
  });
  return handleResponse<{ url: string }>(res);
};
```

---

## 6. 백엔드 응답 처리

백엔드 응답이 `{ success, data, message }` 래핑인 경우와 raw 응답인 경우가 혼재한다.  
`handleResponse`는 에러 처리만 담당하고, **데이터 접근은 훅에서 응답 구조에 맞게 처리한다.**

```ts
// 래핑된 응답 — .data로 접근
interface ApiRes<T> { success: boolean; data: T; message?: string; }

const res = await fetch('/api/wishlists/me');
const json = await handleResponse<ApiRes<WishlistItem[]>>(res);
setItems(json.data);

// raw 응답 — 바로 사용
const res = await fetch('/api/cart');
const json = await handleResponse<CartItem[]>(res);
setItems(json);
```

---

## 7. 필수 체크리스트

- [ ] 컴포넌트에서 `fetch`를 직접 호출하지 않았는가?
- [ ] 훅에 `loading`, `error` 상태가 포함되어 있는가?
- [ ] 에러 처리에 `handleResponse`를 사용했는가?
- [ ] JSON 요청 시 `Content-Type: application/json` 헤더를 명시했는가?
- [ ] 파일 업로드 시 `Content-Type` 헤더를 생략했는가?
- [ ] 훅 파일이 사용 범위에 맞는 위치에 있는가?
- [ ] 타입(interface)이 훅 파일 안에 정의되어 있는가?
- [ ] `console.log` 디버그 코드를 제거했는가?
