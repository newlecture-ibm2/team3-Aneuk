import React from 'react';
import styles from './TaskColumn.module.css';

export interface TaskColumnProps {
  title: string;
  count?: number;
  children?: React.ReactNode;
  className?: string;
}

export default function TaskColumn({ title, count = 0, children, className = '' }: TaskColumnProps) {
  return (
    <div className={`${styles.column} ${className}`.trim()}>
      <div className={styles.header}>
        <h3 className={styles.title}>{title}</h3>
        {count > 0 && <span className={styles.count}>{count}</span>}
      </div>
      <div className={styles.content}>
        {children}
      </div>
    </div>
  );
}
