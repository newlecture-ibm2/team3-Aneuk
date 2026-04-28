'use client';

import { useEffect, useRef, useCallback } from 'react';
import { Client, IMessage } from '@stomp/stompjs';

/**
 * WebSocket STOMP 연결/재연결/구독 관리 훅
 *
 * Co-location 규칙: 앱 전체 공유 → app/ 루트에 위치
 *
 * 사용 예시:
 *   const { subscribe, isConnected } = useWebSocket();
 *
 *   useEffect(() => {
 *     const unsubscribe = subscribe('/topic/room/302', (data) => {
 *       console.log('실시간 메시지:', data);
 *     });
 *     return () => unsubscribe();
 *   }, [subscribe]);
 */

interface UseWebSocketReturn {
  subscribe: (destination: string, callback: (data: unknown) => void) => () => void;
  isConnected: boolean;
}

// WebSocket 서버 URL (개발: 백엔드 직접, 프로덕션: Nginx 프록시)
const WS_URL =
  process.env.NODE_ENV === 'production'
    ? `ws://${typeof window !== 'undefined' ? window.location.host : ''}/ws`
    : 'ws://localhost:8080/ws';

// 재연결 지수 백오프 설정
const RECONNECT_DELAYS = [1000, 2000, 4000, 8000, 16000, 30000]; // ms

export function useWebSocket(): UseWebSocketReturn {
  const clientRef = useRef<Client | null>(null);
  const isConnectedRef = useRef(false);
  const reconnectAttemptRef = useRef(0);
  const subscriptionsRef = useRef<Map<string, { callback: (data: unknown) => void }>>(new Map());

  // STOMP 클라이언트 초기화 및 연결
  useEffect(() => {
    const client = new Client({
      brokerURL: WS_URL,
      
      // 연결 성공
      onConnect: () => {
        console.log('[WS] ✅ STOMP 연결 성공');
        isConnectedRef.current = true;
        reconnectAttemptRef.current = 0;

        // 기존 구독 복원 (재연결 시)
        subscriptionsRef.current.forEach((sub, destination) => {
          client.subscribe(destination, (message: IMessage) => {
            try {
              const data = JSON.parse(message.body);
              sub.callback(data);
            } catch {
              sub.callback(message.body);
            }
          });
          console.log(`[WS] 🔄 구독 복원: ${destination}`);
        });
      },

      // 연결 해제
      onDisconnect: () => {
        console.log('[WS] ❌ STOMP 연결 해제');
        isConnectedRef.current = false;
      },

      // STOMP 에러
      onStompError: (frame) => {
        console.error('[WS] STOMP 에러:', frame.headers['message']);
      },

      // WebSocket 에러 (자동 재연결 트리거)
      onWebSocketError: () => {
        console.warn('[WS] WebSocket 에러 발생, 재연결 시도...');
      },

      // 재연결 지수 백오프
      reconnectDelay: () => {
        const attempt = Math.min(reconnectAttemptRef.current, RECONNECT_DELAYS.length - 1);
        const delay = RECONNECT_DELAYS[attempt];
        reconnectAttemptRef.current += 1;
        console.log(`[WS] 🔁 재연결 시도 #${reconnectAttemptRef.current} (${delay}ms 후)`);
        return delay;
      },
    });

    client.activate();
    clientRef.current = client;

    // 클린업
    return () => {
      if (client.active) {
        client.deactivate();
        console.log('[WS] 🛑 클라이언트 비활성화');
      }
    };
  }, []);

  // 채널 구독 함수
  const subscribe = useCallback(
    (destination: string, callback: (data: unknown) => void): (() => void) => {
      // 구독 정보 저장 (재연결 시 복원용)
      subscriptionsRef.current.set(destination, { callback });

      // 이미 연결된 상태면 즉시 구독
      const client = clientRef.current;
      let stompSubscription: { unsubscribe: () => void } | null = null;

      if (client?.connected) {
        stompSubscription = client.subscribe(destination, (message: IMessage) => {
          try {
            const data = JSON.parse(message.body);
            callback(data);
          } catch {
            callback(message.body);
          }
        });
        console.log(`[WS] 📡 구독 시작: ${destination}`);
      }

      // 구독 해제 함수 반환
      return () => {
        stompSubscription?.unsubscribe();
        subscriptionsRef.current.delete(destination);
        console.log(`[WS] 📡 구독 해제: ${destination}`);
      };
    },
    []
  );

  return {
    subscribe,
    isConnected: isConnectedRef.current,
  };
}
