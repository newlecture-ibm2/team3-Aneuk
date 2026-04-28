import React from 'react';

export default function StatusTag({ status, ...props }: any) {
  return (
    <span 
      style={{ display: 'inline-block', padding: '4px 8px', borderRadius: '4px', fontSize: '12px', background: 'var(--color-surface)', ...props.style }}
      {...props}
    >
      {status || '[StatusTag]'}
    </span>
  );
}
