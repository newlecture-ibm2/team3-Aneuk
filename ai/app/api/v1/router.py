from fastapi import APIRouter
# 도메인별 라우터를 향후 여기서 import 하여 등록합니다.

api_router = APIRouter()

# 예시: api_router.include_router(breakfast.router, prefix="/breakfast", tags=["breakfast"])
