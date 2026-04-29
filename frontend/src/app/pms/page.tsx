'use client';

import React, { useEffect, useState } from 'react';
import styles from './page.module.css';
import useGuests from './useGuests';

/* ── 날짜 포맷 ── */
function formatDateTime(dateStr: string) {
  if (!dateStr) return '-';
  const d = new Date(dateStr);
  return d.toLocaleDateString('ko-KR', {
    year: 'numeric', month: '2-digit', day: '2-digit',
    hour: '2-digit', minute: '2-digit',
  });
}

function formatDate(dateStr: string) {
  if (!dateStr) return '-';
  const d = new Date(dateStr);
  return d.toLocaleDateString('ko-KR', {
    year: 'numeric', month: '2-digit', day: '2-digit',
  });
}

/* ══════════════════════════════════════════════════════════
   PMS  –  Virtual Guest Management Console
   ══════════════════════════════════════════════════════════ */
export default function PmsPage() {
  const { guests, loading, error, fetchGuests, checkIn, checkOut } = useGuests();

  // ── 모달 상태 ──
  const [showCheckIn, setShowCheckIn] = useState(false);
  const [checkOutTarget, setCheckOutTarget] = useState<{
    id: number; roomNumber: string; guestName: string;
  } | null>(null);

  // ── 폼 상태 ──
  const [roomId, setRoomId] = useState('');
  const [guestName, setGuestName] = useState('');
  const [language, setLanguage] = useState('ko');
  const [checkoutDate, setCheckoutDate] = useState('');
  const [formErrors, setFormErrors] = useState<Record<string, string>>({});
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => { fetchGuests(); }, [fetchGuests]);

  // ── 체크인 ──
  const handleCheckIn = async () => {
    const errs: Record<string, string> = {};
    if (!roomId) errs.roomId = '필수';
    if (!guestName.trim()) errs.guestName = '필수';
    if (!checkoutDate) errs.checkoutDate = '필수';
    setFormErrors(errs);
    if (Object.keys(errs).length > 0) return;

    setSubmitting(true);
    try {
      await checkIn({
        roomId: Number(roomId),
        guestName: guestName.trim(),
        language,
        checkoutDate,
      });
      resetForm();
      setShowCheckIn(false);
    } catch { /* useGuests handles error */ }
    finally { setSubmitting(false); }
  };

  const resetForm = () => {
    setRoomId(''); setGuestName(''); setLanguage('ko');
    setCheckoutDate(''); setFormErrors({});
  };

  // ── 체크아웃 ──
  const handleCheckOut = async () => {
    if (!checkOutTarget) return;
    try { await checkOut(checkOutTarget.id); }
    catch { /* useGuests handles error */ }
    finally { setCheckOutTarget(null); }
  };

  /* ═══════ RENDER ═══════ */
  return (
    <div className={styles.pmsRoot}>
      {/* ── Top bar ── */}
      <div className={styles.topBar}>
        <div className={styles.brandMark}>
          <div className={styles.brandIcon}>A</div>
          <span className={styles.brandName}>ANEUK PMS</span>
          <span className={styles.brandTag}>VIRTUAL</span>
        </div>
        <div className={styles.topBarRight}>
          <div className={styles.statusDot} />
          <span className={styles.statusText}>System Online</span>
        </div>
      </div>

      {/* ── Main ── */}
      <div className={styles.mainContainer}>
        {/* Header */}
        <div className={styles.pageHeader}>
          <div className={styles.titleGroup}>
            <h1 className={styles.pageTitle}>객실 손님 관리</h1>
            <p className={styles.pageSubtitle}>
              개발 및 테스트를 위한 가상 PMS — 투숙객 더미 데이터를 생성하고 관리합니다.
            </p>
          </div>
          <button className={styles.checkInBtn} onClick={() => setShowCheckIn(true)}>
            <span style={{ fontSize: 18 }}>+</span> 손님 추가 (체크인)
          </button>
        </div>

        {/* Stats */}
        <div className={styles.statsRow}>
          <div className={styles.statCard}>
            <div className={`${styles.statIcon} ${styles.statIconGreen}`}>🛏️</div>
            <div className={styles.statInfo}>
              <span className={styles.statLabel}>현재 투숙 중</span>
              <span className={styles.statValue}>{guests.length}</span>
            </div>
          </div>
          <div className={styles.statCard}>
            <div className={`${styles.statIcon} ${styles.statIconBlue}`}>🔑</div>
            <div className={styles.statInfo}>
              <span className={styles.statLabel}>오늘 체크인</span>
              <span className={styles.statValue}>
                {guests.filter(g => {
                  const d = new Date(g.createdAt);
                  const today = new Date();
                  return d.toDateString() === today.toDateString();
                }).length}
              </span>
            </div>
          </div>
          <div className={styles.statCard}>
            <div className={`${styles.statIcon} ${styles.statIconPurple}`}>📋</div>
            <div className={styles.statInfo}>
              <span className={styles.statLabel}>오늘 체크아웃 예정</span>
              <span className={styles.statValue}>
                {guests.filter(g => {
                  const d = new Date(g.checkoutDate);
                  const today = new Date();
                  return d.toDateString() === today.toDateString();
                }).length}
              </span>
            </div>
          </div>
        </div>

        {/* Error */}
        {error && (
          <div className={styles.errorBanner}>⚠️ {error}</div>
        )}

        {/* Table */}
        <div className={styles.tableCard}>
          <div className={styles.tableTitle}>
            📋 투숙객 목록
          </div>

          <div className={styles.tableHeader}>
            <span>객실 번호</span>
            <span>이름</span>
            <span>체크인</span>
            <span>체크아웃 예정</span>
            <span>상태</span>
            <span style={{ textAlign: 'right' }}>액션</span>
          </div>

          {loading ? (
            <>
              {[1, 2, 3].map(i => (
                <div key={i} className={styles.skeletonRow}>
                  {[1, 2, 3, 4, 5, 6].map(j => (
                    <div key={j} className={styles.skeletonBlock} />
                  ))}
                </div>
              ))}
            </>
          ) : guests.length === 0 ? (
            <div className={styles.emptyState}>
              <div className={styles.emptyIcon}>🏨</div>
              현재 투숙 중인 손님이 없습니다.<br />
              <span style={{ fontSize: 12, color: '#475569' }}>
                &quot;+ 손님 추가&quot; 버튼으로 체크인해보세요.
              </span>
            </div>
          ) : (
            guests.map((guest, idx) => (
              <div
                key={guest.id}
                className={styles.tableRow}
                style={{ animationDelay: `${idx * 0.08}s` }}
              >
                <div>
                  <span className={styles.roomBadge}>{guest.roomNumber}</span>
                </div>
                <div className={styles.guestName}>{guest.guestName}</div>
                <div className={styles.dateText}>{formatDateTime(guest.createdAt)}</div>
                <div className={styles.dateText}>{formatDate(guest.checkoutDate)}</div>
                <div>
                  <span className={styles.statusBadge}>
                    <span className={styles.statusDotInline} />
                    투숙 중
                  </span>
                </div>
                <div style={{ display: 'flex', justifyContent: 'flex-end' }}>
                  <button
                    className={styles.actionBtn}
                    title="체크아웃 (Hard Delete)"
                    onClick={() => setCheckOutTarget({
                      id: guest.id,
                      roomNumber: guest.roomNumber,
                      guestName: guest.guestName,
                    })}
                  >
                    🗑️
                  </button>
                </div>
              </div>
            ))
          )}
        </div>
      </div>

      {/* ══ Check-In Modal ══ */}
      {showCheckIn && (
        <div className={styles.overlay} onClick={() => { setShowCheckIn(false); resetForm(); }}>
          <div className={styles.modal} onClick={e => e.stopPropagation()}>
            <div className={styles.modalHeader}>
              <h2 className={styles.modalTitle}>🔑 손님 추가 (체크인)</h2>
              <button className={styles.modalClose} onClick={() => { setShowCheckIn(false); resetForm(); }}>✕</button>
            </div>

            <div className={styles.formGroup}>
              <div>
                <label className={styles.fieldLabel}>객실 ID</label>
                <input
                  className={styles.fieldInput}
                  placeholder="예: 1 (room 테이블의 ID)"
                  value={roomId}
                  onChange={e => setRoomId(e.target.value)}
                />
                {formErrors.roomId && <div className={styles.fieldError}>{formErrors.roomId}</div>}
              </div>

              <div>
                <label className={styles.fieldLabel}>투숙객 이름</label>
                <input
                  className={styles.fieldInput}
                  placeholder="예: 홍길동"
                  value={guestName}
                  onChange={e => setGuestName(e.target.value)}
                />
                {formErrors.guestName && <div className={styles.fieldError}>{formErrors.guestName}</div>}
              </div>

              <div>
                <label className={styles.fieldLabel}>언어</label>
                <select
                  className={styles.fieldSelect}
                  value={language}
                  onChange={e => setLanguage(e.target.value)}
                >
                  <option value="ko">한국어</option>
                  <option value="en">English</option>
                  <option value="ja">日本語</option>
                  <option value="zh">中文</option>
                </select>
              </div>

              <div>
                <label className={styles.fieldLabel}>체크아웃 예정일</label>
                <input
                  className={styles.fieldInput}
                  type="date"
                  value={checkoutDate}
                  onChange={e => setCheckoutDate(e.target.value)}
                />
                {formErrors.checkoutDate && <div className={styles.fieldError}>{formErrors.checkoutDate}</div>}
              </div>
            </div>

            <div className={styles.modalActions}>
              <button className={styles.btnCancel} onClick={() => { setShowCheckIn(false); resetForm(); }}>
                취소
              </button>
              <button className={styles.btnSubmit} onClick={handleCheckIn} disabled={submitting}>
                {submitting ? '처리 중...' : '체크인'}
              </button>
            </div>
          </div>
        </div>
      )}

      {/* ══ Check-Out Confirm Modal ══ */}
      {checkOutTarget && (
        <div className={styles.overlay} onClick={() => setCheckOutTarget(null)}>
          <div className={styles.confirmModal} onClick={e => e.stopPropagation()}>
            <div className={styles.confirmIcon}>⚠️</div>
            <h3 className={styles.confirmTitle}>
              {checkOutTarget.roomNumber} {checkOutTarget.guestName} 체크아웃
            </h3>
            <p className={styles.confirmSubtitle}>
              체크아웃 시 데이터가 완전 삭제(Hard Delete)되며<br />복구할 수 없습니다.
            </p>
            <div className={styles.confirmActions}>
              <button className={styles.btnCancel} onClick={() => setCheckOutTarget(null)}>취소</button>
              <button className={styles.btnDanger} onClick={handleCheckOut}>체크아웃</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
