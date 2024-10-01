package com.example.Store.security;

import com.example.Store.entity.User;
import com.example.Store.repository.UserRepository;
import com.example.Store.service.BlackListSerVice;
import com.example.Store.util.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    private final BlackListSerVice blackListSerVice;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;

    public JWTRequestFilter(JwtTokenUtil jwtTokenUtil,
                            UserRepository userRepository,
                            BlackListSerVice blackListSerVice) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
        this.blackListSerVice = blackListSerVice;
    }

    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;

        // String path = request.getRequestURI();
        
        // if (path.startsWith("/api")) {
        //     filterChain.doFilter(request, response);
        //     return;
        // }

        // extract email
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7).trim();
            email = jwtTokenUtil.extractEmail(token);
            System.out.println(email);
        } else {
            System.out.println("Authorization header is not found or invalid");
            filterChain.doFilter(request, response);
            return;
        }

        
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User userDetail = userRepository.findByEmail(email);
            System.out.println(SecurityContextHolder.getContext().getAuthentication() == null);
            
            if (userDetail == null) {
                System.out.println("User not found with email: " + email);
                filterChain.doFilter(request, response);
                return; 
            }
            
            if (!jwtTokenUtil.validateToken(token)) {
                System.out.println("Token is invalid");
                filterChain.doFilter(request, response);
                return; 
            }
        
            if (blackListSerVice.isTokenBlacklisted(token)) {
                System.out.println("Token is blacklisted");
                filterChain.doFilter(request, response);
                return; 
            }

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                System.out.println("User authenticated: " + email);
            } else {
                System.out.println("Token is invalid");
            }
            filterChain.doFilter(request, response);
        }

    }

