package com.openclassrooms.mddapi.security;

import com.openclassrooms.mddapi.services.IUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final IUserService userService;

    @Autowired
    public JwtAuthenticationFilter(JwtUtil jwtUtil, IUserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        
        final String authHeader = request.getHeader("Authorization");
        String requestPath = request.getRequestURI();
        
        logger.debug("Processing request for path: " + requestPath);
        logger.debug("Authorization header present: " + (authHeader != null));
        
        String username = null;
        String jwt = null;
          if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwt);
                logger.debug("Successfully extracted username from token: " + username);
                System.out.println("Processing request with username from token: " + username);
            } catch (Exception e) {
                logger.error("Error extracting username from token: " + e.getMessage(), e);
                System.err.println("Token extraction failed: " + e.getMessage());
            }
        } else if (authHeader != null) {
            logger.warn("Authorization header doesn't start with 'Bearer '");
            System.out.println("Invalid authorization header format: " + authHeader);
        } else {
            logger.debug("No authorization header found in request to: " + requestPath);
        }
        
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                // Check if token is valid based on username only, no need to validate password
                if (jwtUtil.validateToken(jwt, username)) {
                    logger.debug("Token validated successfully for username: " + username);
                    
                    // Try to load user details, but handle the case where user might not exist in DB
                    try {
                        UserDetails userDetails = userService.findByUsername(username);
                        logger.debug("Found userDetails for username: " + username);
                        
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                        logger.debug("Authentication set in SecurityContextHolder with full user details");                    } catch (Exception userEx) {
                        // If user lookup fails, log the error but don't set partial authentication
                        // This prevents operations that require a full user object from proceeding with invalid data
                        logger.error("User lookup failed for username: " + username + ". Error: " + userEx.getMessage());
                        logger.debug("No authentication set in SecurityContextHolder due to user lookup failure");
                    }
                } else {
                    logger.warn("Token validation failed for username: " + username);
                }
            } catch (Exception e) {
                logger.error("Error in authentication process: " + e.getMessage(), e);
            }
        }
        
        filterChain.doFilter(request, response);
    }
}
