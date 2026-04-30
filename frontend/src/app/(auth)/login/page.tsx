"use client";

import React from "react";
import styles from "./login.module.css";
import { useLoginForm } from "./_components/useLoginForm";

export default function LoginPage() {
  const { pin, setPin, isLoading, error, handleLogin } = useLoginForm();

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
              type="text" // 비밀번호 대신 텍스트 허용 (복사/붙여넣기 용이)
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
