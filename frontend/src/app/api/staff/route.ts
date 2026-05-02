import { NextRequest, NextResponse } from "next/server";
import { getIronSession } from "iron-session";
import { cookies } from "next/headers";
import { sessionOptions, SessionData } from "@/lib/session";

const BACKEND_URL = process.env.BACKEND_URL || "http://localhost:8080";

/**
 * BFF Proxy for Staff Domain
 * staff 도메인 전용 API 핸들러입니다.
 */
export async function GET(request: NextRequest) {
  try {
    const session = await getIronSession<SessionData>(await cookies(), sessionOptions);

    if (!session.isLoggedIn || !session.token) {
      return NextResponse.json({ message: "인증되지 않은 사용자입니다." }, { status: 401 });
    }

    const { searchParams } = new URL(request.url);
    const action = searchParams.get("action");
    const departmentId = searchParams.get("departmentId");

    if (action === "requests") {
      const backendEndpoint = departmentId 
        ? `${BACKEND_URL}/staff/requests?departmentId=${departmentId}`
        : `${BACKEND_URL}/staff/requests`;

      const response = await fetch(backendEndpoint, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${session.token}`,
        },
      });

      if (!response.ok) {
        return NextResponse.json(
          { message: "백엔드 서버에서 데이터를 가져오지 못했습니다." },
          { status: response.status }
        );
      }

      const data = await response.json();
      return NextResponse.json(data);
    }

    return NextResponse.json({ message: "잘못된 요청입니다." }, { status: 400 });

  } catch (error) {
    console.error("Staff API error:", error);
    return NextResponse.json(
      { message: "서버 내부 오류가 발생했습니다." },
      { status: 500 }
    );
  }
}
