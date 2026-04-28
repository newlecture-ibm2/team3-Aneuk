'use client';

import React, { useEffect } from 'react';
import Header from './Header';
import Sidebar, { SidebarProps } from './Sidebar';
import { useUiStore } from '@/stores/useUiStore';
import styles from './DashboardLayout.module.css';

interface DashboardLayoutProps {
  children: React.ReactNode;
  role?: SidebarProps['role'];
}

export default function DashboardLayout({ children, role = 'admin' }: DashboardLayoutProps) {
  const { isSidebarOpen, toggleSidebar } = useUiStore();

  return (
    <div className={styles.layout}>
      <Header className={styles.header} />
      
      <div className={styles.body}>
        {isSidebarOpen && (
          <div className={styles.backdrop} onClick={toggleSidebar} />
        )}
        
        <div className={`${styles.sidebarWrapper} ${isSidebarOpen ? styles.open : ''}`}>
          <Sidebar className={styles.sidebar} role={role} />
        </div>
        
        <main className={styles.main}>
          {children}
        </main>
      </div>
    </div>
  );
}
