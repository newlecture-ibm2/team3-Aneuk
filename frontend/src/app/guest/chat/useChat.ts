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
        
        const historyMessages: ChatMessage[] = data.map(msg => ({
          id: msg.id.toString(),
          variant: msg.senderType === 'GUEST' ? 'sent' : 'received',
          content: msg.content,
        }));

        setMessages(historyMessages);
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
              };
              
              setMessages(prev => [...prev, newAiMsg]);
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
