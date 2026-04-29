import { NextRequest, NextResponse } from 'next/server';

const BACKEND_URL = 'http://localhost:8080';

async function handleProxy(req: NextRequest, context: { params: Promise<{ path: string[] }> }) {
  try {
    const params = await context.params;
    const path = params.path.join('/');
    const url = new URL(req.url);
    const searchParams = url.search;

    const backendUrl = `${BACKEND_URL}/${path}${searchParams}`;

    // Get the request body if it's not a GET/HEAD request
    let body = null;
    if (req.method !== 'GET' && req.method !== 'HEAD') {
      body = await req.text();
    }

    // Forward the headers
    const headers = new Headers();
    req.headers.forEach((value, key) => {
      // Avoid forwarding host and connection headers to prevent issues
      if (key.toLowerCase() !== 'host' && key.toLowerCase() !== 'connection') {
        headers.set(key, value);
      }
    });

    const response = await fetch(backendUrl, {
      method: req.method,
      headers,
      body: body || undefined,
      // @ts-ignore
      duplex: 'half',
    });

    // Read the response from the backend
    const responseBody = await response.text();

    // Create the response to send back to the client
    const responseHeaders = new Headers();
    response.headers.forEach((value, key) => {
      responseHeaders.set(key, value);
    });

    return new NextResponse(responseBody, {
      status: response.status,
      headers: responseHeaders,
    });
  } catch (error: any) {
    console.error('BFF Proxy Error:', error);
    return NextResponse.json({ error: 'Internal Server Error' }, { status: 500 });
  }
}

export const GET = handleProxy;
export const POST = handleProxy;
export const PUT = handleProxy;
export const PATCH = handleProxy;
export const DELETE = handleProxy;
