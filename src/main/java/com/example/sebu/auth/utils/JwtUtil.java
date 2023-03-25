package com.example.sebu.auth.utils;


import java.util.Date;

import com.example.sebu.auth.model.Member;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtUtil {

	public static String createJwt(Member member,String secretKey, Long expiredMs) {
		Claims claims = Jwts.claims();
		claims.put("id", member.getId());
		claims.put("password", member.getPassword());
		claims.put("name", member.getName());
		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expiredMs))
				.signWith(SignatureAlgorithm.HS256, secretKey)
				.compact();
				
	}
	
	public static boolean isExpired(String token, String secretKey) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getExpiration().before(new Date());
	}
	
	public static String getUserName(String token, String secretKey) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("name").toString();
	}

	public static String getUserPassword(String token, String secretKey) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("password").toString();
	}
	
	public static String getUserId(String token, String secretKey) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("id").toString();
	}
}
