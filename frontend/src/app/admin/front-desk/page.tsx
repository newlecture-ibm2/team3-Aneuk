'use client';

import React, { useState } from 'react';
import Tabs from '@/components/ui/Tab/Tabs';
import RequestCard from '@/components/ui/Card/RequestCard';
import styles from './page.module.css';

export default function FrontDeskPage() {
  const [activeTab, setActiveTab] = useState('unhandled');

  return (
    <div className={styles.container}>
      {/* Header Section */}
      <div className={styles.header}>
        <span className={styles.subtitle}>부서별 접수 대기 중인 요청을 확인하고 배정할 수 있습니다.</span>
        <h1 className={styles.title}>프론트 데스크</h1>
      </div>

      {/* Tabs Section */}
      <div className={styles.tabSection}>
        <Tabs 
          options={[
            { label: '전체 요청', value: 'all' },
            { label: '미처리 대기', value: 'unhandled', count: 5 },
            { label: '예외 발생', value: 'exception', count: 2 }
          ]}
          activeValue={activeTab}
          onChange={(val) => setActiveTab(val || 'unhandled')}
        />
      </div>

      {/* Content Section */}
      <div className={styles.contentSection}>
        <h2 className={styles.sectionTitle}>미처리 / 예외 요청</h2>
        
        <div className={styles.cardGrid}>
          {/* Dummy Request Cards */}
          <RequestCard 
            roomType="스위트룸"
            roomNumber="101" 
            title="에어컨이 너무 추워요, 온도 조절 부탁드립니다." 
            statusText="프론트 대기"
            statusVariant="red"
            createdAt={new Date()}
            variant="warning"
            primaryActionText="상담 시작"
            secondaryActionText="수동 배정"
          />
          <RequestCard 
            roomType="디럭스 트윈"
            roomNumber="204" 
            title="수건 2장 추가 요청" 
            statusText="AI 의도 파악 실패"
            statusVariant="purple"
            createdAt={new Date(Date.now() - 1000 * 60 * 30)}
            primaryActionText="상담 시작"
            secondaryActionText="수동 배정"
          />
          <RequestCard 
            roomType="스탠다드 더블"
            roomNumber="502" 
            title="룸서비스 메뉴판이 없어요" 
            statusText="프론트 대기"
            statusVariant="gray"
            createdAt={new Date(Date.now() - 1000 * 60 * 120)}
            primaryActionText="상담 시작"
            secondaryActionText="수동 배정"
          />
        </div>
      </div>
    </div>
  );
}
