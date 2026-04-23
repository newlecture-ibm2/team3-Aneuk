---
trigger: always_on
---

# 🚀 VenueOn Frontend Architecture & Development Rule Set

## 1. 🛠️ Tech Stack & Framework
- **Framework**: Next.js 16.2.1 (App Router 기반, **BFF 패턴 적용**)
- **Language**: TypeScript
- **Styling**: Vanilla CSS Modules (`*.module.css`) + CSS Variables
- **State Management**: Zustand (`src/store`)
- **Session/Auth**: iron-session (BFF 레이어 연동)
- **Icons**: Custom SVG (`src/components/icons` 활용)

---

## 2. 📁 Directory Structure & Routing Rules

모든 라우팅은 Next.js App Router 패러다임을 따르며, 관심사 분리를 철저히 지킵니다.

- **`src/app/`**: 라우트(페이지) 정의 및 서버 컴포넌트의 진입점.
  - 라우트 그룹 `(folderName)`을 적극 활용하여 논리적 그룹화 (예: `(auth)`).
  - 특정 페이지에서만 사용되는 종속적인 컴포넌트는 해당 라우트 폴더 내 `_components/` 디렉토리에 위치시킵니다.
    - ❌ Bad: `src/components/LoginForm.tsx` (오직 로그인 페이지에서만 쓰임)
    - ✅ Good: `src/app/(auth)/login/_components/LoginForm/LoginForm.tsx`
  - **`api/` (BFF Layer)**: 백엔드 서버와 클라이언트 사이를 중계하는 API Route Handlers. 보안 및 데이터 가공을 이곳에서 수행합니다.

- **`src/components/`**: 2곳 이상의 페이지/도메인에서 재사용되는 전역 컴포넌트.
  - `ui/`: 버튼, 인풋, 뱃지 등 최소 단위(Atomic) 컴포넌트.
  - `layout/`: Header, Footer, Sidebar 등 전역 레이아웃 요소.
  - `modal/`: 전역적으로 사용되는 모달 컴포넌트.
  - `icons/`: 프로젝트에서 사용되는 모든 커스텀 SVG 아이콘 컴포넌트 (외부 아이콘 라이브러리 미사용).

- **`src/lib/`**: BFF 호출 및 비즈니스 로직, 유틸리티 함수.
  - API 함수는 도메인별로 분리하며, 백엔드 API를 직접 찌르지 않고 Next.js BFF 레이어(`src/app/api`)를 호출하도록 작성합니다.

- **`src/store/`**: Zustand 기반의 클라이언트 전역 상태 관리.
  - 스토어는 도메인 단위로 분리 (`useAuthStore.ts`, `useUIStore.ts`).

- **`src/styles/`**: 전역 스타일 및 디자인 토큰.
  - `globals.css`: 전역 리셋 및 기본 설정.
  - `variables.css`: 색상, 타이포그래피, 간격 등 디자인 시스템의 CSS 변수(`:root`).

---

## 3. 🎨 Styling Rules (CSS Modules)

Tailwind CSS 등의 유틸리티 클래스를 사용하지 않으며, **순수 CSS Modules** 방식을 고수합니다.

1. **파일 네이밍**: `ComponentName.module.css` 형태로 컴포넌트와 동일한 위치에 생성.
2. **CSS 변수 적극 활용**: 하드코딩된 색상 및 사이즈 대신 `variables.css`에 정의된 `var(--color-primary)` 형태의 디자인 토큰을 무조건 사용.
3. **스코프 원칙**: 글로벌 스타일이 필요한 경우를 제외하고 컴포넌트 스타일은 철저히 모듈 내 캡슐화.

---

## 4. 🧩 Component Development Rules

1. **Server vs Client Components**:
   - Next.js의 **Server Component**를 최대한 활용하여 번들 사이즈 최소화 및 초기 렌더링 성능 최적화.
   - 상태(State), 생명주기(Effect), 이벤트 리스너(onClick 등)가 필요한 컴포넌트의 최상단에만 `'use client'` 선언.
2. **디렉토리 묶음**: 컴포넌트는 단일 파일이 아닌 폴더 단위로 관리.
   - `HeroSection/`
     - `HeroSection.tsx`
     - `HeroSection.module.css`
3. **Props 인터페이스**: 모든 컴포넌트의 Props는 `type` 또는 `interface`로 명확히 타이핑.

---

## 5. 📡 BFF (Backend-For-Frontend) & API Rules

클라이언트(브라우저)에서 백엔드 서버로 직접 요청을 보내지 않고, Next.js 서버를 거쳐가는 **BFF(Backend-for-Frontend) 패턴**을 강제합니다.

1. **클라이언트 요청 통제**: 브라우저의 컴포넌트는 `fetch`를 통해 내부 BFF 엔드포인트(`Next.js API Routes` 또는 `Server Actions`)만 호출합니다.
2. **BFF 레이어의 역할 (`src/app/api` 또는 Server Actions)**:
   - 클라이언트의 요청을 받아 실제 백엔드 서버(Java/Spring 등)로 프록시(Proxy) 합니다.
   - 서버 간 통신이므로 CORS 이슈를 방지할 수 있습니다.
   - 필요한 경우 클라이언트가 소비하기 편한 형태로 API 응답 데이터를 가공(Formatting)하여 전달합니다.
3. **에러 핸들링**: BFF 레이어에서 백엔드의 에러를 1차적으로 캐치하고, 클라이언트에 정제된 에러 메시지를 반환합니다.

---

## 6. 🔐 Auth & Session Rules (with BFF)

보안 강화를 위해 토큰 등 민감한 정보는 브라우저에 노출하지 않습니다.

1. **HttpOnly 쿠키 활용**: BFF 레이어에서 로그인을 처리하고, 발급받은 Access/Refresh Token은 `iron-session`을 통해 **HttpOnly, Secure 쿠키**로 구워 클라이언트에 전달합니다.
2. **토큰 갱신 로직 은닉**: Token 탈취를 막기 위해 클라이언트 코드에는 토큰 관련 로직이 존재하지 않으며, 모든 인증 상태 유지 및 토큰 갱신은 BFF 레이어에서 처리합니다.
3. **상태 관리**: 클라이언트 단의 유저 렌더링(닉네임, 권한 등) 및 UI 권한 분기는 `useAuthStore` (Zustand)를 활용하되, 초기 데이터는 서버(BFF)에서 주입받습니다.

---

## 7. 🏷️ Naming Conventions

- **Component & File (React)**: `PascalCase` (예: `EventList.tsx`, `HeroSection.tsx`)
- **Functions & Variables**: `camelCase` (예: `fetchEventData`, `isLoggedIn`)
- **CSS Classes**: 무조건 **`camelCase`** 강제 (예: `.btnPrimary`, `.headerContainer`)
- **API Files**: `kebab-case` (예: `admin-api.ts`, `auth-api.ts`)
- **Constants (상수)**: `UPPER_SNAKE_CASE` (예: `MAX_UPLOAD_SIZE`)

## 리팩토링 교훈 기반 예방 규칙

### 파일 크기 경고 임계치
| 줄 수 | 상태 | 조치 |
|-------|------|------|
| ~150줄 | ✅ 정상 | - |
| 150~300줄 | ⚠️ 경고 | `_components/` 분리 검토 |
| 300줄 이상 | 🔴 즉시 분리 | 서브 컴포넌트 + 커스텀 훅 추출 |
| 500줄 이상 | 🚨 아키텍처 위반 | 즉시 리팩토링 필수 |

### 즉시 수정 경고 신호
- ⚠️ page.tsx 생성 시 `_components/` 폴더를 동시에 만들지 않았다면 → 즉시 생성
- ⚠️ 컴포넌트가 폴더 없이 단독 `.tsx` 파일로 존재한다면 → `ComponentName/ComponentName.tsx + .module.css + index.ts` 구조로 변환
- ⚠️ 하나의 컴포넌트에 UI 렌더링 + 상태 관리 + API 호출이 혼재한다면 → `use{Feature}.ts` 커스텀 훅으로 로직 분리
- ⚠️ API 함수가 하나의 파일에 여러 도메인이 혼재한다면 → `lib/api/{domain}API.ts`로 도메인별 분리
- ⚠️ BFF `route.ts`가 100줄 이상이라면 → `handlers/` 폴더로 핸들러 분리
- ⚠️ CSS Module을 다른 컴포넌트와 공유하고 있다면 → 각 컴포넌트 전용 `.module.css` 생성

