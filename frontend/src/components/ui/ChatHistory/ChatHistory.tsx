'use client';

import React from 'react';
import styles from './ChatHistory.module.css';
import { DoorOpen, ChevronRight } from 'lucide-react';

export interface ChatHistoryData {
  id: string | number;
  roomNumber: string | number;
  statusText: string;
}

export interface ChatHistoryProps {
  title?: string;
  rooms: ChatHistoryData[];
  activeRoomId?: string | number;
  onRoomSelect?: (roomId: string | number) => void;
}

export default function ChatHistory({ title = '객실 선택', rooms, activeRoomId, onRoomSelect }: ChatHistoryProps) {
  return (
    <div className={styles.container}>
      {title && <h3 className={styles.title}>{title}</h3>}
      <div className={styles.list}>
        {rooms.map((room) => {
          const isActive = room.id === activeRoomId;
          return (
            <div 
              key={room.id} 
              className={`${styles.item} ${isActive ? styles.itemActive : ''}`}
              onClick={() => onRoomSelect && onRoomSelect(room.id)}
            >
              <div className={`${styles.iconBox} ${isActive ? styles.iconBoxActive : ''}`}>
                <DoorOpen size={24} className={isActive ? styles.iconActive : styles.icon} />
              </div>
              <div className={styles.textContainer}>
                <span className={styles.roomNumber}>{room.roomNumber}호</span>
                <span className={styles.statusText}>{room.statusText}</span>
              </div>
              {isActive && <ChevronRight size={20} className={styles.chevron} />}
            </div>
          );
        })}
      </div>
    </div>
  );
}
