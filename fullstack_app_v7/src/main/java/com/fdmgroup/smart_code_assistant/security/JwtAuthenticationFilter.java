package com.fdmgroup.smart_code_assistant.security;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fdmgroup.smart_code_assistant.service.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	private static final Logger logger = Logger.getLogger(JwtAuthenticationFilter.class.getName());
	
	@Autowired
	@Qualifier("customUserDetailsService")
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtService jwtService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String authHeader = request.getHeader("Authorization");
		logger.info("Received Authorization header: " + (authHeader != null ? authHeader.substring(0, Math.min(20, authHeader.length())) + "..." : "null"));
		
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			logger.warning("No valid Authorization header found");
			filterChain.doFilter(request, response);
			return;
		}
		
		final String jwt = authHeader.substring(7);
		final String username = jwtService.extractUsername(jwt);
		logger.info("Extracted username from JWT: " + username);
		
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			logger.info("Loading user details for username: " + username);
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			logger.info("User details loaded successfully");
			
			if (jwtService.isTokenValid(jwt, userDetails)) {
				logger.info("JWT token is valid");
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
					userDetails,
					null,
					userDetails.getAuthorities()
				);
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
				logger.info("Authentication token set in SecurityContext");
			} else {
				logger.warning("JWT token is invalid");
			}
		} else {
			logger.warning("Username is null or user is already authenticated");
		}
		
		filterChain.doFilter(request, response);
	}
} 