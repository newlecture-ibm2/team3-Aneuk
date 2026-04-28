'use client';

import React, { useState } from 'react';
import styles from './FilterButton.module.css';
import { FilterIcon } from '@/components/icons';
import PopoverMenu, { PopoverMenuItem } from '@/components/ui/PopoverMenu/PopoverMenu';

export interface FilterButtonProps {
  filterOptions: PopoverMenuItem[];
  selectedFilter?: string;
  onFilterSelect: (value: string) => void;
  className?: string;
}

export default function FilterButton({
  filterOptions,
  selectedFilter,
  onFilterSelect,
  className = ''
}: FilterButtonProps) {
  const [isOpen, setIsOpen] = useState(false);

  return (
    <div className={`${styles.container} ${className}`.trim()}>
      <button 
        className={`${styles.button} ${isOpen ? styles.active : ''}`}
        onMouseDown={(e) => {
          e.stopPropagation();
          setIsOpen(!isOpen);
        }}
        onClick={(e) => e.stopPropagation()}
        aria-label="필터"
      >
        <FilterIcon size={20} />
      </button>

      {isOpen && (
        <PopoverMenu
          items={filterOptions}
          selectedValue={selectedFilter}
          onSelect={(value) => {
            onFilterSelect(value);
            setIsOpen(false);
          }}
          onClose={() => setIsOpen(false)}
          width="max-content"
          style={{ 
            top: 'calc(100% + var(--space-8))', 
            right: 0, 
            left: 'auto',
            minWidth: '160px'
          }}
        />
      )}
    </div>
  );
}
