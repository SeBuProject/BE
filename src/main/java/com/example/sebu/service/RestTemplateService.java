package com.example.sebu.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
//excel
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

//import org.springframework.http.MediaType;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//utils
import com.example.sebu.utils.Utils;

@Service
public class RestTemplateService {

	public void businessStatusInquiry(String url, Object param) {
		//restTemplate 방식은 계속 인증키 에러뜸. okHttp3로하면 정상으로 가져옴
		
		//변수선언
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
		
		Utils.createExcel(businessStatusInquiryArr);
		
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
