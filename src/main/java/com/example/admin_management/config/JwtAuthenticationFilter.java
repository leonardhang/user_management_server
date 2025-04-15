package com.example.admin_management.config;

import com.example.admin_management.utils.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    public JwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getRequestURI().startsWith("/auth/login") || request.getRequestURI().startsWith("/auth/registry") || request.getRequestURI().startsWith("/auth/refresh")
                || request.getRequestURI().startsWith("/doc.html") || request.getRequestURI().startsWith("/v3/api-docs") || request.getRequestURI().startsWith("/swagger-resources/")
                || request.getRequestURI().startsWith("/swagger-ui") || request.getRequestURI().startsWith("/knife4j/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = jwtTokenUtil.getTokenFromRequest(request);
        String userName = jwtTokenUtil.getUsernameFromToken(token);

        if (token != null && jwtTokenUtil.validateToken(token, userName)) {
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userName, null, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

}
