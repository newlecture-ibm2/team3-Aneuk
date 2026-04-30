'use client';

import { useEffect, useState, useCallback, useRef } from 'react';
import { Client, IMessage } from '@stomp/stompjs';

interface WsMessage {
  channel: string;
  data: unknown;
  receivedAt: string;
}

// WebSocket 서버 URL (개발: 백엔드 직접, 프로덕션: Nginx 프록시)
const getWsUrl = () => {
  if (process.env.NODE_ENV === 'production') {
    const protocol = typeof window !== 'undefined' && window.location.protocol === 'https:' ? 'wss' : 'ws';
    const host = typeof window !== 'undefined' ? window.location.host : '';
    return `${protocol}://${host}/ws`;
  }
  return 'ws://localhost:8080/ws';
};

export default function WebSocketTestPage() {
  const [isManualConnected, setIsManualConnected] = useState(false);
  const [messages, setMessages] = useState<WsMessage[]>([]);
  const [dbRequests, setDbRequests] = useState<any[]>([]);
  const [channels, setChannels] = useState({
    room: true,
    dept: true,
    admin: true,
  });
  const clientRef = useRef<Client | null>(null);

  // 수동 연결
  const connect = useCallback(() => {
    if (clientRef.current?.active) return;

    const url = getWsUrl();
    const client = new Client({
      brokerURL: url,
      reconnectDelay: 2000,
      onConnect: () => {
        console.log('[WS Test] ✅ STOMP 연결 성공');
        setIsManualConnected(true);
      },
      onDisconnect: () => {
        console.log('[WS Test] ❌ STOMP 연결 해제');
        setIsManualConnected(false);
      },
      onWebSocketError: () => {
        console.warn('[WS Test] WebSocket 에러 발생');
      },
    });

    client.activate();
    clientRef.current = client;
  }, []);

  // 수동 연결 해제
  const disconnect = useCallback(() => {
    if (clientRef.current?.active) {
      clientRef.current.deactivate();
      clientRef.current = null;
      setIsManualConnected(false);
    }
  }, []);

  // 클린업
  useEffect(() => {
    return () => {
      clientRef.current?.deactivate();
    };
  }, []);

  // 채널 구독 (연결 후에만)
  useEffect(() => {
    const client = clientRef.current;
    if (!isManualConnected || !client?.connected) return;

    const subscriptions: { unsubscribe: () => void }[] = [];

    const addSub = (destination: string, label: string) => {
      const sub = client.subscribe(destination, (message: IMessage) => {
        try {
          const data = JSON.parse(message.body);
          setMessages((prev) => [
            { channel: label, data, receivedAt: new Date().toLocaleTimeString() },
            ...prev,
          ]);
        } catch {
          setMessages((prev) => [
            { channel: label, data: message.body, receivedAt: new Date().toLocaleTimeString() },
            ...prev,
          ]);
        }
      });
      subscriptions.push(sub);
    };

    if (channels.room) addSub('/topic/room/707', '🚪 room/707');
    if (channels.dept) addSub('/topic/dept/HK', '🏢 dept/HK');
    if (channels.admin) addSub('/topic/admin', '👑 admin');

    return () => subscriptions.forEach((sub) => sub.unsubscribe());
  }, [isManualConnected, channels]);

  // API 호출 테스트
  const simulateRequest = async (payload: any) => {
    try {
      const res = await fetch('/api/test/simulate-request', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      });
      const data = await res.text();
      alert(`✅ 성공: ${data}`);
    } catch (err) {
      alert(`❌ 실패: ${err}`);
    }
  };

  // 채팅 메시지 전송 (실제 AI 플로우 트리거)
  const sendChatMessage = async (message: string) => {
    try {
      const res = await fetch('/api/chat/707/messages', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ content: message, guestLanguage: 'ko' }),
      });
      const data = await res.json();
      alert(`✅ 메시지 전송 완료 (id: ${data.messageId})\nAI 응답이 WebSocket으로 도착합니다.`);
    } catch (err) {
      alert(`❌ 메시지 전송 실패: ${err}`);
    }
  };

  // 707호 DB 요청 목록 조회 (RQ-3)
  const fetchRequests = async () => {
    try {
      const res = await fetch('/api/chat/707/requests');
      const data = await res.json();
      setDbRequests(data);
    } catch (err) {
      alert(`조회 실패: ${err}`);
    }
  };

  // 직원용 요청 상태 변경 (RQ-4)
  const changeStatus = async (id: number, action: 'accept' | 'complete') => {
    try {
      const res = await fetch(`/api/staff/requests/${id}/${action}`, {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ staffId: 1 }), // 임의의 직원 ID
      });
      
      const data = await res.text();
      
      if (!res.ok) {
        throw new Error(data);
      }
      
      alert(`✅ 상태 변경 성공: ${data}`);
      fetchRequests(); // 상태 변경 후 목록 새로고침
    } catch (err) {
      alert(`❌ 상태 변경 실패: ${err}`);
    }
  };

  const chatSimulationButtons = [
    {
      label: '💬 "수건 2장 주세요"',
      desc: 'AI → HK 도메인 감지 → Request 자동 생성',
      message: '수건 2장 주세요'
    },
    {
      label: '💬 "에어컨이 안 돼요"',
      desc: 'AI → FACILITY 도메인 감지 → 긴급 요청',
      message: '에어컨이 안 돼요'
    },
    {
      label: '💬 "룸서비스 음식 주문"',
      desc: 'AI → FB 도메인 감지 → 일반 요청',
      message: '룸서비스 음식 주문할게요'
    },
    {
      label: '💬 "안녕하세요"',
      desc: 'AI → 단순 대화 (Request 생성 안 됨)',
      message: '안녕하세요'
    }
  ];

  const simulationButtons = [
    {
      label: '하우스키핑 요청 (수건)',
      desc: 'HK 부서로 수건 2장 정상 요청',
      payload: { roomNo: "707", domainCode: "HK", rawText: "수건 2장 주세요", summary: "수건 2장 요청", entities: { item: "towel", qty: 2 } }
    },
    {
      label: '식음료 룸서비스',
      desc: 'FB 부서로 스테이크 요청',
      payload: { roomNo: "707", domainCode: "FB", rawText: "스테이크 1개 주문할게요", summary: "스테이크 1개 룸서비스", entities: { item: "steak", qty: 1 } }
    },
    {
      label: '긴급 에스컬레이션',
      desc: '낮은 확신도(0.5)로 시설 부서 요청',
      payload: { roomNo: "707", domainCode: "FACILITY", rawText: "에어컨이 이상해요", summary: "에어컨 고장 의심", confidence: 0.5, entities: { target: "air_conditioner" } }
    }
  ];

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
          background: isManualConnected ? 'rgba(110, 231, 183, 0.1)' : 'rgba(248, 113, 113, 0.1)',
          border: `1px solid ${isManualConnected ? '#6ee7b7' : '#f87171'}`,
          fontSize: '14px', fontWeight: 600,
        }}>
          {isManualConnected ? '✅ STOMP 연결됨' : '⏸️ 연결 대기 (수동 연결 필요)'}
        </div>
        {!isManualConnected ? (
          <button
            onClick={connect}
            style={{
              padding: '12px 20px', borderRadius: '8px', background: '#10b981',
              border: 'none', color: '#fff', cursor: 'pointer', fontSize: '14px', fontWeight: 600,
            }}
          >
            🔌 WebSocket 연결
          </button>
        ) : (
          <button
            onClick={disconnect}
            style={{
              padding: '12px 20px', borderRadius: '8px', background: '#ef4444',
              border: 'none', color: '#fff', cursor: 'pointer', fontSize: '14px', fontWeight: 600,
            }}
          >
            🔌 연결 해제
          </button>
        )}
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
              {ch === 'room' ? '🚪 /topic/room/707' : ch === 'dept' ? '🏢 /topic/dept/HK' : '👑 /topic/admin'}
            </label>
          ))}
        </div>
      </div>

      {/* E2E 채팅 시뮬레이션 (실제 AI 플로우) */}
      <div style={{ marginBottom: '24px' }}>
        <h2 style={{ fontSize: '16px', color: '#34d399', marginBottom: '12px' }}>🧪 E2E 채팅 시뮬레이션 (채팅 → AI → Request → WebSocket)</h2>
        <p style={{ fontSize: '12px', color: '#888', marginBottom: '12px' }}>
          실제 채팅 API를 호출하여 AI 분석 → RequestDetectedEvent → Request 생성 → WebSocket Push 전체 흐름을 테스트합니다.
        </p>
        <div style={{ display: 'flex', gap: '12px', flexWrap: 'wrap' }}>
          {chatSimulationButtons.map((btn, i) => (
            <div key={i} style={{
              background: '#0f1f1a', border: '1px solid #1a3a2a', borderRadius: '8px',
              padding: '16px', display: 'flex', flexDirection: 'column', gap: '8px', flex: '1 1 220px'
            }}>
              <div style={{ fontSize: '14px', fontWeight: 'bold' }}>{btn.label}</div>
              <div style={{ fontSize: '12px', color: '#888' }}>{btn.desc}</div>
              <button
                onClick={() => sendChatMessage(btn.message)}
                style={{
                  padding: '8px', borderRadius: '4px', background: '#10b981', marginTop: 'auto',
                  border: 'none', color: '#fff', cursor: 'pointer', fontSize: '13px'
                }}
              >
                📤 메시지 전송
              </button>
            </div>
          ))}
        </div>
      </div>

      {/* API 시뮬레이션 (이벤트 직접 발행) */}
      <div style={{ marginBottom: '24px' }}>
        <h2 style={{ fontSize: '16px', color: '#fbbf24', marginBottom: '12px' }}>⚡ 가짜 요청(Request) 생성 테스트</h2>
        <div style={{ display: 'flex', gap: '12px', flexWrap: 'wrap' }}>
          {simulationButtons.map((btn, i) => (
            <div key={i} style={{
              background: '#1a1a2e', border: '1px solid #2a2a4a', borderRadius: '8px',
              padding: '16px', display: 'flex', flexDirection: 'column', gap: '8px', flex: '1 1 250px'
            }}>
              <div style={{ fontSize: '14px', fontWeight: 'bold' }}>{btn.label}</div>
              <div style={{ fontSize: '12px', color: '#888' }}>{btn.desc}</div>
              <button
                onClick={() => simulateRequest(btn.payload)}
                style={{
                  padding: '8px', borderRadius: '4px', background: '#3b82f6', marginTop: 'auto',
                  border: 'none', color: '#fff', cursor: 'pointer', fontSize: '13px'
                }}
              >
                🚀 요청 생성
              </button>
            </div>
          ))}
        </div>
      </div>

      {/* DB 조회 및 상태 변경 (RQ-3, RQ-4 테스트) */}
      <div style={{ marginBottom: '24px' }}>
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '12px' }}>
          <h2 style={{ fontSize: '16px', color: '#f472b6', margin: 0 }}>🗄️ 707호 DB 요청 관리 (RQ-3, RQ-4 테스트)</h2>
          <button
            onClick={fetchRequests}
            style={{
              padding: '8px 16px', borderRadius: '6px', background: '#ec4899',
              border: 'none', color: '#fff', cursor: 'pointer', fontSize: '13px', fontWeight: 600
            }}
          >
            🔄 707호 요청 목록 가져오기
          </button>
        </div>
        
        {dbRequests.length > 0 ? (
          <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
            {dbRequests.map((req) => (
              <div key={req.id} style={{
                background: '#1a1a2e', border: '1px solid #2a2a4a', borderRadius: '8px',
                padding: '12px 16px', display: 'flex', justifyContent: 'space-between', alignItems: 'center'
              }}>
                <div>
                  <span style={{ fontWeight: 'bold', marginRight: '12px' }}>ID: {req.id}</span>
                  <span style={{ 
                    padding: '2px 8px', borderRadius: '4px', fontSize: '12px', fontWeight: 'bold', marginRight: '12px',
                    background: req.status === 'PENDING' ? '#fbbf24' : req.status === 'IN_PROGRESS' ? '#3b82f6' : '#10b981',
                    color: '#fff'
                  }}>
                    {req.status}
                  </span>
                  <span style={{ color: '#a78bfa', marginRight: '12px' }}>[{req.domainCode}]</span>
                  <span style={{ color: '#e0e0e0' }}>{req.summary}</span>
                </div>
                <div style={{ display: 'flex', gap: '8px' }}>
                  <button
                    onClick={() => changeStatus(req.id, 'accept')}
                    disabled={req.status !== 'PENDING'}
                    style={{
                      padding: '6px 12px', borderRadius: '4px', background: req.status !== 'PENDING' ? '#4b5563' : '#3b82f6',
                      border: 'none', color: '#fff', cursor: req.status !== 'PENDING' ? 'not-allowed' : 'pointer', fontSize: '12px'
                    }}
                  >
                    직원 배정/수락 (Accept)
                  </button>
                  <button
                    onClick={() => changeStatus(req.id, 'complete')}
                    disabled={req.status === 'COMPLETED' || req.status === 'PENDING'}
                    style={{
                      padding: '6px 12px', borderRadius: '4px', 
                      background: (req.status === 'COMPLETED' || req.status === 'PENDING') ? '#4b5563' : '#10b981',
                      border: 'none', color: '#fff', 
                      cursor: (req.status === 'COMPLETED' || req.status === 'PENDING') ? 'not-allowed' : 'pointer', 
                      fontSize: '12px'
                    }}
                    title={req.status === 'PENDING' ? "직원 배정(수락) 후 완료 가능합니다." : ""}
                  >
                    완료 처리 (Complete)
                  </button>
                </div>
              </div>
            ))}
          </div>
        ) : (
          <div style={{
            background: '#1a1a2e', border: '1px solid #2a2a4a', borderRadius: '8px',
            padding: '20px', textAlign: 'center', color: '#666', fontSize: '14px',
          }}>
            [가져오기] 버튼을 눌러 DB에 저장된 707호의 요청을 확인하세요.<br/>
            요청이 없다면 위의 [가짜 요청 생성]을 먼저 해주세요.
          </div>
        )}
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
