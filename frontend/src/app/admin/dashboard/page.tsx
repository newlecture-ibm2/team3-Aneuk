'use client';

import React, { useState } from 'react';
import InputField from '@/components/ui/Inputfield/InputField';
import FilterButton from '@/components/ui/FilterButton/FilterButton';
import ChartCard from '@/components/ui/Card/ChartCard';
import SummaryCard from '@/components/ui/Card/SummaryCard';
import styles from './page.module.css';

export default function DashboardPage() {
  const [searchValue, setSearchValue] = useState('');
  const [selectedFilter, setSelectedFilter] = useState('7d');

  return (
    <div className={styles.container}>
      {/* Header Section */}
      <div className={styles.header}>
        <div>
          <span className={styles.subtitle}>호텔 운영 관리 시스템</span>
          <h1 className={styles.title}>대시보드</h1>
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
              { label: '오늘', value: '1d' }, 
              { label: '최근 7일', value: '7d' }, 
              { label: '최근 30일', value: '30d' }
            ]}
            selectedFilter={selectedFilter}
            onFilterSelect={(val) => setSelectedFilter(val)}
          />
        </div>
      </div>

      {/* Main Content Section (Charts) */}
      <div className={styles.chartsGrid}>
        <ChartCard title="부서별 평균 처리 시간" subtitle="AVERAGE RESOLUTION TIME (MINUTES)">
           {/* Placeholder for bar chart */}
           <div className={styles.chartPlaceholder}>
              (막대 그래프 들어갈 자리)
           </div>
        </ChartCard>
        
        <ChartCard title="최다 요청 항목" subtitle="MOST FREQUENT REQUESTS (%)">
           {/* Placeholder for donut chart */}
           <div className={styles.chartPlaceholder}>
              (도넛 차트 들어갈 자리)
           </div>
        </ChartCard>
      </div>

      {/* Bottom Content Section (Summaries) */}
      <div className={styles.summaryGrid}>
        <SummaryCard 
          title="오늘 총 요청" 
          value={7} 
          changeValue="+12%" 
          changeType="positive" 
          size="md" 
        />
        <SummaryCard 
          title="평균 응답 시간" 
          value="1.5m" 
          changeValue="-0.2m" 
          changeType="positive" 
          size="md" 
        />
        <SummaryCard 
          title="해결률" 
          value="94%" 
          changeValue="+2%" 
          changeType="positive" 
          size="md" 
        />
        <SummaryCard 
          title="고객 만족도" 
          value="4.8" 
          changeValue="+0.1" 
          changeType="positive" 
          size="md" 
        />
      </div>
    </div>
  );
}
