'use client';

import { useState, useCallback } from 'react';

interface PmsRoom {
  number: string;
  type: string;
  occupied: boolean;
  guestName: string | null;
}

export default function useRooms() {
  const [rooms, setRooms] = useState<PmsRoom[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchRooms = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const res = await fetch('/api/pms/rooms');
      if (!res.ok) throw new Error('객실 목록 조회 실패');
      const data = await res.json();
      setRooms(data);
    } catch (err: any) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }, []);

  return { rooms, loading, error, fetchRooms };
}
