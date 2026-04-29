'use client';

import React, { useState } from 'react';
import styles from './FeedbackCard.module.css';
import { ReviewStarIcon } from '@/components/icons';

const RATING_LABELS = ['', '별로예요', '그저 그래요', '보통이에요', '좋았어요', '최고예요!'];

export interface FeedbackCardProps {
  onSubmit: (rating: number) => void;
}

export default function FeedbackCard({ onSubmit }: FeedbackCardProps) {
  const [rating, setRating] = useState(0);
  const [hoverRating, setHoverRating] = useState(0);

  const activeRating = hoverRating || rating;

  const handleStarClick = (star: number) => {
    setRating(star);
    onSubmit(star);
  };

  return (
    <div className={styles.card}>
      {/* 헤더 */}
      <div className={styles.textWrapper}>
        <div className={styles.titleBox}>
          <h2 className={styles.title}>
            서비스가 만족스러우셨나요?
          </h2>
        </div>
      </div>

      {/* 별점 */}
      <div className={styles.ratingWrapper}>
        <div className={styles.stars}>
          {[1, 2, 3, 4, 5].map((star) => (
            <button
              key={star}
              type="button"
              className={styles.starButton}
              onClick={() => handleStarClick(star)}
              onMouseEnter={() => setHoverRating(star)}
              onMouseLeave={() => setHoverRating(0)}
              aria-label={`${star}점`}
            >
              <ReviewStarIcon
                className={`${styles.starIcon} ${star <= activeRating ? styles.starFilled : styles.starEmpty}`}
                fill={star <= activeRating ? 'currentColor' : 'none'}
              />
            </button>
          ))}
        </div>
        <span className={styles.ratingLabel}>
          {RATING_LABELS[activeRating] || '별점을 선택해주세요'}
        </span>
      </div>
    </div>
  );
}
