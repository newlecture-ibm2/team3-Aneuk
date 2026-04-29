import { NextRequest, NextResponse } from 'next/server';

const BACKEND_URL = process.env.BACKEND_URL || 'http://localhost:8080';

/** PUT /api/admin/roles/:id → 백엔드 PUT /admin/roles/:id */
export async function PUT(
  request: NextRequest,
  { params }: { params: Promise<{ id: string }> }
) {
  const { id } = await params;
  const body = await request.json();
  const res = await fetch(`${BACKEND_URL}/admin/roles/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(body),
  });

  const data = await res.json();
  return NextResponse.json(data, { status: res.status });
}

/** DELETE /api/admin/roles/:id → 백엔드 DELETE /admin/roles/:id */
export async function DELETE(
  request: NextRequest,
  { params }: { params: Promise<{ id: string }> }
) {
  const { id } = await params;
  const res = await fetch(`${BACKEND_URL}/admin/roles/${id}`, {
    method: 'DELETE',
  });

  if (res.status === 204) {
    return new NextResponse(null, { status: 204 });
  }
  const data = await res.json();
  return NextResponse.json(data, { status: res.status });
}
