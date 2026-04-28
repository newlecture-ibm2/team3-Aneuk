'use client';

import React from 'react';
import Link from 'next/link';
import { Menu } from 'lucide-react';
import { useUiStore } from '@/stores/useUiStore';
import styles from './Header.module.css';

interface HeaderProps {
  className?: string;
}

export default function Header({ className = '' }: HeaderProps) {
  const { toggleSidebar } = useUiStore();

  return (
    <header className={`${styles.header} ${className}`.trim()}>
      <div className={styles.left}>
        <button className={styles.hamburgerBtn} onClick={toggleSidebar} aria-label="메뉴 열기">
          <Menu size={24} />
        </button>
        <Link href="/" style={{ 
          fontSize: '1.75rem', 
          fontWeight: 900, 
          letterSpacing: '-0.05em', 
          color: 'var(--color-primary, #0f172a)',
          textDecoration: 'none'
        }}>
          Anook
        </Link>
      </div>
    </header>
  );
}
