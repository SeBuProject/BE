package com.example.sebu.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.sebu.auth.model.Member;
import com.example.sebu.auth.repository.MemberRepository;

@Service
public class UserService {

	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Value("${jwt.secret}")
	private String secretKey;
	
	public void signUpUser(Member member) {	
		String password = member.getPassword();
		password = passwordEncoder.encode(password);
		member.setPassword(password);
		System.out.println(member.getId());
		System.out.println(member.getPassword());
		System.out.println(member.getName());
		memberRepository.save(member);
	}
	
	public Member loginUser (String id, String password) {
		Member sebuMember = memberRepository.findById(id);
		String msg= "";
		if(sebuMember != null) {
			
			String sebuPassword = sebuMember.getPassword();
			
			if(!passwordEncoder.matches(password, sebuPassword)) {
				sebuMember = null;
			}
		}else {
			sebuMember = null;
		}
		
		return sebuMember;
	}
}
