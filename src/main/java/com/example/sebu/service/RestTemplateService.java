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
					callbusinessStatusInquiryApi(url,object,businessStatusInquiryArr);
				}else {
					object.put("b_no", list.subList(fromIdx, lastIdx));
					callbusinessStatusInquiryApi(url,object,businessStatusInquiryArr);
				}
				
			}
		}else {
			object.put("b_no", list);
			
			callbusinessStatusInquiryApi(url,object,businessStatusInquiryArr);
		}
		
		createExcel(businessStatusInquiryArr);
		
		
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

	private void callbusinessStatusInquiryApi(String url, JSONObject object,JSONArray businessStatusInquiryArr) {
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
				businessStatusInquiryArr.add(a);
			}
			
		}catch (Exception e) {
			System.out.println(e);
		}
		
	}
	
	private void createExcel(JSONArray businessStatusInquiryArr) {
		/**
         * excel sheet 생성
         */
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("사업자 상태조회"); // 엑셀 sheet 이름
        sheet.setDefaultColumnWidth(28); // 디폴트 너비 설정
        
        /**
         * header font style
         */
        XSSFFont headerXSSFFont = (XSSFFont) workbook.createFont();
        headerXSSFFont.setColor(new XSSFColor(new byte[]{(byte) 255, (byte) 255, (byte) 255}));

        /**
         * header cell style
         */
        XSSFCellStyle headerXssfCellStyle = (XSSFCellStyle) workbook.createCellStyle();
        
        // 테두리 설정
        headerXssfCellStyle.setBorderLeft(BorderStyle.THIN);
        headerXssfCellStyle.setBorderRight(BorderStyle.THIN);
        headerXssfCellStyle.setBorderTop(BorderStyle.THIN);
        headerXssfCellStyle.setBorderBottom(BorderStyle.THIN);

        // 배경 설정
        headerXssfCellStyle.setFillForegroundColor(new XSSFColor(new byte[]{(byte) 34, (byte) 37, (byte) 41}));
        headerXssfCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerXssfCellStyle.setFont(headerXSSFFont);
        
        /**
         * body cell style
         */
        XSSFCellStyle bodyXssfCellStyle = (XSSFCellStyle) workbook.createCellStyle();

        // 테두리 설정
        bodyXssfCellStyle.setBorderLeft(BorderStyle.THIN);
        bodyXssfCellStyle.setBorderRight(BorderStyle.THIN);
        bodyXssfCellStyle.setBorderTop(BorderStyle.THIN);
        bodyXssfCellStyle.setBorderBottom(BorderStyle.THIN);

        /**
         * header data
         */
        int rowCount = 0; // 데이터가 저장될 행
        String headerNames[] = new String[]{"사업자등록번호",//b_no
							        		"납세자상태",//b_stt
							        		"과세유형메세지",//tax_type
							        		"폐업일",//end_dt
							        		"단위과세전환폐업여부",//utcc_yn
							        		"최근과세유형전환일자",//tax_type_change_dt
							        		"세금계산서적용일자"};//invoice_apply_dt

        Row headerRow = null;
        Cell headerCell = null;

        headerRow = sheet.createRow(rowCount++);
        for(int i=0; i<headerNames.length; i++) {
            headerCell = headerRow.createCell(i);
            headerCell.setCellValue(headerNames[i]); // 데이터 추가
            headerCell.setCellStyle(headerXssfCellStyle); // 스타일 추가
        }
        
        /**
         * body data
         */
        String[] data;
        ArrayList list = new ArrayList<>();
        String b_no = "";
        String b_stt = "";
        String tax_type = "";
        String end_dt = "";
        String utcc_yn = "";
        String tax_type_change_dt = "";
        String invoice_apply_dt = "";
        
        Object[] bodyData =businessStatusInquiryArr.toArray();
        
		for(int i=0; i<businessStatusInquiryArr.size(); i++) {
				Map obj = (HashMap)businessStatusInquiryArr.get(i);
				b_no = String.valueOf(obj.get("b_no"));
				b_stt = String.valueOf(obj.get("b_stt"));
				tax_type = String.valueOf(obj.get("tax_type"));
				end_dt = String.valueOf(obj.get("end_dt"));
				utcc_yn = String.valueOf(obj.get("utcc_yn"));
				tax_type_change_dt = String.valueOf(obj.get("tax_type_change_dt"));
				invoice_apply_dt = String.valueOf(obj.get("invoice_apply_dt"));
				
				data = new String[] {b_no,b_stt,tax_type,end_dt,utcc_yn,tax_type_change_dt,invoice_apply_dt};
					
				list.add(data);
		}

        Row bodyRow = null;
        Cell bodyCell = null;

        for(Object bodyDatas : list) {
            bodyRow = sheet.createRow(rowCount++);
            data = (String[])bodyDatas;
            for(int i=0; i<data.length; i++) {
                bodyCell = bodyRow.createCell(i);
                bodyCell.setCellValue(data[i]); // 데이터 추가
                bodyCell.setCellStyle(bodyXssfCellStyle); // 스타일 추가
            }
        }
        
        /**
         * download
         */
        FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream("c:\\Temp\\사업자정보조회.xlsx");
			workbook.write(outputStream);
			workbook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

	}
	
}
