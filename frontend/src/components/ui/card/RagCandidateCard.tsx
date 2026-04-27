import React from 'react';
import styles from './RagCandidateCard.module.css';
import Button from '@/components/ui/Button/Button';
import StatusBadge from '@/components/ui/StatusBadge/StatusBadge';

export interface RagCandidateCardProps {
  category?: string;
  roomNumber?: string;
  consultationTitle: string;
  consultationContent: string;
  timestamp: string;
  onAddRag: (extractedContent: string) => void;
  onReject: () => void;
}

export default function RagCandidateCard({
  category = '수동 상담',
  roomNumber,
  consultationTitle,
  consultationContent,
  timestamp,
  onAddRag,
  onReject
}: RagCandidateCardProps) {

  // 대화 내용 중 '직원:'으로 시작하는 가장 긴 답장 추출
  const getLongestStaffReply = (text: string) => {
    const lines = text.split('\n');
    let longestReply = text;
    let maxLength = 0;
    
    const staffLines = lines.filter(line => line.includes('직원:'));
    if (staffLines.length > 0) {
      staffLines.forEach(line => {
        const reply = line.split('직원:')[1].trim();
        if (reply.length > maxLength) {
          maxLength = reply.length;
          longestReply = reply;
        }
      });
      return `"${longestReply}"`;
    }
    
    return `"${text.trim()}"`;
  };

  const displayContent = getLongestStaffReply(consultationContent);

  return (
    <div className={styles.card}>
      <div className={styles.header}>
        <div className={styles.badgeGroup}>
          <StatusBadge variant="purple">지식 후보</StatusBadge>
          <span className={styles.category}>{category}</span>
        </div>
        <span className={styles.timeText}>
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
            <circle cx="12" cy="12" r="10"></circle>
            <polyline points="12 6 12 12 16 14"></polyline>
          </svg>
          {timestamp}
        </span>
      </div>

      <div className={styles.body}>
        <h3 className={styles.title}>
          {roomNumber && <span className={styles.roomText}>[{roomNumber}호]</span>} {consultationTitle}
        </h3>
        <p className={styles.content}>{displayContent}</p>
      </div>

      <div className={styles.actions}>
        <Button variant="secondary" onClick={onReject} style={{ flex: 1 }}>제외</Button>
        <Button variant="primary" onClick={() => onAddRag(displayContent.replace(/(^"|"$)/g, ''))} style={{ flex: 1 }}>RAG 추가</Button>
      </div>
    </div>
  );
}
