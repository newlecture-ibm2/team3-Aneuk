"use client";

import React, { useEffect } from "react";
import { useSearchParams } from "next/navigation";
import styles from "./login.module.css";
import { useLoginForm } from "./_components/useLoginForm";

export default function LoginPage() {
  const { pin, setPin, isLoading, error, handleLogin, performLogin } = useLoginForm();
  const searchParams = useSearchParams();

  // ★ QR 코드 자동 로그인 로직
  useEffect(() => {
    const code = searchParams.get("code");
    if (code) {
      setPin(code); // 입력창에도 표시
      performLogin(code); // 즉시 로그인 시도
    }
  }, [searchParams, performLogin, setPin]);

  return (
    <div className={styles.container}>
      <div className={styles.loginCard}>
        <header className={styles.header}>
          <h1 className={styles.logo}>Aneuk</h1>
          <p className={styles.subtitle}>Welcome to Premium Service</p>
        </header>

        <form className={styles.form} onSubmit={handleLogin}>
          <div className={styles.inputGroup}>
            <label htmlFor="auth-code" className={styles.inputLabel}>
              PIN Code or Access Code
            </label>
            <input
              id="auth-code"
              type="text"
              placeholder="Enter PIN or Access Code"
              value={pin}
              onChange={(e) => setPin(e.target.value)}
              className={styles.pinInput}
              autoFocus
            />
          </div>

          {error && <div className={styles.errorMsg}>{error}</div>}

          <button type="submit" className={styles.submitBtn} disabled={isLoading || !pin}>
            {isLoading ? "Authenticating..." : "Start Service"}
          </button>
        </form>

        <footer className={styles.footer}>
          <p>© 2026 Aneuk Hotel Concierge Service</p>
        </footer>
      </div>
    </div>
  );
}
