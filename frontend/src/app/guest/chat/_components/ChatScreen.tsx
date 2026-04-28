import React, { useRef, useEffect } from 'react';
import styles from './ChatScreen.module.css';
import ChatBubble from './ChatBubble';
import ChatInput from './ChatInput';
import TypingIndicator from './TypingIndicator';

export interface ChatMessage {
  id: string;
  variant: 'sent' | 'received';
  content: string;
}

export interface ChatScreenProps {
  messages: ChatMessage[];
  isTyping: boolean;
  onSendMessage: (text: string) => void;
}

export default function ChatScreen({ messages, isTyping, onSendMessage }: ChatScreenProps) {
  const messagesEndRef = useRef<HTMLDivElement>(null);

  // Auto-scroll to bottom when messages or typing state changes
  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages, isTyping]);

  return (
    <div className={styles.chatScreen}>
      <div className={styles.header}>
        <div className={styles.logo}>Anook</div>
      </div>
      
      <div className={styles.messageList}>
        {messages.map((msg) => (
          <ChatBubble key={msg.id} variant={msg.variant}>
            {msg.content}
          </ChatBubble>
        ))}
        {isTyping && (
          <ChatBubble variant="received">
            <TypingIndicator />
          </ChatBubble>
        )}
        <div ref={messagesEndRef} />
      </div>
      
      <div className={styles.footer}>
        <ChatInput onSend={onSendMessage} />
      </div>
    </div>
  );
}
