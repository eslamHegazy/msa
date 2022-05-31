package com.ScalableTeam.httpServer;

import com.ScalableTeam.httpServer.caching.RedisUtility;
import com.ScalableTeam.httpServer.utils.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.attribute.UserPrincipalNotFoundException;

@Slf4j
@Component
@AllArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final RedisUtility redisUtility;
    private final JwtUtil jwtUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Inside Authentication Filter originated by request {}", request.getRequestURI());

//        final String authToken = request.getHeader("Authorization");
//        String userId = jwtUtil.extractUsername(authToken);
//        String cachedToken = redisUtility.getValue(userId);
//
//        if(authToken == null || !authToken.equals(cachedToken)) {
//            response.sendError(HttpStatus.UNAUTHORIZED.value());
//            return;
//        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getRequestURI().startsWith("/auth/");
    }
}
