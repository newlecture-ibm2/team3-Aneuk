/**
 * [가이드라인 준수] 공통 API 응답 처리기
 * 규칙: fetch wrapper를 만들지 않고, 이 핸들러만 각 훅에서 호출하여 사용함.
 */
export async function handleResponse<T>(response: Response): Promise<T> {
  if (!response.ok) {
    let errorMessage = '알 수 없는 오류가 발생했습니다.';
    try {
      const errorData = await response.json();
      errorMessage = errorData.message || errorMessage;
    } catch {
      // JSON 파싱 실패 시 기본 메시지 유지
    }
    throw new Error(errorMessage);
  }

  // 204 No Content 처리
  if (response.status === 204) {
    return {} as T;
  }

  return response.json();
}
