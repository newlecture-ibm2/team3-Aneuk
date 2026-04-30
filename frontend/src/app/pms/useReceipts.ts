import { useState, useCallback } from 'react';

interface Receipt {
  id: number;
  roomNo: string;
  menuId: number;
  menuName: string;
  quantity: number;
  totalPrice: number;
  status: string;
  createdAt: string;
}

export default function useReceipts() {
  const [receipts, setReceipts] = useState<Receipt[]>([]);
  const [loading, setLoading] = useState(false);

  const fetchUnpaidReceipts = useCallback(async (roomNo: string) => {
    setLoading(true);
    try {
      const res = await fetch(`/api/pms/receipts?roomNo=${roomNo}&unpaid=true`);
      if (res.ok) {
        const data = await res.json();
        setReceipts(data);
        return data as Receipt[];
      }
      setReceipts([]);
      return [];
    } catch {
      setReceipts([]);
      return [];
    } finally {
      setLoading(false);
    }
  }, []);

  const payAll = useCallback(async (roomNo: string) => {
    const res = await fetch(`/api/pms/receipts/pay-all?roomNo=${roomNo}`, {
      method: 'PATCH',
    });
    if (!res.ok) throw new Error('결제 실패');
  }, []);

  return { receipts, loading, fetchUnpaidReceipts, payAll };
}
