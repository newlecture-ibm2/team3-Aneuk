import React from 'react';
import styles from './Pill.module.css';

export interface PillProps {
  options: string[];
  selectedOption?: string;
  onSelect: (option: string) => void;
}

export default function Pill({ options, selectedOption, onSelect }: PillProps) {
  if (!options || options.length === 0) return null;

  return (
    <div className={styles.container}>
      {options.map((option, index) => (
        <button 
          key={index} 
          className={`${styles.button} ${selectedOption === option ? styles.buttonSelected : ''}`}
          onClick={() => onSelect(option)}
          role="tab"
          aria-selected={selectedOption === option}
        >
          {option}
        </button>
      ))}
    </div>
  );
}
