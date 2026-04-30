'use client';

import React, { useState } from 'react';
import InputField from '@/components/ui/Inputfield/InputField';
import FilterButton from '@/components/ui/FilterButton/FilterButton';
import RequestCard from '@/components/ui/Card/RequestCard';
import styles from './page.module.css';

export default function EmergencyPage() {
  const [searchValue, setSearchValue] = useState('');

  return (
    <div className={styles.container}>
      {/* Header Section */}
      <div className={styles.header}>
        <div className={styles.headerLeft}>
          <h1 className={styles.title}>긴급 대응</h1>
        </div>
        <div className={styles.headerActions}>
          <InputField 
            variant="search" 
            placeholder="검색어를 입력하세요..." 
            value={searchValue}
            onChange={(e) => setSearchValue(e.target.value)}
          />
          <FilterButton 
            filterOptions={[
              { label: '전체', value: 'all' }, 
              { label: '최신순', value: 'latest' }
            ]}
            selectedFilter="all"
            onFilterSelect={() => {}}
          />
        </div>
      </div>

      {/* Content Section */}
      <div className={styles.cardList}>
        {/* Emergency Request Card */}
        <RequestCard 
          roomType="객실"
          roomNumber="1001" 
          title="객실 내 응급 환자" 
          description="심한 복통 호소, 의료진 지원 필요"
          statusText="CRITICAL"
          statusVariant="red"
          createdAt={new Date(Date.now() - 1000 * 60 * 15)} // 15 mins ago
          variant="warning"
          primaryActionText="긴급 대응 시작"
          secondaryActionText="엔지니어 호출"
        />
      </div>
    </div>
  );
}
