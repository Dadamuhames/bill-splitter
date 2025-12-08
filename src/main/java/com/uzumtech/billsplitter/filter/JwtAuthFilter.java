package com.uzumtech.billsplitter.filter;


import com.uzumtech.billsplitter.constant.enums.Error;
import com.uzumtech.billsplitter.constant.enums.Role;
import com.uzumtech.billsplitter.exception.JwtMalformedException;
import com.uzumtech.billsplitter.service.auth.token.JwtService;
import com.uzumtech.billsplitter.service.auth.userdetails.UserDetailsServiceDispatcher;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Order(1)
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsServiceDispatcher detailsServiceDispatcher;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);

        if (isAuthenticated) {
            filterChain.doFilter(request, response);
            return;
        }

        // TODO: proper error handling - controller advice does not handle filters

        String jwt = authHeader.substring(7);

        if (jwtService.isTokenExpired(jwt)) {
            filterChain.doFilter(request, response);
            throw new JwtMalformedException(Error.JWT_INVALID_CODE);
        }

        String username = jwtService.extractSubject(jwt);
        Role role = jwtService.extractRole(jwt);

        UserDetails userDetails = detailsServiceDispatcher.loadUserByLoginAndRole(username, role);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }
}
