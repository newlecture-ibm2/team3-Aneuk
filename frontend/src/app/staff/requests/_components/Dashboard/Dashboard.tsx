'use client';

import React, { useMemo, useState } from 'react';
import Sidebar from '@/components/layout/Sidebar';
import TaskColumn from '@/components/ui/TaskBoard/TaskColumn';
import TaskTicket from '@/components/ui/TaskBoard/TaskTicket';
import InputField from '@/components/ui/Inputfield/InputField';
import FilterButton from '@/components/ui/FilterButton/FilterButton';
import { useSearchParams } from 'next/navigation';
import { useTasks } from '../../_hooks/useTasks';
import styles from './Dashboard.module.css';

const COLUMN_CONFIG = [
  { id: 'PENDING', title: '대기 중', status: 'TODO' },
  { id: 'IN_PROGRESS', title: '진행 중', status: 'IN_PROGRESS' },
  { id: 'COMPLETED', title: '완료됨', status: 'DONE' },
];

const PRIORITY_OPTIONS = [
  { label: '전체 우선순위', value: 'ALL' },
  { label: '긴급 (URGENT)', value: 'URGENT' },
  { label: '높음 (HIGH)', value: 'HIGH' },
  { label: '일반 (NORMAL)', value: 'NORMAL' },
  { label: '낮음 (LOW)', value: 'LOW' },
];

export default function Dashboard() {
  const searchParams = useSearchParams();
  const view = searchParams.get('view');
  
  // '내 작업(view=my)'일 경우에만 부서 필터링 (임시로 'HK' 사용)
  const departmentId = view === 'my' ? 'HK' : undefined;
  const { tasks, loading, error } = useTasks(departmentId);

  // 필터 상태 관리
  const [searchQuery, setSearchQuery] = useState('');
  const [priorityFilter, setPriorityFilter] = useState('ALL');

  // 필터링된 태스크 (실시간 업데이트 반영)
  const filteredTasks = useMemo(() => {
    return tasks.filter(task => {
      // 1. 우선순위 필터링
      if (priorityFilter !== 'ALL' && task.priority !== priorityFilter) {
        return false;
      }

      // 2. 검색어 필터링 (객실번호, 요약, 원문)
      if (searchQuery) {
        const query = searchQuery.toLowerCase();
        const matches =
          task.roomNumber.toString().includes(query) ||
          task.summary.toLowerCase().includes(query) ||
          task.rawText.toLowerCase().includes(query) ||
          task.id.toString().includes(query);

        if (!matches) return false;
      }

      return true;
    });
  }, [tasks, searchQuery, priorityFilter]);

  // 태스크를 컬럼별로 분류
  const boardData = useMemo(() => {
    return {
      TODO: filteredTasks.filter(t => t.status === 'PENDING'),
      IN_PROGRESS: filteredTasks.filter(t => t.status === 'ASSIGNED' || t.status === 'IN_PROGRESS'),
      DONE: filteredTasks.filter(t => t.status === 'COMPLETED'),
    };
  }, [filteredTasks]);

  return (
    <div className={styles.container}>
      <Sidebar role="housekeeping" />

      <main className={styles.mainContent}>
        <div className={styles.headerContainer}>
          <header className={styles.header}>
            <h1 className={styles.title}>하우스키핑 관리</h1>
            <p className={styles.subtitle}>하우스키핑 전용 채널</p>
          </header>

          <div className={styles.toolbar}>
            <div className={styles.searchBox}>
              <InputField
                variant="search"
                placeholder="객실번호 또는 내용 검색..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
              />
            </div>
            <FilterButton
              filterOptions={PRIORITY_OPTIONS}
              selectedFilter={priorityFilter}
              onFilterSelect={setPriorityFilter}
            />
          </div>
        </div>

        {loading ? (
          <div className={styles.loading}>데이터를 불러오는 중...</div>
        ) : error ? (
          <div className={styles.error}>데이터를 불러오는 데 실패했습니다. ({error})</div>
        ) : (
          <section className={styles.board}>
            {COLUMN_CONFIG.map(col => {
              const columnTasks = boardData[col.status as keyof typeof boardData];
              return (
                <TaskColumn
                  key={col.id}
                  title={col.title}
                  count={columnTasks.length}
                >
                  <div className={styles.columnContent}>
                    {columnTasks.map(task => (
                      <TaskTicket
                        key={task.id}
                        ticketId={task.id}
                        priority={mapPriority(task.priority)}
                        title={`[${task.roomNumber}호] ${task.summary}`}
                        description={task.rawText}
                        status={col.status as 'TODO' | 'IN_PROGRESS' | 'DONE'}
                        createdAt={task.createdAt}
                      />
                    ))}
                    {columnTasks.length === 0 && (
                      <div className={styles.empty}>해당하는 작업이 없습니다.</div>
                    )}
                  </div>
                </TaskColumn>
              );
            })}
          </section>
        )}
      </main>
    </div>
  );
}

function mapPriority(p: string): 'LOW' | 'MEDIUM' | 'HIGH' | 'URGENT' {
  switch (p) {
    case 'URGENT': return 'URGENT';
    case 'HIGH': return 'HIGH';
    case 'LOW': return 'LOW';
    default: return 'MEDIUM';
  }
}
