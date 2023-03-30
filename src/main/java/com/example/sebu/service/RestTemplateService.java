package com.example.sebu.service;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.example.sebu.message.ResponseMessage;
//utils
import com.example.sebu.utils.Utils;

@Service
public class RestTemplateService {

	public ResponseMessage businessStatusInquiry(String url, Object param) {
		//restTemplate 방식은 계속 인증키 에러뜸. okHttp3로하면 정상으로 가져옴
		
		//변수선언
		ResponseMessage resp = new ResponseMessage();
		JSONObject object = new JSONObject();
		JSONArray businessStatusInquiryArr = new JSONArray();
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
					object.put("b_no", list.subList(fromIdx, list.size()));
					Utils.callbusinessStatusInquiryApi(url,object,businessStatusInquiryArr);
				}else {
					object.put("b_no", list.subList(fromIdx, lastIdx));
					Utils.callbusinessStatusInquiryApi(url,object,businessStatusInquiryArr);
				}
				
			}
		}else {
			object.put("b_no", list);
			
			Utils.callbusinessStatusInquiryApi(url,object,businessStatusInquiryArr);
		}
		//추후에 따로 controller 기능으로 빼야함
		Utils.createExcel(businessStatusInquiryArr);
		
		resp.setData(businessStatusInquiryArr);
		
		return resp;
		
	
		
		
// RestTemplate 방식	
//		JSONParser parser = new JSONParser();
//		object.put("b_no", list); 
		
		/*RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<?> requestMessage = new HttpEntity<>(object,headers);
		
		HttpEntity<String> response = restTemplate.postForEntity(url, requestMessage, String.class);*/ 
		
	}

	
	
}
