import React from 'react';
import styles from './HandoverTable.module.css';
import { HandoverItem } from './HandoverRecord';

interface HandoverTableProps {
  items: HandoverItem[];
}

export default function HandoverTable({ items }: HandoverTableProps) {
  return (
    <table className={styles.table}>
      <colgroup>
        <col style={{ width: '15%' }} />
        <col style={{ width: '15%' }} />
        <col style={{ width: '70%' }} />
      </colgroup>
      <thead>
        <tr>
          <th className={styles.th}>방 호수</th>
          <th className={styles.th}>고객명</th>
          <th className={styles.th}>요청 내용</th>
        </tr>
      </thead>
      <tbody>
        {items.map((item) => (
          <tr key={item.id}>
            <td className={styles.td}>{item.roomNumber}</td>
            <td className={styles.td}>{item.guestName}</td>
            <td className={styles.td}>{item.requestDetails}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}
