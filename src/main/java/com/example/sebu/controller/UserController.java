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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
@Tag(name = "세무사를부탁해(세부) 회원 API", description = "로그인 ,회원가입 등 API")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	@Autowired
	private UserTokenService userTokenService;
	
	@Autowired
	private UserService userService;
	
	@Operation(summary = "로그인", description = "로그인 API")
	@Parameter(name = "Map<String>", description = "{\"id\": {}, \"password\": {}")
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
	@Operation(summary = "회원가입", description = "회원가입 API")
	@Parameter(name = "Member", description = "Member 클래스")
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
