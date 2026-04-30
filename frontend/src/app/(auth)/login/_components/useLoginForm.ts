"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";

export const useLoginForm = () => {
  const [pin, setPin] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const router = useRouter();

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!pin) {
      setError("PIN 번호 또는 접속 코드를 입력해주세요.");
      return;
    }

    setIsLoading(true);
    setError(null);

    // 판별 로직: 숫자로만 된 6자리면 직원(Staff), 그 외에는 투숙객(Guest)
    const isStaffPin = /^\d{6}$/.test(pin);
    const endpoint = isStaffPin ? "/api/auth/staff" : "/api/auth/guest";
    const body = isStaffPin ? { pin } : { accessCode: pin };

    try {
      const response = await fetch(endpoint, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body),
      });

      const data = await response.json();

      if (!response.ok) throw new Error(data.message || "인증에 실패했습니다.");

      // 역할별 리다이렉트
      if (data.role === "ADMIN") {
        router.push("/admin/dashboard");
      } else if (data.role === "STAFF") {
        router.push("/staff/requests");
      } else if (data.role === "GUEST") {
        router.push("/guest/chat");
      }
      
    } catch (err: any) {
      setError(err.message);
    } finally {
      setIsLoading(false);
    }
  };

  return { pin, setPin, isLoading, error, handleLogin };
};
