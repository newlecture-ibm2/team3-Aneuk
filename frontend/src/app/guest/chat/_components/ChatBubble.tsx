import React from 'react';
import styles from './ChatBubble.module.css';

export interface ChatBubbleProps {
  variant: 'sent' | 'received';
  isFallback?: boolean;
  children: React.ReactNode;
}

export default function ChatBubble({ variant, isFallback, children }: ChatBubbleProps) {
  return (
    <div className={`${styles.wrapper} ${variant === 'sent' ? styles.sentWrapper : styles.receivedWrapper}`}>
      <div className={`${styles.bubble} ${styles[variant]} ${isFallback ? styles.fallback : ''}`}>
        {children}
      </div>
    </div>
  );
}
