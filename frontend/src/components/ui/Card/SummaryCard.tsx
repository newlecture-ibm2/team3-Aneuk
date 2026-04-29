import React from 'react';
import styles from './SummaryCard.module.css';

export interface SummaryCardProps {
  title: string;
  value: string | number;
  changeValue?: string;
  changeType?: 'positive' | 'negative' | 'neutral';
  size?: 'sm' | 'md';
}

export default function SummaryCard({
  title,
  value,
  changeValue,
  changeType = 'neutral',
  size = 'md'
}: SummaryCardProps) {
  const sizeClass = size === 'sm' ? styles.sm : styles.md;
  return (
    <div className={`${styles.summaryCard} ${sizeClass}`}>
      <span className={styles.title}>{title}</span>
      <div className={styles.bottomRow}>
        <span className={styles.value}>{value}</span>
        {changeValue && (
          <span className={`${styles.change} ${styles[changeType]}`}>
            {changeValue}
          </span>
        )}
      </div>
    </div>
  );
}
