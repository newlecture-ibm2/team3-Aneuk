# 🏨 아늑 (Aneuk) : AI 기반 지능형 호텔 통합 관리 시스템

> 고객 요청을 AI가 분석하여 태스크를 자동 생성하고, 적합한 직원에게 분배하며, 처리 과정을 추적하는 차세대 호텔 운영 시스템입니다.

## 📖 프로젝트 개요

**아늑(Aneuk)**은 다국어로 입력되는 비정형 데이터(투숙객의 자연어 요청)를 AI가 분석하여 정형화된 업무 지시서(Task)로 변환하고, 이를 호텔 내 각 담당 부서에 자동으로 라우팅해주는 플랫폼입니다. 
관리자는 AI 확신도(Confidence Score) 기반의 하이브리드 컨펌 시스템을 통해 유연한 업무 배분을 수행하며, 처리된 데이터는 RAG 기반 지식으로 자생적 진화를 이룹니다.

## ✨ 핵심 가치
- **One-pass 프로세싱**: 단일 AI 프롬프트로 다국어 번역, 의도(Intent) 분석, 식별자(Entity) 추출을 한 번에 처리
- **초경량 Task Code**: 복잡한 JSON이 아닌 `[부서]|[긴급도]|[분류]|[항목]|[수량]` 형태의 약속된 코드 포맷을 통한 토큰 80~90% 절약 및 시스템 안정성 확보
- **하이브리드 자동화**: AI 확신도(Confidence ≥ 0.8)에 따라 완전 자동 분배 또는 관리자 수동 컨펌(반자동) 체계 도입
- **데이터 플라이휠**: 미답변 데이터 및 처리 이력을 RAG에 반영하여 도메인 특화 지식망 자생적 튜닝 구축

---

## 👥 사용자 역할 (Roles)

| 역할 | 설명 | 인증 방식 | 디바이스 |
|------|------|-----------|----------|
| 🧑 **Guest (투숙객)** | 객실 QR 스캔을 통해 별도 앱 설치 없이 PWA로 즉시 접속하여 다국어 요청 | 객실번호 + 이름 | 모바일 |
| 👷 **Staff (직원)** | 실시간 알림을 수신하고, 담당 부서로 할당된 태스크 수락 및 처리 | 관리자 발급 PIN (JWT) | PC, 모바일 |
| 🏨 **Admin (관리자)** | 전체 태스크 실시간 모니터링, AI 미분류/특이사항 건 수동 에스컬레이션 컨펌 | ID/PW (JWT) | PC |

---

## 🚀 주요 기능 (MVP)

1. **AI 챗봇 기반 고객 요청 접수 (Guest Chat)**: 투숙객이 모국어로 자연스럽게 요청하거나 빠른 버튼으로 즉시 요청 
2. **태스크 자동 생성 및 부서 라우팅**: AI가 의도를 분석하여 하우스키핑, 시설관리, F&B, 프론트 등으로 업무 자동 배분
3. **AI 되묻기 (Clarification) 및 Fallback**: 필수 정보가 누락된 경우 AI가 투숙객에게 되물어 정보를 보완하거나 관리자에게 에스컬레이션
4. **실시간 상태 추적 (WebSocket)**: 투숙객과 직원 모두 현재 태스크 진행 상태(접수됨 → 배정됨 → 진행중 → 완료) 실시간 동기화
5. **RAG 기반 FAQ 자동 응대**: 호텔 이용 정보(조식 시간, 편의시설 등)를 벡터 DB(pgvector)에서 검색해 AI 즉답
6. **교대 인수인계 자동화**: 이전 근무조의 미처리 잔여 업무, 특이사항, 주요 처리 내역을 AI가 취합하여 자연어 브리핑 생성

---

## 🛠 기술 스택 (Tech Stack)

### Backend
- **Language & Framework**: Java 21, Spring Boot 3.x
- **Architecture**: Hexagonal Architecture (Ports & Adapters), DDD (Domain-Driven Design)
- **Database**: PostgreSQL (pgvector for RAG), Spring Data JPA
- **Real-time**: WebSocket (STOMP)
- **Security**: Spring Security, JWT (HttpOnly Cookie)

### Frontend
- **Framework**: Next.js 15.x (App Router)
- **Platform**: PWA (Progressive Web App)
- **State Management**: Zustand (Local State)
- **Styling**: CSS Modules, CSS Variables

### AI & Integrations
- **AI Engine**: Google Gemini API
- **AI Features**: Intent/Entity Classification, Clarification, Summarization, Safety Filters

---

## 🏗 시스템 아키텍처 주요 방어 전략
- **분산 환경의 동시성 제어**: `@Version`을 이용한 JPA 낙관적 락(Optimistic Locking)을 통해 다중 직원의 상태 변경 경합 해결
- **개인정보 보호(PII)**: 정규표현식을 이용한 백엔드 1차 마스킹 및 API 보안 설계 도입
- **오프라인-퍼스트 (Offline-First)**: IndexedDB를 이용해 네트워크 단절 시에도 안정적인 앱 UX 보장

---
*(이 README는 `docs/MVP_핵심기능_통합안.md` 및 `docs/기술_스펙_정리안.md`를 바탕으로 작성되었습니다.)*