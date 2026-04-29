'use client';

import React from 'react';
import { ModalOverlay, ModalCard } from '@/components/ui/Modal';
import { X, Clock, Edit2 } from 'lucide-react';
import Button from '@/components/ui/Button/Button';
import StatusBadge from '@/components/ui/StatusBadge/StatusBadge';
import styles from './KnowledgeModal.module.css';

export interface KnowledgeModalProps {
  isOpen: boolean;
  onClose: () => void;
  category: string;
  title: string;
  description: string;
  updatedAt: string;
  onEdit?: () => void;
}

export default function KnowledgeModal({
  isOpen,
  onClose,
  category,
  title,
  description,
  updatedAt,
  onEdit
}: KnowledgeModalProps) {
  return (
    <ModalOverlay isOpen={isOpen} onClose={onClose}>
      <ModalCard size="lg">
        {/* Header */}
        <div className={styles.header}>
          <div className={styles.headerLeft}>
            <StatusBadge variant="gray">{category}</StatusBadge>
            <h2 className={styles.title}>{title}</h2>
          </div>
          <button className={styles.closeBtn} onClick={onClose}>
            <X size={24} />
          </button>
        </div>
        
        {/* Body */}
        <div className={styles.body}>
          <div className={styles.descriptionBox}>
            {description}
          </div>
        </div>
        
        {/* Footer */}
        <div className={styles.footer}>
          <div className={styles.dateInfo}>
            <Clock size={16} className={styles.clockIcon} />
            <span className={styles.dateText}>최종 업데이트: {updatedAt}</span>
          </div>
          <Button variant="primary" onClick={onEdit} className={styles.editBtn}>
            <Edit2 size={16} />
            정보 수정하기
          </Button>
        </div>
      </ModalCard>
    </ModalOverlay>
  );
}
