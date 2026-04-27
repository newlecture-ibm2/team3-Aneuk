import React from 'react';
import HandoverTable from './HandoverTable';
import styles from './HandoverRecord.module.css';

export interface HandoverItem {
  id: string | number;
  roomNumber: string;
  guestName: string;
  requestDetails: string;
}

export interface HandoverRecordProps {
  managerName: string;
  workingHours: string;
  date: string;
  items: HandoverItem[];
}

export default function HandoverRecord({ managerName, workingHours, date, items }: HandoverRecordProps) {
  return (
    <div className={styles.container}>
      <h2 className={styles.title}>인수인계 기록</h2>
      
      <div className={styles.metadataContainer}>
        <div className={styles.metaRow}>
          <span><strong>담당자:</strong> {managerName}</span>
        </div>
        <div className={styles.metaRow}>
          <span><strong>날짜:</strong> {date} <span style={{ marginLeft: '12px' }}><strong>근무시간:</strong> {workingHours}</span></span>
        </div>
      </div>

      <div className={styles.tableWrapper}>
        <HandoverTable items={items} />
      </div>
    </div>
  );
}
