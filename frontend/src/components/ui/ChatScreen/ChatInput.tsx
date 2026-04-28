import React, { useState } from 'react';
import styles from './ChatInput.module.css';
import { SendIcon } from '@/components/icons';

export interface ChatInputProps {
  placeholder?: string;
  onSend?: (text: string) => void;
}

export default function ChatInput({ placeholder = '무엇이든 물어보세요...', onSend }: ChatInputProps) {
  const [value, setValue] = useState('');

  const handleSend = () => {
    if (value.trim() && onSend) {
      onSend(value);
      setValue('');
    }
  };

  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      handleSend();
    }
  };

  return (
    <div className={styles.wrapper}>
      <input 
        className={styles.input} 
        placeholder={placeholder} 
        value={value} 
        onChange={(e) => setValue(e.target.value)}
        onKeyDown={handleKeyDown}
      />
      <button className={styles.sendButton} onClick={handleSend} aria-label="메시지 전송">
        <SendIcon size={24} color="var(--color-white)" />
      </button>
    </div>
  );
}
