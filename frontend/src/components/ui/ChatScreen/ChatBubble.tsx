import React from 'react';
import styles from './ChatBubble.module.css';

export interface ChatBubbleProps {
  variant: 'sent' | 'received';
  children: React.ReactNode;
}

export default function ChatBubble({ variant, children }: ChatBubbleProps) {
  return (
    <div className={`${styles.wrapper} ${variant === 'sent' ? styles.sentWrapper : styles.receivedWrapper}`}>
      <div className={`${styles.bubble} ${styles[variant]}`}>
        {children}
      </div>
    </div>
  );
}
