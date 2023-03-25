package com.example.sebu.auth.config;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.sebu.auth.service.UserTokenService;
import com.example.sebu.auth.utils.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtFilter extends OncePerRequestFilter{
	
	private UserTokenService userService;
	
	private String secretKey;
	public JwtFilter(UserTokenService userService2, String secretKey2) {
		this.userService = userService2;
		this.secretKey = secretKey2;
	}
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		final String auth = request.getHeader(HttpHeaders.AUTHORIZATION);
		logger.info(auth);
		
		if(auth==null || !auth.startsWith("Bearer ")) {
			logger.info("auth없거나 잘못보냄");
			filterChain.doFilter(request, response);
			return;
		}
		
		String userToken = auth.split(" ")[1];
		
		// token expire 여부
		if(JwtUtil.isExpired(userToken, secretKey)) {
			logger.info("토큰만료");
			filterChain.doFilter(request, response);
			return;
		}
		String userName = JwtUtil.getUserName(userToken, secretKey);
		String password = JwtUtil.getUserPassword(userToken, secretKey);
		String id = JwtUtil.getUserId(userToken, secretKey);
		logger.info("토큰에서 가져온id :"+userName);
		logger.info("토큰에서 가져온password :"+password);
		logger.info("토큰에서 가져온name :"+password);
		
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userName, password, List.of(new SimpleGrantedAuthority("USER")));
		
		token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		
		SecurityContextHolder.getContext().setAuthentication(token);
		filterChain.doFilter(request, response);
		
		
	}
}
