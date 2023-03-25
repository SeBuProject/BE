package com.example.sebu.auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.sebu.auth.model.Member;
import com.example.sebu.auth.utils.JwtUtil;

@Service
public class UserTokenService {
	
	@Value("${jwt.secret}")
	private String secretKey;
	
	private Long expiredMs = 1000 * 60 *60l;
	
	public String getToken(Member sebuMember) {
		
		return JwtUtil.createJwt(sebuMember, secretKey, expiredMs);
	}
}
