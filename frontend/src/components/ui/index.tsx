import React from 'react';

export const Button = ({ children, variant, style, ...props }: any) => (
  <button 
    style={{ ...style, cursor: 'pointer', padding: '8px 16px', borderRadius: '4px', border: 'none', background: variant === 'danger' ? 'var(--color-error)' : 'var(--color-primary)', color: 'white' }} 
    {...props}
  >
    {children}
  </button>
);

export const Checkbox = ({ label, ...props }: any) => (
  <label style={{ display: 'flex', alignItems: 'center', gap: '8px', cursor: 'pointer' }}>
    <input type="checkbox" {...props} />
    {label && <span>{label}</span>}
  </label>
);

export const Radio = ({ label, ...props }: any) => (
  <label style={{ display: 'flex', alignItems: 'center', gap: '8px', cursor: 'pointer' }}>
    <input type="radio" {...props} />
    {label && <span>{label}</span>}
  </label>
);

export const Tag = ({ children, variant, style, ...props }: any) => (
  <span 
    style={{ ...style, display: 'inline-block', padding: '4px 8px', borderRadius: '4px', fontSize: '12px', background: 'var(--color-surface)' }}
    {...props}
  >
    {children}
  </span>
);

export const InputField = ({ label, style, ...props }: any) => (
  <div style={{ display: 'flex', flexDirection: 'column', gap: '4px' }}>
    {label && <label>{label}</label>}
    <input 
      style={{ ...style, padding: '8px', border: '1px solid var(--color-border)', borderRadius: '4px', width: '100%' }}
      {...props} 
    />
  </div>
);

export const Input = InputField;

export const TextareaField = ({ label, style, ...props }: any) => (
  <div style={{ display: 'flex', flexDirection: 'column', gap: '4px' }}>
    {label && <label>{label}</label>}
    <textarea 
      style={{ ...style, padding: '8px', border: '1px solid var(--color-border)', borderRadius: '4px', width: '100%', minHeight: '80px' }}
      {...props} 
    />
  </div>
);

export const Select = ({ children, style, ...props }: any) => (
  <select 
    style={{ ...style, padding: '8px', border: '1px solid var(--color-border)', borderRadius: '4px', width: '100%' }}
    {...props}
  >
    {children}
  </select>
);


export const Tabs = ({ options, activeValue, onChange }: any) => (
  <div style={{ display: 'flex', gap: '8px' }}>
    {options?.map((opt: any) => (
      <button 
        key={opt.value} 
        onClick={() => onChange?.(opt.value)}
        style={{ 
          padding: '4px 12px', 
          background: activeValue === opt.value ? 'var(--color-primary)' : 'var(--color-surface)',
          color: activeValue === opt.value ? 'white' : 'black',
          border: 'none',
          borderRadius: '4px',
          cursor: 'pointer'
        }}
      >
        {opt.label}
      </button>
    ))}
  </div>
);
