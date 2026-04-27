'use client';

import React from 'react';
import styles from './ModalCard.module.css';

interface ModalCardProps {
  size?: 'sm' | 'md' | 'lg';
  children: React.ReactNode;
}

export default function ModalCard({ size = 'sm', children }: ModalCardProps) {
  const sizeClass = size === 'sm' ? styles.sizeSm : size === 'md' ? styles.sizeMd : styles.sizeLg;
  return (
    <div className={`${styles.modalCard} ${sizeClass}`} onClick={(e) => e.stopPropagation()}>
      {children}
    </div>
  );
}
