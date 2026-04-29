import React, { useState } from 'react';
import styles from './ChatModal.module.css';
import ModalOverlay from './ModalOverlay';
import ModalCard from './ModalCard';
import ChatBubble from '@/app/guest/chat/_components/ChatBubble';
import ChatInput from '@/app/guest/chat/_components/ChatInput';
import { CancelIcon } from '@/components/icons';

export interface ChatMessage {
  id: string;
  variant: 'sent' | 'received'; // Staff perspective: 'sent' = staff/AI, 'received' = guest
  content: string;
}

export interface ChatModalProps {
  isOpen: boolean;
  onClose: () => void;
  roomNumber?: string;
  initialMessages?: ChatMessage[];
}

const defaultStaffMessages: ChatMessage[] = [
  {
    id: '1',
    variant: 'sent',
    content: '안녕하세요! 그랜드 호텔 AI 서비스입니다. 무엇을 도와드릴까요?'
  },
  {
    id: '2',
    variant: 'received',
    content: '에어컨이 작동하지 않아요. 그리고 수건에서 하얀 실밥이 떨어져요. 수건 좀 교체해주세요.'
  },
  {
    id: '3',
    variant: 'sent',
    content: '안녕하세요! 수건 상태로 인해 불편을 드려 죄송합니다. 하우스키핑 담당자가 즉시 새 수건을 객실로 가져다 드릴 수 있도록 조치했습니다.'
  },
  {
    id: '4',
    variant: 'sent',
    content: '에어컨 작동 문제에 대해서도 확인했습니다. 현재 에어컨 시스템에 기술적인 결함이 있는 것으로 파악되어, 엔지니어링 수리팀이 즉시 객실로 방문하여 점검 및 수리를 진행해 드릴 예정입니다. 최대한 빨리 조치해 드리겠습니다.'
  }
];

export default function ChatModal({ isOpen, onClose, roomNumber = '1204', initialMessages = defaultStaffMessages }: ChatModalProps) {
  const [messages, setMessages] = useState<ChatMessage[]>(initialMessages);

  const handleSend = (text: string) => {
    const newUserMsg: ChatMessage = { id: Date.now().toString(), variant: 'sent', content: text };
    setMessages(prev => [...prev, newUserMsg]);
  };

  return (
    <ModalOverlay isOpen={isOpen} onClose={onClose}>
      <ModalCard size="md" padding={0}>
        <div className={styles.chatModalContainer}>
          <div className={styles.header}>
            <div className={styles.headerInfo}>
              <span className={styles.roomBadge}>객실 {roomNumber}</span>
              <h3 className={styles.title}>고객 상담</h3>
            </div>
            <button className={styles.closeButton} onClick={onClose} aria-label="닫기">
              <CancelIcon width={24} height={24} color="var(--color-gray-500)" />
            </button>
          </div>
          
          <div className={styles.messageList}>
            {messages.map((msg) => (
              <ChatBubble key={msg.id} variant={msg.variant}>
                {msg.content}
              </ChatBubble>
            ))}
          </div>
          
          <div className={styles.footer}>
            <ChatInput placeholder="고객에게 답변을 입력하세요..." onSend={handleSend} />
          </div>
        </div>
      </ModalCard>
    </ModalOverlay>
  );
}
