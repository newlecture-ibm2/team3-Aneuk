import React, { ReactNode } from 'react';
import styles from './ChartCard.module.css';

export interface ChartCardProps {
  title: string;
  subtitle?: string;
  children?: ReactNode;
  className?: string;
}

export default function ChartCard({ title, subtitle, children, className = '' }: ChartCardProps) {
  return (
    <div className={`${styles.chartCard} ${className}`}>
      <div className={styles.header}>
        <h3 className={styles.title}>{title}</h3>
        {subtitle && <p className={styles.subtitle}>{subtitle}</p>}
      </div>
      <div className={styles.content}>
        {children}
      </div>
    </div>
  );
}
