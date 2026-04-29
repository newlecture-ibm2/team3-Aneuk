import { NextRequest, NextResponse } from 'next/server';

const BACKEND_URL = process.env.BACKEND_URL || 'http://localhost:8080';

/** GET /api/admin/roles → 백엔드 GET /admin/roles */
export async function GET() {
  const res = await fetch(`${BACKEND_URL}/admin/roles`, {
    headers: { 'Content-Type': 'application/json' },
  });

  const data = await res.json();
  return NextResponse.json(data, { status: res.status });
}

/** POST /api/admin/roles → 백엔드 POST /admin/roles */
export async function POST(request: NextRequest) {
  const body = await request.json();
  const res = await fetch(`${BACKEND_URL}/admin/roles`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(body),
  });

  const data = await res.json();
  return NextResponse.json(data, { status: res.status });
}
