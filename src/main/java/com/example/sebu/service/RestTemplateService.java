package com.example.sebu.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
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

	public String businessStatusInquiry(String url, Object param) {
		//restTemplate 방식은 계속 인증키 에러뜸. okHttp3로하면 정상으로 가져옴
		
		//변수선언
		JSONObject object = new JSONObject();
		
		int apiCallCnt = 1;
		
		ArrayList<String> splitList = new ArrayList<String>();
		//파라미터 가져옴
		ArrayList<String> list = (ArrayList<String>)param;
		
		//파라미터 100개단위로 나누기
		apiCallCnt += (list.size()-1)/100;
		System.out.println("api 돌릴 횟수 :"+list.size());
		System.out.println("api 돌릴 횟수 :"+apiCallCnt);
		int cnt =0;
		if(apiCallCnt >1) {
			for(int i=0; i<apiCallCnt; i++) {
				int fromIdx = i*100;
				int lastIdx = (100*(i+1));
				//list.subList(i*100, (100*(i+1))-1);
				if(lastIdx > list.size()) {
					for(String a : list.subList(fromIdx, list.size())) {
						cnt++;
						System.out.println("split list 사업자 번호 :" + a + "count :" + cnt);
						
					}
					object.put("b_no", list.subList(fromIdx, list.size()));
					callbusinessStatusInquiryApi(url,object);
				}else {
					for(String a : list.subList(fromIdx, lastIdx)) {
						cnt++;
						System.out.println("split list 사업자 번호 :" + a + "count :" + cnt);
					}
					object.put("b_no", list.subList(fromIdx, lastIdx));
					callbusinessStatusInquiryApi(url,object);
				}
				
			}
		}else {
			object.put("b_no", list);
			
			callbusinessStatusInquiryApi(url,object);
		}
		
		System.out.println("총 갯수 : "+cnt);
		
		
		return "";
		
//		JSONParser parser = new JSONParser();
//		object.put("b_no", list);
		
		/*RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<?> requestMessage = new HttpEntity<>(object,headers);
		
		HttpEntity<String> response = restTemplate.postForEntity(url, requestMessage, String.class);*/ //1
		
//		MediaType mediaType = MediaType.parse("application/json");
//		RequestBody body = RequestBody.create(mediaType,object.toString());
//		OkHttpClient client = new OkHttpClient().newBuilder().build();
//		
//		Request req = new Request.Builder()
//				.url(url)
//				.method("POST", body)
//				.addHeader("Content-Type", "application/json")
//				.build();
//		try {
//			Response rsp = client.newCall(req).execute();
//			String res = rsp.body().toString();
//			
//			JSONObject objResp =(JSONObject)parser.parse(rsp.body().string());
//			
//			System.out.println("응답 :"+ objResp.get("data"));
//			return "";
//		}catch (Exception e) {
//			System.out.println(e);
//			return "에러";
//		}
		
	}

	private void callbusinessStatusInquiryApi(String url, JSONObject object) {
		JSONParser parser = new JSONParser();
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
			
			JSONObject objResp =(JSONObject)parser.parse(rsp.body().string());
			
			JSONArray array = (JSONArray)objResp.get("data");
			
			for(Object a : array) {
				System.out.println("data"+a);
			}
			
		}catch (Exception e) {
			System.out.println(e);
		}
		
	}
	
}
