package com.anook.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
// 클라이언트의 모든 API 요청을 중간에 가로채서 쿠키 안의 토큰을 검사하는 문지기 필터
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    public JwtAuthFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. HTTP 요청의 헤더에서 "Authorization" 값을 꺼냅니다.
        String token = extractTokenFromHeader(request);

        // 2. 토큰이 존재하고, 유효한(위조되지 않은) 토큰인지 검사합니다.
        if (token != null && jwtProvider.validateToken(token)) {
            var claims = jwtProvider.getClaims(token);
            String identifier = claims.getSubject();
            String role = claims.get("role", String.class);

            // 3. 토큰에서 꺼낸 권한(Role)에 "ROLE_" 접두사를 붙여 스프링 시큐리티 규격에 맞춥니다.
            var authority = new SimpleGrantedAuthority("ROLE_" + role);
            
            // 4. "이 유저는 인증되었다"는 증명서(Authentication)를 만듭니다.
            var authentication = new UsernamePasswordAuthenticationToken(
                    identifier, null, Collections.singletonList(authority));
                    
            // 5. 증명서를 시큐리티 컨텍스트(사내 장부)에 등록하여 컨트롤러로 무사히 통과하게 만듭니다.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 이후의 실제 토큰 문자열만 반환
        }
        return null;
    }
}
