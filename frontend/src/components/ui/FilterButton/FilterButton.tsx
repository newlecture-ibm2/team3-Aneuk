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
        <div className={styles.popoverContainer} onMouseDown={(e) => e.stopPropagation()}>
          <PopoverMenu
            items={filterOptions}
            selectedValue={selectedFilter}
            onSelect={(value) => {
              onFilterSelect(value);
              setIsOpen(false);
            }}
            onClose={() => setIsOpen(false)}
          />
        </div>
      )}
    </div>
  );
}
