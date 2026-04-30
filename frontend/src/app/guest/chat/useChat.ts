import { useState, useEffect, useRef } from 'react';
import { Client } from '@stomp/stompjs';
import { ChatMessage } from './_components/ChatScreen';

const ROOM_NO = '707'; // 하드코딩 (추후 연동 시 동적 할당)

interface BackendMessage {
  id: number;
  senderType: 'GUEST' | 'AI';
  content: string;
  translatedContent: string | null;
  createdAt: string;
}

export function useChat() {
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [isTyping, setIsTyping] = useState(false);
  const stompClientRef = useRef<Client | null>(null);

  // 1. 대화 내역 불러오기
  useEffect(() => {
    const fetchHistory = async () => {
      try {
        const response = await fetch(`/api/chat/${ROOM_NO}/messages`);
        if (!response.ok) throw new Error('Failed to fetch chat history');
        
        const data: BackendMessage[] = await response.json();
        
        if (data.length === 0) {
          // 히스토리가 없으면 Welcome & Idle 메시지 기본 제공
          setMessages([
            {
              id: 'welcome-1',
              variant: 'received',
              type: 'WELCOME',
              content: '안녕하세요! 그랜드 호텔 AI 컨시어지입니다 🏨\n무엇이든 편하게 말씀해주세요.\n\n💡 예시:\n• "수건 2개 가져다주세요"\n• "조식 몇 시에 열어요?"',
            },
            {
              id: 'idle-1',
              variant: 'received',
              type: 'QUICK_REPLY',
              content: '추가로 필요한 서비스가 있으신가요?',
              meta: { options: ['🛏️ 수건 요청', '🧴 어메니티 추가', '🧹 룸 클리닝', '🔑 체크아웃 문의'] }
            }
          ]);
        } else {
          const historyMessages: ChatMessage[] = data.map(msg => ({
            id: msg.id.toString(),
            variant: msg.senderType === 'GUEST' ? 'sent' : 'received',
            content: msg.content,
            type: 'TEXT' // 과거 기록은 기본 TEXT로 처리 (추후 DB 확장에 따라 수정 가능)
          }));
          setMessages(historyMessages);
        }
      } catch (error) {
        console.error('Error fetching chat history:', error);
      }
    };

    fetchHistory();
  }, []);

  // 2. WebSocket 연결
  useEffect(() => {
    const client = new Client({
      brokerURL: 'ws://localhost:8080/ws',
      debug: function (str) {
        console.log(str);
      },
      reconnectDelay: 5000,
      onConnect: () => {
        console.log('STOMP Connected');
        client.subscribe(`/topic/room/${ROOM_NO}`, (message) => {
          if (message.body) {
            const payload = JSON.parse(message.body);
            
            if (payload.type === 'AI_RESPONSE' || payload.type === 'AI_ERROR') {
              // AI 응답 수신 시 타이핑 종료
              setIsTyping(false);
              
              const newAiMsg: ChatMessage = {
                id: payload.messageId ? payload.messageId.toString() : Date.now().toString(),
                variant: 'received',
                content: payload.content,
                type: payload.uiType || 'TEXT', // 백엔드에서 uiType을 주면 매핑, 없으면 TEXT
                meta: payload.meta || {},
              };
              
              setMessages(prev => [...prev, newAiMsg]);
            } else if (payload.type === 'STATUS_UPDATE') {
              // 진행 상태 업데이트 처리
              const statusMsg: ChatMessage = {
                id: payload.messageId ? payload.messageId.toString() : Date.now().toString(),
                variant: 'received',
                type: 'STATUS_CARD',
                content: payload.content, // "수건 요청이 접수되었습니다!" 등
                meta: { progress: payload.progress || 33 } // 33, 66, 100 등
              };
              setMessages(prev => [...prev, statusMsg]);
              
              // 만약 진행률이 100%면 피드백 카드도 띄워주기
              if (payload.progress === 100) {
                setTimeout(() => {
                  setMessages(prev => [...prev, {
                    id: `feedback-${Date.now()}`,
                    variant: 'received',
                    type: 'FEEDBACK',
                  }]);
                }, 1000);
              }
            }
          }
        });
      },
      onStompError: (frame) => {
        console.error('Broker reported error: ' + frame.headers['message']);
        console.error('Additional details: ' + frame.body);
      },
    });

    client.activate();
    stompClientRef.current = client;

    return () => {
      if (stompClientRef.current) {
        stompClientRef.current.deactivate();
      }
    };
  }, []);

  // 3. 메시지 전송
  const sendMessage = async (text: string) => {
    // 임시 아이디로 화면에 먼저 표시
    const tempId = `temp-${Date.now()}`;
    const newUserMsg: ChatMessage = { id: tempId, variant: 'sent', content: text };
    setMessages(prev => [...prev, newUserMsg]);
    
    // 타이핑 애니메이션 시작
    setIsTyping(true);

    try {
      const response = await fetch(`/api/chat/${ROOM_NO}/messages`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ content: text }),
      });

      if (!response.ok) {
        // 에러 발생 시 처리
        setIsTyping(false);
        const errorData = await response.json().catch(() => ({}));
        
        // 429 에러(디바운스) 또는 기타 에러 메시지 표시
        const errorMsg: ChatMessage = {
          id: `error-${Date.now()}`,
          variant: 'received',
          content: errorData.error || '메시지 전송에 실패했습니다. 다시 시도해 주세요.',
        };
        setMessages(prev => [...prev, errorMsg]);
        return;
      }
      
      const data = await response.json();
      
      // 임시 아이디를 실제 아이디로 교체 (선택사항, 화면 변화 없음)
      setMessages(prev => prev.map(msg => 
        msg.id === tempId ? { ...msg, id: data.guestMessageId.toString() } : msg
      ));

    } catch (error) {
      console.error('Error sending message:', error);
      setIsTyping(false);
    }
  };

  return {
    messages,
    isTyping,
    sendMessage
  };
}
