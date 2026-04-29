'use client';

import React from 'react';
import DashboardLayout from '@/components/layout/DashboardLayout';
import SettingsPage from './_components/SettingsPage/SettingsPage';

export default function StaffManagementPage() {
  return (
    <DashboardLayout role="admin" title="직원 관리">
      <SettingsPage />
    </DashboardLayout>
  );
}
