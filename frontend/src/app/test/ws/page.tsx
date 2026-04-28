'use client';

import { useEffect, useState } from 'react';
import { useWebSocket } from '@/app/useWebSocket';

interface WsMessage {
  channel: string;
  data: unknown;
  receivedAt: string;
}

export default function WebSocketTestPage() {
  const { subscribe, isConnected } = useWebSocket();
  const [messages, setMessages] = useState<WsMessage[]>([]);
  const [channels, setChannels] = useState({
    room: true,
    dept: true,
    admin: true,
  });

  // 채널 구독
  useEffect(() => {
    const unsubscribes: (() => void)[] = [];

    if (channels.room) {
      unsubscribes.push(
        subscribe('/topic/room/302', (data) => {
          setMessages((prev) => [
            { channel: '🚪 room/302', data, receivedAt: new Date().toLocaleTimeString() },
            ...prev,
          ]);
        })
      );
    }

    if (channels.dept) {
      unsubscribes.push(
        subscribe('/topic/dept/HK', (data) => {
          setMessages((prev) => [
            { channel: '🏢 dept/HK', data, receivedAt: new Date().toLocaleTimeString() },
            ...prev,
          ]);
        })
      );
    }

    if (channels.admin) {
      unsubscribes.push(
        subscribe('/topic/admin', (data) => {
          setMessages((prev) => [
            { channel: '👑 admin', data, receivedAt: new Date().toLocaleTimeString() },
            ...prev,
          ]);
        })
      );
    }

    return () => unsubscribes.forEach((unsub) => unsub());
  }, [subscribe, channels]);

  // curl 명령어 복사
  const curlCommands = [
    {
      label: '🚪 Room 302',
      cmd: `curl -X POST http://localhost:8080/test/ws/room/302 -H "Content-Type: application/json" -d '{"type":"AI_RESPONSE","message":"수건 2장 보내드리겠습니다"}'`,
    },
    {
      label: '🏢 Dept HK',
      cmd: `curl -X POST http://localhost:8080/test/ws/dept/HK -H "Content-Type: application/json" -d '{"type":"NEW_REQUEST","summary":"302호 수건 요청"}'`,
    },
    {
      label: '👑 Admin',
      cmd: `curl -X POST http://localhost:8080/test/ws/admin -H "Content-Type: application/json" -d '{"type":"ESCALATED","reason":"긴급 에스컬레이션"}'`,
    },
  ];

  const copyToClipboard = (text: string) => {
    navigator.clipboard.writeText(text);
  };

  return (
    <div style={{ fontFamily: "'Inter', sans-serif", background: '#0f0f1a', color: '#e0e0e0', minHeight: '100vh', padding: '32px' }}>
      <h1 style={{ fontSize: '24px', marginBottom: '8px', background: 'linear-gradient(135deg, #6ee7b7, #3b82f6)', WebkitBackgroundClip: 'text', WebkitTextFillColor: 'transparent' }}>
        🔌 WebSocket 테스트
      </h1>
      <p style={{ color: '#888', fontSize: '14px', marginBottom: '32px' }}>STOMP 실시간 통신 검증 페이지</p>

      {/* 연결 상태 */}
      <div style={{ display: 'flex', gap: '16px', marginBottom: '24px', flexWrap: 'wrap' }}>
        <div style={{
          padding: '12px 20px', borderRadius: '8px',
          background: isConnected ? 'rgba(110, 231, 183, 0.1)' : 'rgba(248, 113, 113, 0.1)',
          border: `1px solid ${isConnected ? '#6ee7b7' : '#f87171'}`,
          fontSize: '14px', fontWeight: 600,
        }}>
          {isConnected ? '✅ STOMP 연결됨' : '❌ 연결 안 됨 (백엔드 실행 확인)'}
        </div>
        <button
          onClick={() => setMessages([])}
          style={{
            padding: '12px 20px', borderRadius: '8px', background: '#1a1a2e',
            border: '1px solid #2a2a4a', color: '#e0e0e0', cursor: 'pointer', fontSize: '14px',
          }}
        >
          🗑️ 메시지 초기화
        </button>
      </div>

      {/* 구독 채널 토글 */}
      <div style={{ marginBottom: '24px' }}>
        <h2 style={{ fontSize: '16px', color: '#6ee7b7', marginBottom: '12px' }}>📡 구독 채널</h2>
        <div style={{ display: 'flex', gap: '12px', flexWrap: 'wrap' }}>
          {(['room', 'dept', 'admin'] as const).map((ch) => (
            <label key={ch} style={{
              padding: '8px 16px', borderRadius: '6px', cursor: 'pointer', fontSize: '13px',
              background: channels[ch] ? 'rgba(59, 130, 246, 0.2)' : '#1a1a2e',
              border: `1px solid ${channels[ch] ? '#3b82f6' : '#2a2a4a'}`,
              display: 'flex', alignItems: 'center', gap: '6px',
            }}>
              <input
                type="checkbox"
                checked={channels[ch]}
                onChange={() => setChannels((prev) => ({ ...prev, [ch]: !prev[ch] }))}
              />
              {ch === 'room' ? '🚪 /topic/room/302' : ch === 'dept' ? '🏢 /topic/dept/HK' : '👑 /topic/admin'}
            </label>
          ))}
        </div>
      </div>

      {/* curl 명령어 */}
      <div style={{ marginBottom: '24px' }}>
        <h2 style={{ fontSize: '16px', color: '#fbbf24', marginBottom: '12px' }}>⚡ 테스트 명령어 (터미널에서 실행)</h2>
        <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
          {curlCommands.map((c, i) => (
            <div key={i} style={{
              background: '#1a1a2e', border: '1px solid #2a2a4a', borderRadius: '8px',
              padding: '12px 16px', display: 'flex', justifyContent: 'space-between', alignItems: 'center', gap: '12px',
            }}>
              <code style={{ fontSize: '12px', color: '#a0a0a0', wordBreak: 'break-all', flex: 1 }}>{c.cmd}</code>
              <button
                onClick={() => copyToClipboard(c.cmd)}
                style={{
                  padding: '6px 12px', borderRadius: '4px', background: '#3b82f6',
                  border: 'none', color: '#fff', cursor: 'pointer', fontSize: '12px', whiteSpace: 'nowrap',
                }}
              >
                {c.label} 복사
              </button>
            </div>
          ))}
        </div>
      </div>

      {/* 수신 메시지 로그 */}
      <div>
        <h2 style={{ fontSize: '16px', color: '#a78bfa', marginBottom: '12px' }}>
          📨 수신 메시지 ({messages.length}건)
        </h2>
        {messages.length === 0 ? (
          <div style={{
            background: '#1a1a2e', border: '1px solid #2a2a4a', borderRadius: '8px',
            padding: '40px', textAlign: 'center', color: '#666', fontSize: '14px',
          }}>
            아직 수신된 메시지가 없습니다. 위 curl 명령어를 터미널에서 실행해 보세요!
          </div>
        ) : (
          <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', maxHeight: '400px', overflowY: 'auto' }}>
            {messages.map((msg, i) => (
              <div key={i} style={{
                background: '#1a1a2e', border: '1px solid #2a2a4a', borderRadius: '8px', padding: '12px 16px',
              }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '6px' }}>
                  <span style={{ fontWeight: 600, fontSize: '13px' }}>{msg.channel}</span>
                  <span style={{ color: '#666', fontSize: '11px' }}>{msg.receivedAt}</span>
                </div>
                <pre style={{
                  margin: 0, fontSize: '12px', color: '#6ee7b7', background: '#0f0f1a',
                  padding: '8px', borderRadius: '4px', overflowX: 'auto',
                }}>
                  {JSON.stringify(msg.data, null, 2)}
                </pre>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
