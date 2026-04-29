const API_BASE_URL = '/api';

interface FetchOptions extends RequestInit {
  params?: Record<string, string>;
}

interface ApiError {
  status: number;
  message: string;
}

/**
 * 공통 Fetch Wrapper
 * - BFF 프록시(/api)를 통해 백엔드와 통신
 * - 에러 핸들링 통합
 */
export async function client<T>(
  endpoint: string,
  options: FetchOptions = {},
): Promise<T> {
  const { params, headers: customHeaders, ...restOptions } = options;

  let url = `${API_BASE_URL}${endpoint}`;

  if (params) {
    const searchParams = new URLSearchParams(params);
    url += `?${searchParams.toString()}`;
  }

  const headers: HeadersInit = {
    'Content-Type': 'application/json',
    ...customHeaders,
  };

  const response = await fetch(url, {
    headers,
    credentials: 'include',
    ...restOptions,
  });

  if (!response.ok) {
    const error: ApiError = {
      status: response.status,
      message: response.statusText,
    };

    try {
      const body = await response.json();
      error.message = body.message || error.message;
    } catch {
      // JSON 파싱 실패 시 기본 메시지 사용
    }

    throw error;
  }

  // 204 No Content
  if (response.status === 204) {
    return undefined as T;
  }

  return response.json();
}
