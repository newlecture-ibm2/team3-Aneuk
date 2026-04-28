'use client';

import React from 'react';
import ModalOverlay from './ModalOverlay';
import ModalCard from './ModalCard';
import { CancelIcon, CodeIcon } from '@/components/icons';
import Tag from '@/components/ui/StatusBadge/StatusBadge';
import SummaryCard from '@/components/ui/Card/SummaryCard';
import Button from '@/components/ui/Button/Button';
import styles from './LogDataModal.module.css';

interface LogDataModalProps {
  isOpen: boolean;
  onClose: () => void;
  // In a real implementation, we would pass log data as props
  // For now, we use the static data from the design
}

export default function LogDataModal({ isOpen, onClose }: LogDataModalProps) {
  if (!isOpen) return null;

  return (
    <ModalOverlay isOpen={isOpen} onClose={onClose}>
      <ModalCard size="lg">
        <div className={styles.modalContent}>
          {/* Header */}
          <div className={styles.header}>
            <div className={styles.titleSection}>
              <div className={styles.iconBox}>
                <CodeIcon />
              </div>
              <div className={styles.titleGroup}>
                <div className={styles.titleRow}>
                  <h2 className={styles.title}>로그 데이터 분석</h2>
                  <Tag variant="gray">RAW DATA</Tag>
                </div>
                <p className={styles.subtitle}>
                  연결된 티켓 번호: <span className={styles.ticketId}>request_id: #1029</span>
                </p>
              </div>
            </div>
            <button className={styles.closeButton} onClick={onClose} aria-label="닫기">
              <CancelIcon />
            </button>
          </div>

          {/* Stats Grid */}
          <div className={styles.statsGrid}>
            <SummaryCard title="입력 토큰" value="1,200" size="sm" />
            <SummaryCard title="출력 토큰" value="320" size="sm" />
            <SummaryCard title="처리 시간" value="850ms" size="sm" />
            <SummaryCard title="사용 모델" value="gemini-1.5-flash" size="sm" />
          </div>

          {/* Prompt Section */}
          <div className={styles.section}>
            <h3 className={styles.sectionTitle}>입력 프롬프트 원문 (RAW PROMPT)</h3>
            <div className={styles.promptText}>
              당신은 일류 호텔의 컨시어지 AI입니다. 고객의 요청을 분석하여 부서를 할당하세요.<br />
              고객 요청: &quot;수건 2장 더 가져다주세요. 그리고 가습기가 있으면 좋겠어요.&quot;
            </div>
          </div>

          {/* Response Section */}
          <div className={styles.section}>
            <h3 className={styles.sectionTitle}>AI 출력 JSON 원문 (RAW RESPONSE)</h3>
            <div className={styles.jsonContainer}>
              <div className={styles.jsonLine}>
                <span className={styles.lineNumber}>1</span>
                <span><span className={styles.jsonSyntax}>{'{'}</span></span>
              </div>
              <div className={styles.jsonLine}>
                <span className={styles.lineNumber}>2</span>
                <span>
                  &nbsp;&nbsp;<span className={styles.jsonKey}>&quot;domain&quot;</span><span className={styles.jsonSyntax}>: </span><span className={styles.jsonString}>&quot;HK&quot;</span><span className={styles.jsonSyntax}>,</span>
                </span>
              </div>
              <div className={styles.jsonLine}>
                <span className={styles.lineNumber}>3</span>
                <span>
                  &nbsp;&nbsp;<span className={styles.jsonKey}>&quot;intent&quot;</span><span className={styles.jsonSyntax}>: </span><span className={styles.jsonString}>&quot;request_items&quot;</span><span className={styles.jsonSyntax}>,</span>
                </span>
              </div>
              <div className={styles.jsonLine}>
                <span className={styles.lineNumber}>4</span>
                <span>
                  &nbsp;&nbsp;<span className={styles.jsonKey}>&quot;items&quot;</span><span className={styles.jsonSyntax}>: [</span><span className={styles.jsonString}>&quot;towel&quot;</span><span className={styles.jsonSyntax}>, </span><span className={styles.jsonString}>&quot;humidifier&quot;</span><span className={styles.jsonSyntax}>],</span>
                </span>
              </div>
              <div className={styles.jsonLine}>
                <span className={styles.lineNumber}>5</span>
                <span>
                  &nbsp;&nbsp;<span className={styles.jsonKey}>&quot;quantity&quot;</span><span className={styles.jsonSyntax}>: [</span><span className={styles.jsonNumber}>2</span><span className={styles.jsonSyntax}>, </span><span className={styles.jsonNumber}>1</span><span className={styles.jsonSyntax}>],</span>
                </span>
              </div>
              <div className={styles.jsonLine}>
                <span className={styles.lineNumber}>6</span>
                <span>
                  &nbsp;&nbsp;<span className={styles.jsonKey}>&quot;priority&quot;</span><span className={styles.jsonSyntax}>: </span><span className={styles.jsonString}>&quot;medium&quot;</span>
                </span>
              </div>
              <div className={styles.jsonLine}>
                <span className={styles.lineNumber}>7</span>
                <span><span className={styles.jsonSyntax}>{'}'}</span></span>
              </div>
            </div>
          </div>

          {/* Footer */}
          <div className={styles.footer}>
            <Button variant="primary" size="large" fullWidth onClick={onClose}>
              분석 완료 및 닫기
            </Button>
          </div>
        </div>
      </ModalCard>
    </ModalOverlay>
  );
}
