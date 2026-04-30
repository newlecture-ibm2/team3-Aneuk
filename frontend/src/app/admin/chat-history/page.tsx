'use client';

import React, { useState } from 'react';
import InputField from '@/components/ui/Inputfield/InputField';
import FilterButton from '@/components/ui/FilterButton/FilterButton';
import ChatHistory, { ChatHistoryData } from '@/components/ui/ChatHistory/ChatHistory';
import ChatBubble from '@/app/guest/chat/_components/ChatBubble';
import { MoreVertical } from 'lucide-react';
import styles from './page.module.css';

const dummyRooms: ChatHistoryData[] = [
  { id: '1001', roomNumber: '1001', statusText: '보관됨' },
  { id: '1204', roomNumber: '1204', statusText: '활성 대화' },
  { id: '2105', roomNumber: '2105', statusText: '보관됨' },
  { id: '302', roomNumber: '302', statusText: '보관됨' },
  { id: '502', roomNumber: '502', statusText: '보관됨' },
  { id: '805', roomNumber: '805', statusText: '보관됨' },
];

export default function ChatHistoryPage() {
  const [searchValue, setSearchValue] = useState('');
  const [activeRoomId, setActiveRoomId] = useState<string | number>('1001');
  const [isPopoverOpen, setIsPopoverOpen] = useState(false);

  return (
    <div className={styles.container}>
      {/* Header Section */}
      <div className={styles.header}>
        <div className={styles.headerLeft}>
          <h1 className={styles.title}>채팅 히스토리</h1>
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

      {/* Main Content Section */}
      <div className={styles.mainContent}>
        {/* Left Sidebar: Room List */}
        <div className={styles.sidebar}>
          <ChatHistory 
            rooms={dummyRooms} 
            activeRoomId={activeRoomId} 
            onRoomSelect={setActiveRoomId} 
          />
        </div>

        {/* Right Area: Chat Logs */}
        <div className={styles.chatArea}>
          {/* Chat Header */}
          <div className={styles.chatHeader}>
            <div className={styles.chatHeaderLeft}>
              <h2 className={styles.chatTitle}>{activeRoomId}호 채팅 기록</h2>
              <span className={styles.chatSubtitle}>전체 대화 로그</span>
            </div>
            <div className={styles.chatHeaderActions} onClick={() => setIsPopoverOpen(!isPopoverOpen)}>
              <MoreVertical size={24} />
              {isPopoverOpen && (
                <div className={styles.popoverMenu}>
                  <div className={styles.popoverItem}>
                    삭제
                  </div>
                </div>
              )}
            </div>
          </div>

          {/* Chat Body */}
          <div className={styles.chatBody}>
            {activeRoomId === '1001' ? (
              <>
                <ChatBubble variant="received">
                  안녕하세요! 그랜드 호텔입니다. 무엇을 도와드릴까요?
                </ChatBubble>
                <ChatBubble variant="sent">
                  1001호인데, 근처 맛집 추천해주실 수 있나요?
                </ChatBubble>
                <ChatBubble variant="received">
                  네, 고객님! 호텔 근처 긴자 지구의 "스시 미즈타니"와 미슐랭 라멘 맛집을 추천해 드립니다. 상세 지도를 원하시면 객실 내 태블릿으로 전송해 드릴까요?
                </ChatBubble>
              </>
            ) : (
              <div style={{ textAlign: 'center', color: 'var(--color-gray-400)', marginTop: '40px' }}>
                대화 기록이 없습니다.
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
