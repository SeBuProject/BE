package com.example.sebu.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
//import org.springframework.http.MediaType;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class RestTemplateService {

	public String callPostApi(String url, Object param) {
		//restTemplate 방식은 계속 인증키 에러뜸. okHttp3로하면 정상으로 가져옴
		String arr[] = {"1111111111,1108141568"};
		List list = new ArrayList<>();
		list.add("1111111111");
		list.add("1108141568");
		
		System.out.println(url);
		JSONObject object = new JSONObject();
		object.put("b_no", list);
		
		/*RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<?> requestMessage = new HttpEntity<>(object,headers);
		
		HttpEntity<String> response = restTemplate.postForEntity(url, requestMessage, String.class);*/ //1
		
		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType,object.toString());
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		
		Request req = new Request.Builder()
				.url(url)
				.method("POST", body)
				.addHeader("Content-Type", "application/json")
				.build();
		try {
			Response rsp = client.newCall(req).execute();
			String res = rsp.body().toString();
			System.out.println("응답 :"+rsp.body().string());
			
			return "";
		}catch (Exception e) {
			System.out.println(e);
			return "에러";
		}
		
	}
	
}
