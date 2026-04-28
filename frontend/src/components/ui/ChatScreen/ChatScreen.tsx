import React, { useState } from 'react';
import styles from './ChatScreen.module.css';
import ChatBubble from './ChatBubble';
import ChatInput from './ChatInput';

export interface ChatMessage {
  id: string;
  variant: 'sent' | 'received';
  content: string;
}

export interface ChatScreenProps {
  initialMessages?: ChatMessage[];
  onSendMessage?: (text: string) => void;
}

const defaultMessages: ChatMessage[] = [
  {
    id: '1',
    variant: 'received',
    content: '안녕하세요! 그랜드 호텔 AI 서비스입니다. 무엇을 도와드릴까요?'
  },
  {
    id: '2',
    variant: 'sent',
    content: '에어컨이 작동하지 않아요. 그리고 수건에서 하얀 실밥이 떨어져요. 수건 좀 교체해주세요.'
  },
  {
    id: '3',
    variant: 'received',
    content: '안녕하세요! 수건 상태로 인해 불편을 드려 죄송합니다. 하우스키핑 담당자가 즉시 새 수건을 객실로 가져다 드릴 수 있도록 조치했습니다.'
  },
  {
    id: '4',
    variant: 'received',
    content: '에어컨 작동 문제에 대해서도 확인했습니다. 현재 에어컨 시스템에 기술적인 결함이 있는 것으로 파악되어, 엔지니어링 수리팀이 즉시 객실로 방문하여 점검 및 수리를 진행해 드릴 예정입니다. 최대한 빨리 조치해 드리겠습니다.'
  }
];

export default function ChatScreen({ initialMessages = defaultMessages, onSendMessage }: ChatScreenProps) {
  const [messages, setMessages] = useState<ChatMessage[]>(initialMessages);

  const handleSend = (text: string) => {
    // Add user message immediately
    const newUserMsg: ChatMessage = { id: Date.now().toString(), variant: 'sent', content: text };
    setMessages(prev => [...prev, newUserMsg]);
    
    if (onSendMessage) {
      onSendMessage(text);
    }
  };

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
      </div>
      
      <div className={styles.footer}>
        <ChatInput onSend={handleSend} />
      </div>
    </div>
  );
}
