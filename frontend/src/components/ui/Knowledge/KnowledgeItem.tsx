'use client';

import React from 'react';
import styles from './KnowledgeItem.module.css';
import StatusBadge from '@/components/ui/StatusBadge/StatusBadge';

export interface KnowledgeItemProps {
  category: string;
  title: string;
  description: string;
  updatedAt: string;
  onClick?: () => void;
}

export default function KnowledgeItem({ category, title, description, updatedAt, onClick }: KnowledgeItemProps) {
  return (
    <div className={styles.container} onClick={onClick}>
      <div style={{ marginBottom: 'var(--space-16)' }}>
        <StatusBadge variant="gray">{category}</StatusBadge>
      </div>
      <h3 className={styles.title}>{title}</h3>
      <p className={styles.description}>{description}</p>
      <div className={styles.footer}>
        <span className={styles.dateText}>최종 업데이트: {updatedAt}</span>
      </div>
    </div>
  );
}
