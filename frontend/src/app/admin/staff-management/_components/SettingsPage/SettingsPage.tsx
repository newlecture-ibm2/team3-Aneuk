import React, { useState } from 'react';
import Tabs from '@/components/ui/Tab/Tabs';
import StaffTab from '../StaffTab/StaffTab';
import RoleTab from '../RoleTab/RoleTab';

export default function SettingsPage() {
  const [activeTab, setActiveTab] = useState('staff');

  const tabsOptions = [
    { label: '직원 관리', value: 'staff' },
    { label: '역할 관리', value: 'role' },
  ];

  return (
    <div style={{ padding: 'var(--space-32) var(--space-48)', display: 'flex', flexDirection: 'column', gap: 'var(--space-32)' }}>
      <div style={{ borderBottom: '1px solid var(--color-surface)' }}>
        <Tabs
          variant="line"
          options={tabsOptions}
          activeValue={activeTab}
          onChange={setActiveTab}
        />
      </div>

      <div style={{ minHeight: '500px' }}>
        {activeTab === 'staff' && <StaffTab />}
        {activeTab === 'role' && <RoleTab />}
      </div>
    </div>
  );
}
