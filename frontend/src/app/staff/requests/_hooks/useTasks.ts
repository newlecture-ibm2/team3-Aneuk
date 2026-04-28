'use client';

import { useEffect, useCallback, useState } from 'react';
import { useWebSocket } from '@/app/useWebSocket';

export interface StaffTask {
  id: number;
  status: 'PENDING' | 'ASSIGNED' | 'IN_PROGRESS' | 'COMPLETED' | 'CANCELLED';
  priority: 'LOW' | 'NORMAL' | 'HIGH' | 'URGENT';
  departmentId: string;
  summary: string;
  rawText: string;
  roomNumber: string;
  assignedStaffName: string | null;
  confidence: number | null;
  createdAt: string;
}

interface UseTasksReturn {
  tasks: StaffTask[];
  loading: boolean;
  error: string | null;
  refetch: () => void;
}

export function useTasks(): UseTasksReturn {
  const [tasks, setTasks] = useState<StaffTask[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const { subscribe } = useWebSocket();

  const fetchTasks = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const res = await fetch('/api/staff/requests');
      if (!res.ok) throw new Error(`${res.status}`);
      const data: StaffTask[] = await res.json();
      setTasks(data);
    } catch (err) {
      setError(err instanceof Error ? err.message : '요청 목록을 불러오지 못했습니다.');
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchTasks();
  }, [fetchTasks]);

  useEffect(() => {
    const unsubscribe = subscribe('/topic/admin', (data: unknown) => {
      const event = data as { type?: string; payload?: StaffTask };
      if (!event || !event.type) return;

      if (event.type === 'NEW_REQUEST' && event.payload) {
        setTasks((prev) => [event.payload!, ...prev]);
      } else if (event.type === 'STATUS_CHANGED' && event.payload) {
        setTasks((prev) =>
          prev.map((t) => (t.id === event.payload!.id ? event.payload! : t))
        );
      }
    });
    return () => unsubscribe();
  }, [subscribe]);

  return { tasks, loading, error, refetch: fetchTasks };
}
