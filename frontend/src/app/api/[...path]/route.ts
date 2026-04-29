import { NextRequest, NextResponse } from 'next/server';

const BACKEND_URL = process.env.BACKEND_URL || 'http://localhost:8080';

async function handleProxy(req: NextRequest, { params }: { params: Promise<{ path: string[] }> }) {
  try {
    const { path: segments } = await params;
    const path = segments.join('/');
    const url = new URL(`${BACKEND_URL}/${path}`);
    url.search = req.nextUrl.search;

    const options: RequestInit = {
      method: req.method,
      headers: {
        'Content-Type': req.headers.get('Content-Type') || 'application/json',
      },
    };

    if (req.method !== 'GET' && req.method !== 'HEAD') {
      const body = await req.text();
      if (body) {
        options.body = body;
      }
    }

    const response = await fetch(url.toString(), options);
    
    let data;
    const contentType = response.headers.get('content-type');
    if (contentType && contentType.includes('application/json')) {
      data = await response.json();
    } else {
      data = await response.text();
    }

    return NextResponse.json(data, {
      status: response.status,
    });
  } catch (error) {
    console.error('BFF Proxy Error:', error);
    return NextResponse.json({ error: 'Internal Server Error' }, { status: 500 });
  }
}

export async function GET(req: NextRequest, ctx: any) {
  return handleProxy(req, ctx);
}

export async function POST(req: NextRequest, ctx: any) {
  return handleProxy(req, ctx);
}

export async function PUT(req: NextRequest, ctx: any) {
  return handleProxy(req, ctx);
}

export async function PATCH(req: NextRequest, ctx: any) {
  return handleProxy(req, ctx);
}

export async function DELETE(req: NextRequest, ctx: any) {
  return handleProxy(req, ctx);
}
