package com.example.sebu.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sebu.auth.model.Member;
import com.example.sebu.auth.service.UserService;
import com.example.sebu.auth.service.UserTokenService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	@Autowired
	private UserTokenService userTokenService;
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody Map param){
		System.out.println(param.get("id"));
		String id = param.get("id").toString();
		String password = param.get("password").toString();
		Member sebuMember = userService.loginUser(id,password);
		
		if(sebuMember != null) {
			return ResponseEntity.ok().body(userTokenService.getToken(sebuMember));
		}else {
			return ResponseEntity.badRequest().body("로그인 오류"); 
		}
		
	}
	
	@PostMapping("/join")
	public ResponseEntity<String> join(@RequestBody Member member){
		try {
			userService.signUpUser(member);
			return ResponseEntity.ok().body("join token");
		}catch(Exception e) {
			return ResponseEntity.badRequest().body("회원가입중 오류가 발생했습니다");
		}
		
	}
	
}
