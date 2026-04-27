import React from 'react';
import styles from './RagCandidateCard.module.css';
import Button from '@/components/ui/Button/Button';

export interface RagCandidateCardProps {
  category?: string;
  roomNumber?: string;
  consultationContent: string;
  timestamp: string;
  onAddRag: (extractedContent: string) => void;
  onReject: () => void;
}

export default function RagCandidateCard({
  category = '수동 상담',
  roomNumber,
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
      {roomNumber && (
        <div className={styles.roomBox}>
          <span className={styles.roomType}>객실</span>
          <span className={styles.roomNumber}>{roomNumber}</span>
        </div>
      )}

      <div className={styles.contentSection}>
        <div className={styles.contentHeader}>
          <span className={styles.category}>{category}</span>
          <span className={styles.timeText}>
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
              <circle cx="12" cy="12" r="10"></circle>
              <polyline points="12 6 12 12 16 14"></polyline>
            </svg>
            {timestamp}
          </span>
        </div>
        <h3 className={styles.title}>
          {displayContent}
        </h3>
      </div>

      <div className={styles.actionSection}>
        <Button variant="primary" onClick={() => onAddRag(displayContent.replace(/(^"|"$)/g, ''))} style={{ width: '100%', padding: 'var(--space-8)' }}>RAG 추가</Button>
        <Button variant="secondary" onClick={onReject} style={{ width: '100%', padding: 'var(--space-8)' }}>제외</Button>
      </div>
    </div>
  );
}
