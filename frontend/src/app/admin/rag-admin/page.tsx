'use client';

import React, { useState } from 'react';
import InputField from '@/components/ui/Inputfield/InputField';
import FilterButton from '@/components/ui/FilterButton/FilterButton';
import KnowledgeItem from '@/components/ui/Knowledge/KnowledgeItem';
import KnowledgeModal from '@/components/ui/Knowledge/KnowledgeModal';
import KnowledgeEditModal from '@/components/ui/Knowledge/KnowledgeEditModal';
import Button from '@/components/ui/Button/Button';
import styles from './page.module.css';

const dummyKnowledges = [
  {
    id: 1,
    category: '체크인/아웃',
    title: '체크아웃 시간 연장(Late Checkout)',
    description: '오후 2시까지 연장 가능하며, 시간당 2,500엔의 추가 비용이 발생합니다. 멤버십 등급에 따라 무료 연장이 가능할 수 있으니 프런트에 문의 바랍니다.',
    updatedAt: '2026-04-15'
  },
  {
    id: 2,
    category: '부대시설',
    title: '천연 온천(대욕장) 이용 안내',
    description: '호텔 12층에 위치한 천연 온천은 오후 3시부터 익일 오전 10시까지 이용 가능합니다. 문신이 있으신 고객님은 제공해 드리는 커버 씰을 부착해 주시기 바랍니다.',
    updatedAt: '2026-04-10'
  },
  {
    id: 3,
    category: '주변 관광',
    title: '도쿄 타워(Tokyo Tower) 방문 가이드',
    description: '호텔 서쪽 출구에서 도보로 약 10분 거리에 위치해 있습니다. 메인 데크 예매권은 호텔 컨시어지 데스크에서 할인된 가격으로 구매 가능합니다.',
    updatedAt: '2026-04-18'
  }
];

export default function RagAdminPage() {
  const [searchValue, setSearchValue] = useState('');
  const [selectedKnowledge, setSelectedKnowledge] = useState<any>(null);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [isCreatingNew, setIsCreatingNew] = useState(false);

  return (
    <div className={styles.container}>
      {/* Header Section */}
      <div className={styles.header}>
        <div className={styles.headerLeft}>
          <h1 className={styles.title}>지식 라이브러리</h1>
        </div>
        <div className={styles.headerActions}>
          <Button 
            variant="primary" 
            onClick={() => {
              setIsCreatingNew(true);
              setIsEditModalOpen(true);
            }}
          >
            + 지식 정보 추가
          </Button>
        </div>
      </div>

      {/* Content Section */}
      <div className={styles.contentSection}>
        <div className={styles.filterSection}>
          <div style={{ width: '300px' }}>
            <InputField 
              variant="search" 
              placeholder="검색어를 입력하세요..." 
              value={searchValue}
              onChange={(e) => setSearchValue(e.target.value)}
            />
          </div>
          <FilterButton 
            filterOptions={[
              { label: '전체', value: 'all' }, 
              { label: '최신순', value: 'latest' }
            ]}
            selectedFilter="all"
            onFilterSelect={() => {}}
          />
        </div>

        <div className={styles.listContainer}>
          {dummyKnowledges.map((item) => (
            <KnowledgeItem
              key={item.id}
              category={item.category}
              title={item.title}
              description={item.description}
              updatedAt={item.updatedAt}
              onClick={() => setSelectedKnowledge(item)}
            />
          ))}
        </div>
      </div>

      {/* View Modal */}
      {selectedKnowledge && !isEditModalOpen && (
        <KnowledgeModal
          isOpen={!!selectedKnowledge}
          onClose={() => setSelectedKnowledge(null)}
          category={selectedKnowledge.category}
          title={selectedKnowledge.title}
          description={selectedKnowledge.description}
          updatedAt={selectedKnowledge.updatedAt}
          onEdit={() => setIsEditModalOpen(true)}
        />
      )}

      {/* Edit / Create Modal */}
      {isEditModalOpen && (
        <KnowledgeEditModal
          isOpen={isEditModalOpen}
          onClose={() => {
            setIsEditModalOpen(false);
            if (isCreatingNew) {
              setIsCreatingNew(false);
            }
          }}
          initialCategory={isCreatingNew ? '' : selectedKnowledge?.category}
          initialTitle={isCreatingNew ? '' : selectedKnowledge?.title}
          initialDescription={isCreatingNew ? '' : selectedKnowledge?.description}
          onSave={(data) => {
            console.log('Saved knowledge:', data);
            setIsEditModalOpen(false);
            setIsCreatingNew(false);
            setSelectedKnowledge(null);
          }}
        />
      )}
    </div>
  );
}
