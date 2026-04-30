'use client';

import React, { useState } from 'react';
import InputField from '@/components/ui/Inputfield/InputField';
import FilterButton from '@/components/ui/FilterButton/FilterButton';
import TaskColumn from '@/components/ui/TaskBoard/TaskColumn';
import TaskTicket from '@/components/ui/TaskBoard/TaskTicket';
import styles from './page.module.css';

export default function FbPage() {
  const [searchValue, setSearchValue] = useState('');

  return (
    <div className={styles.container}>
      {/* Header Section */}
      <div className={styles.header}>
        <div className={styles.headerLeft}>
          <h1 className={styles.title}>식음료</h1>
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

      {/* Task Board Section */}
      <div className={styles.board}>
        {/* Column 1: 대기 중 */}
        <div className={styles.columnWrapper}>
          <TaskColumn title="대기 중" count={1}>
            <TaskTicket 
              ticketId="1204"
              priority="MEDIUM"
              title="수건 교체"
              description="새 수건 3장 추가 요청"
              status="TODO"
              createdAt={new Date(Date.now() - 1000 * 60 * 15)} // 15 mins ago
            />
          </TaskColumn>
        </div>

        {/* Column 2: 진행 중 */}
        <div className={styles.columnWrapper}>
          <TaskColumn title="진행 중" count={1}>
            <TaskTicket 
              ticketId="502"
              priority="HIGH"
              title="침구류 정리"
              description="엑스트라 베드 설치 요청"
              status="IN_PROGRESS"
              createdAt={new Date(Date.now() - 1000 * 60 * 60)} // 1 hour ago
              updatedAt={new Date(Date.now() - 1000 * 60 * 5)} // started 5 mins ago
            />
          </TaskColumn>
        </div>

        {/* Column 3: 완료됨 */}
        <div className={styles.columnWrapper}>
          <TaskColumn title="완료됨" count={0}>
            {/* No tasks */}
          </TaskColumn>
        </div>
      </div>
    </div>
  );
}
