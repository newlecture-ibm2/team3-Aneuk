import React, { useState, useEffect } from 'react';
import { Table, TableHeader, TableRow, TableCell } from '@/components/ui/Table/Table';
import Button from '@/components/ui/Button/Button';
import InputField from '@/components/ui/Inputfield/InputField';
import { useRoleManagement, Role } from './useRoleManagement';
import RoleFormModal from '../RoleFormModal/RoleFormModal';
import { ConfirmModal } from '@/components/ui/Modal';
import { useUiStore } from '@/stores/useUiStore';

export default function RoleTab() {
  const { roles, loading, error, fetchRoles, createRole, updateRole, deleteRole } = useRoleManagement();
  const { showToast } = useUiStore();

  const [searchTerm, setSearchTerm] = useState('');
  const [isFormModalOpen, setIsFormModalOpen] = useState(false);
  const [editingRole, setEditingRole] = useState<Role | undefined>(undefined);
  
  const [isConfirmModalOpen, setIsConfirmModalOpen] = useState(false);
  const [roleToDelete, setRoleToDelete] = useState<Role | null>(null);

  useEffect(() => {
    fetchRoles();
  }, [fetchRoles]);

  useEffect(() => {
    if (error) {
      showToast(error, 'error');
    }
  }, [error, showToast]);

  const filteredRoles = roles.filter((role) =>
    role.name.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const handleAddClick = () => {
    setEditingRole(undefined);
    setIsFormModalOpen(true);
  };

  const handleEditClick = (role: Role) => {
    setEditingRole(role);
    setIsFormModalOpen(true);
  };

  const handleDeleteClick = (role: Role) => {
    setRoleToDelete(role);
    setIsConfirmModalOpen(true);
  };

  const handleSaveRole = async (name: string) => {
    if (editingRole) {
      await updateRole(editingRole.id, { name });
      showToast('역할이 수정되었습니다.', 'success');
    } else {
      await createRole({ name });
      showToast('역할이 추가되었습니다.', 'success');
    }
  };

  const handleConfirmDelete = async () => {
    if (roleToDelete) {
      try {
        await deleteRole(roleToDelete.id);
        showToast('역할이 삭제되었습니다.', 'success');
      } catch (err) {
        // 에러는 useRoleManagement에서 처리됨
      }
    }
    setIsConfirmModalOpen(false);
    setRoleToDelete(null);
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: 'var(--space-24)' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <div style={{ width: '300px' }}>
          <InputField
            variant="search"
            placeholder="역할명 검색..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>
        <Button variant="primary" onClick={handleAddClick}>
          + 역할 추가
        </Button>
      </div>

      {loading && roles.length === 0 ? (
        <div style={{ padding: 'var(--space-24)', textAlign: 'center', color: 'var(--color-gray-500)' }}>
          로딩 중...
        </div>
      ) : (
        <Table columns="1fr 100px">
          <TableHeader>
            <TableCell>역할명</TableCell>
            <TableCell>액션</TableCell>
          </TableHeader>
          {filteredRoles.length > 0 ? (
            filteredRoles.map((role) => (
              <TableRow key={role.id}>
                <TableCell>{role.name}</TableCell>
                <TableCell>
                  <div style={{ display: 'flex', gap: 'var(--space-8)' }}>
                    <Button variant="secondary" size="small" onClick={() => handleEditClick(role)}>
                      수정
                    </Button>
                    <Button variant="danger" size="small" onClick={() => handleDeleteClick(role)}>
                      삭제
                    </Button>
                  </div>
                </TableCell>
              </TableRow>
            ))
          ) : (
            <TableRow>
              <TableCell>
                <div style={{ textAlign: 'center', color: 'var(--color-gray-500)', padding: 'var(--space-24)' }}>
                  등록된 역할이 없습니다.
                </div>
              </TableCell>
              <TableCell></TableCell>
            </TableRow>
          )}
        </Table>
      )}

      <RoleFormModal
        isOpen={isFormModalOpen}
        onClose={() => setIsFormModalOpen(false)}
        onSave={handleSaveRole}
        initialData={editingRole}
      />

      <ConfirmModal
        isOpen={isConfirmModalOpen}
        onClose={() => setIsConfirmModalOpen(false)}
        onConfirm={handleConfirmDelete}
        title="역할 삭제"
        subtitle={`'${roleToDelete?.name}' 역할을 삭제하시겠습니까? 이 작업은 되돌릴 수 없습니다.`}
        confirmText="삭제"
        cancelText="취소"
      />
    </div>
  );
}
