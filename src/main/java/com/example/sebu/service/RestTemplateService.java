package com.example.sebu.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
		//데이터 변환 변수
        String b_no = "";
        String end_dt = "";
        String utcc_yn = "";
        String tax_type_change_dt = "";
        String invoice_apply_dt = "";
		
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
		
		//데이터 변환
		for(int i=0; i<businessStatusInquiryArr.size(); i++) {
			Map obj = (HashMap)businessStatusInquiryArr.get(i);
			
			b_no = String.valueOf(obj.get("b_no"));
			//사업자번호 - 추가
			StringBuilder sb = new StringBuilder();
			sb.append(b_no);
			b_no=sb.insert(3, "-").insert(6, "-").toString();
			
			
			obj.put("b_no", b_no);
			
			end_dt = String.valueOf(obj.get("end_dt"));
			if(Utils.isEmpty(end_dt)) {
				if(end_dt.length() == 8) {
					sb = new StringBuilder();
					sb.append(end_dt);
					obj.put("end_dt", sb.insert(4, "-").insert(7, "-"));
				}
			}
	
			utcc_yn = String.valueOf(obj.get("utcc_yn"));
			
			obj.put("utcc_yn", "Y".equals(utcc_yn) ? "여":"부");
		
			tax_type_change_dt = String.valueOf(obj.get("tax_type_change_dt"));
			if(Utils.isEmpty(tax_type_change_dt)) {
				if(tax_type_change_dt.length() == 8) {
					sb = new StringBuilder();
					sb.append(tax_type_change_dt);
					obj.put("tax_type_change_dt", sb.insert(4, "-").insert(7, "-"));
				}
			}
			
			invoice_apply_dt = String.valueOf(obj.get("invoice_apply_dt"));
			if(Utils.isEmpty(invoice_apply_dt)) {
				if(invoice_apply_dt.length() == 8) {
					sb = new StringBuilder();
					sb.append(invoice_apply_dt);
					obj.put("invoice_apply_dt", sb.insert(4, "-").insert(7, "-"));
				}
			}
	}
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
