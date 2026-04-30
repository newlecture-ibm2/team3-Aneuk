import React, { Suspense } from "react";
import LoginForm from "./_components/LoginForm/LoginForm";

/**
 * 로그인 페이지 메인 (Suspense 래퍼 전용)
 * 
 * 💡 useSearchParams를 사용하는 컴포넌트(LoginForm)를 
 * 반드시 Suspense로 감싸야 정적 빌드 시 에러가 발생하지 않습니다.
 */
export default function LoginPage() {
  return (
    <Suspense fallback={<div>Loading login page...</div>}>
      <LoginForm />
    </Suspense>
  );
}
