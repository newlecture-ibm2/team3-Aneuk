'use client';

import { useState, useCallback } from 'react';

interface Guest {
  id: number;
  roomNumber: string;
  name: string;
  phone: string | null;
  checkinDate: string;
  checkoutDate: string;
}

interface CheckInPayload {
  roomNumber: string;
  name: string;
  phone: string;
  checkoutDate: string;
}

export default function useGuests() {
  const [guests, setGuests] = useState<Guest[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchGuests = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const res = await fetch('/api/pms/guests');
      if (!res.ok) throw new Error('투숙객 목록 조회 실패');
      const data = await res.json();
      setGuests(data);
    } catch (err: any) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }, []);

  const checkIn = useCallback(async (payload: CheckInPayload) => {
    setError(null);
    try {
      const res = await fetch('/api/pms/guests', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      });
      if (!res.ok) {
        const errData = await res.json().catch(() => ({}));
        throw new Error(errData.message || '체크인 실패');
      }
      await fetchGuests();
    } catch (err: any) {
      setError(err.message);
      throw err;
    }
  }, [fetchGuests]);

  const checkOut = useCallback(async (guestId: number) => {
    setError(null);
    try {
      const res = await fetch(`/api/pms/guests/${guestId}`, { method: 'DELETE' });
      if (!res.ok) {
        const errData = await res.json().catch(() => ({}));
        throw new Error(errData.message || '체크아웃 실패');
      }
      await fetchGuests();
    } catch (err: any) {
      setError(err.message);
      throw err;
    }
  }, [fetchGuests]);

  return { guests, loading, error, fetchGuests, checkIn, checkOut };
}
