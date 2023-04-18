package com.example.sebu.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.sebu.auth.service.UserTokenService;


@EnableWebSecurity
@Configuration
public class SecurityConfig {

	@Autowired
	private UserTokenService userTokenService;
	@Value("${jwt.secret}")
	private String secretKey;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
		
		return httpSecurity.
				httpBasic().disable()
				.csrf().disable()
				.cors().disable()
				.authorizeHttpRequests().requestMatchers("/api/v1/users/**").permitAll()
				.requestMatchers("/**").permitAll()
				.requestMatchers("/api/v1/contents/**").permitAll()
				.anyRequest().authenticated()
				.and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.addFilterBefore(new JwtFilter(userTokenService, secretKey), UsernamePasswordAuthenticationFilter.class)
				.build();
	}
	
	@Bean
	public BCryptPasswordEncoder encodePassword() {
		return new BCryptPasswordEncoder();
	}
}
