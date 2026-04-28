import React from 'react';
import styles from './SummaryCard.module.css';

export interface SummaryCardProps {
  title: string;
  value: string | number;
  changeValue?: string;
  changeType?: 'positive' | 'negative' | 'neutral';
}

export default function SummaryCard({
  title,
  value,
  changeValue,
  changeType = 'neutral'
}: SummaryCardProps) {
  return (
    <div className={styles.summaryCard}>
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
