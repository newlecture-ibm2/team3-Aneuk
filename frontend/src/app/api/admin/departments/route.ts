import { NextResponse } from 'next/server';

const BACKEND_URL = process.env.BACKEND_URL || 'http://localhost:8080';

/** GET /api/admin/departments → 백엔드 GET /admin/departments */
export async function GET() {
  const res = await fetch(`${BACKEND_URL}/admin/departments`, {
    headers: { 'Content-Type': 'application/json' },
  });

  const data = await res.json();
  return NextResponse.json(data, { status: res.status });
}
