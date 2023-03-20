package com.example.sebu.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestTemplateService {

	public String callPostApi(String url, Object param) {
		
		String arr[] = {"1111111111,1108141586"};
		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		Map<String, Object> body = new HashMap<String, Object>();
		
		body.put("b_no",arr);
		
		HttpEntity<?> requestMessage = new HttpEntity<>(body,headers);
		
		HttpEntity<String> response = restTemplate.postForEntity(url, requestMessage, String.class);
		
		return response.toString();
	}
	
}
