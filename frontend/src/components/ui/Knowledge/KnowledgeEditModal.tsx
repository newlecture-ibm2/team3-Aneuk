'use client';

import React, { useState } from 'react';
import { ModalOverlay, ModalCard } from '@/components/ui/Modal';
import { X } from 'lucide-react';
import Button from '@/components/ui/Button/Button';
import InputField from '@/components/ui/Inputfield/InputField';
import Dropdown from '@/components/ui/Dropdown/Dropdown';
import StatusBadge from '@/components/ui/StatusBadge/StatusBadge';
import { ConfirmModal } from '@/components/ui/Modal';
import { useUiStore } from '@/stores/useUiStore';
import styles from './KnowledgeEditModal.module.css';

export interface KnowledgeEditModalProps {
  isOpen: boolean;
  onClose: () => void;
  initialCategory?: string;
  initialTitle?: string;
  initialDescription?: string;
  onSave?: (data: { category: string; title: string; description: string }) => void;
}

export default function KnowledgeEditModal({
  isOpen,
  onClose,
  initialCategory = '',
  initialTitle = '',
  initialDescription = '',
  onSave
}: KnowledgeEditModalProps) {
  const [category, setCategory] = useState(initialCategory);
  const [title, setTitle] = useState(initialTitle);
  const [description, setDescription] = useState(initialDescription);
  const [isConfirmOpen, setIsConfirmOpen] = useState(false);
  const showToast = useUiStore(state => state.showToast);

  const categoryOptions = [
    { value: '체크인/아웃', label: '체크인/아웃' },
    { value: '부대시설', label: '부대시설' },
    { value: '주변 관광', label: '주변 관광' },
    { value: '교통 정보', label: '교통 정보' }
  ];

  return (
    <>
      <ModalOverlay isOpen={isOpen} onClose={() => setIsConfirmOpen(true)}>
        <ModalCard size="md">
        {/* Header */}
        <div className={styles.header}>
          <div className={styles.headerLeft}>
            <StatusBadge variant="gray">정보 수정</StatusBadge>
            <h2 className={styles.title}>지식 정보 수정</h2>
          </div>
          <button className={styles.closeBtn} onClick={() => setIsConfirmOpen(true)}>
            <X size={24} />
          </button>
        </div>

        {/* Body */}
        <div className={styles.body}>
          <div className={styles.formGroup}>
            <label className={styles.label}>카테고리</label>
            <Dropdown
              options={categoryOptions}
              value={category}
              onChange={(val) => setCategory(val as string)}
              placeholder="카테고리 선택"
            />
          </div>

          <div className={styles.formGroup}>
            <label className={styles.label}>제목</label>
            <InputField
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              placeholder="제목을 입력하세요"
            />
          </div>

          <div className={styles.formGroup}>
            <label className={styles.label}>내용</label>
            <textarea
              className={styles.textarea}
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              placeholder="내용을 입력하세요"
            />
          </div>
        </div>

        {/* Footer */}
        <div className={styles.footer}>
          <Button variant="secondary" onClick={() => setIsConfirmOpen(true)} className={styles.btn}>
            취소
          </Button>
          <Button variant="primary" onClick={() => {
            if (onSave) onSave({ category, title, description });
            showToast('지식 정보가 성공적으로 수정되었습니다.', 'success');
          }} className={styles.btn}>
            변경사항 저장하기
          </Button>
        </div>
      </ModalCard>
    </ModalOverlay>
    {isConfirmOpen && (
      <ConfirmModal
        isOpen={isConfirmOpen}
        onClose={() => setIsConfirmOpen(false)}
        onConfirm={() => {
          setIsConfirmOpen(false);
          onClose();
        }}
        title="수정 취소"
        subtitle="수정 중인 내용이 저장되지 않습니다. 정말 취소하시겠습니까?"
        confirmText="네, 취소할게요"
        cancelText="계속 작성하기"
        status="danger"
      />
    )}
    </>
  );
}
