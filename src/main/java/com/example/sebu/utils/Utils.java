package com.example.sebu.utils;

import java.io.File;
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
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Utils {
	
	public static void callbusinessStatusInquiryApi(String url, JSONObject object,JSONArray businessStatusInquiryArr) {
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
	
	public static void createExcel(JSONArray businessStatusInquiryArr) {
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
			int fileCnt = 0;
			String body = "";
			String ext = "";
			
			String location = System.getProperty("user.home")+ File.separator + "Downloads"+ File.separator +"사업자정보조회.xlsx";
			File file = new File(location);
			if(!file.createNewFile()) {
				if(location.lastIndexOf(".") != -1) {
					body = location.substring(0,location.lastIndexOf("."));
					ext = location.substring(location.lastIndexOf("."));
				}else {
					body = location;
				}
				
				while(fileCnt <9999) {
					fileCnt++;
					file = new File(body +"(" +fileCnt +")"+ext);
					if(file.createNewFile()) {
						location = body +"(" +fileCnt +")"+ext ;
						break;
					}
				}
			}
			outputStream = new FileOutputStream(location);
			workbook.write(outputStream);
			workbook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

	}
}
