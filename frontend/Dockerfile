# 1. Dependencies install
FROM node:22-alpine AS deps
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm ci

# 2. Build stage
FROM node:22-alpine AS build
WORKDIR /app
COPY --from=deps /app/node_modules ./node_modules
COPY . .
RUN npm run build && mkdir -p /app/public

# 3. Run stage
FROM node:22-alpine
WORKDIR /app

# standalone 서버 복사
COPY --from=build /app/.next/standalone ./
COPY --from=build /app/.next/static ./.next/static

# public 폴더 복사 (빌드 스테이지에서 보장)
COPY --from=build /app/public ./public

EXPOSE 3000
ENV PORT=3000
CMD ["node", "server.js"]
