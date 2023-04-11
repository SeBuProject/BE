package com.example.sebu.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
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
import org.apache.xmlbeans.ResourceLoader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Utils {
	
	private static WebDriver driver;
	
	@Value("${homtax.pwd}")
	private String hardCodingPwd;
	
	@Autowired
	ResourceLoader resourceLoader;
	
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
				//사업자번호 - 추가
				StringBuilder sb = new StringBuilder();
				sb.append(b_no);
				b_no=sb.insert(3, "-").insert(6, "-").toString();
				
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
	
	public static void callHometaxLogin(){
		JSONParser parser = new JSONParser();
		MediaType mediaType = MediaType.parse("application/json");
		OkHttpClient client = new OkHttpClient().newBuilder().build();
        HttpClient htpclient = new HttpClient();
        GetMethod instance = new GetMethod("https://www.hometax.go.kr/wqAction.do?actionId=ATXPPZXA001R01&screenId=UTXPPABA01");
        instance.setFollowRedirects(false);
        String wmonId = "";
        String TxppSessionId = "";
        try {
			int statusCode = htpclient.executeMethod(instance);
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        Header[] headers = instance.getResponseHeaders();
        for(Header a : headers) {
        	if(a.getName().equals("Set-Cookie") && a.getValue().contains("WMONID")) {
        		wmonId =a.getValue().substring(7,a.getValue().indexOf(";"));
        	}else if(a.getName().equals("Set-Cookie") && a.getValue().contains("TXPPsessionID")) {
        		TxppSessionId =a.getValue().substring(14,a.getValue().indexOf(";"));
        	}
        }

		Request req = new Request.Builder()
				.addHeader("Content-Type", "application/json")
				.addHeader("Host", ".hometax.go.kr")
				.url("https://www.hometax.go.kr/wqAction.do?actionId=ATXPPZXA001R01&screenId=UTXPPABA01")
				.build();
		try {
			Response rsp = client.newCall(req).execute();
			String res = rsp.body().string();
			Gson gson = new Gson();
			Map resMap = gson.fromJson(res, HashMap.class);
		//	JSONObject objResp =(JSONObject)parser.parse(rsp.body().string());
//			
//			JSONArray array = (JSONArray)objResp.get("pkcEncSsn");
//			for(Object a : array) {
//				System.out.println(a.toString());
//			}
			System.out.println(resMap.get("pkcEncSsn"));
			System.out.println("wmonId :"+wmonId);
			System.out.println("TxppSessionId :"+TxppSessionId);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public static void seleniumTest(String pwd) {
		
		String url = "https://www.hometax.go.kr/websquare/websquare.html?w2xPath=/ui/pp/index.xml";
		
		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");

		try {
			
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--remote-allow-origins=*");
			options.addArguments("--headless");
			
			driver = new ChromeDriver(options);
			
			driver.get(url);
			
			/*Actions action = new Actions(driver);
			WebElement we = driver.findElement(By.cssSelector("#textbox81212923"));
			action.moveToElement(we).build().perform();
			
			Thread.sleep(2000);
			WebElement ele = driver.findElement(By.cssSelector("#menuAtag_0104030000"));
			ele.click();
			
			Thread.sleep(2000);
			WebElement ele2 = driver.findElement(By.cssSelector("#menuAtag_0104030100"));
			ele2.click();*/
			WebElement loginElement = driver.findElement(By.cssSelector("#group88615548"));
			
			System.out.println(loginElement.getText());
			Thread.sleep(2000);
			
			loginElement.click();
			
			Thread.sleep(3000);
			WebElement frame = driver.findElement(By.id("txppIframe"));
			driver.switchTo().frame(frame);
			
			
			WebElement publicCertBtn = driver.findElement(By.id("anchor22"));
			
			Thread.sleep(3000);
			
			publicCertBtn.click();
			
			frame = driver.findElement(By.id("dscert"));
			
			driver.switchTo().frame(frame);
			
			
			Thread.sleep(3000);
			
			WebElement certPwEle = driver.findElement(By.cssSelector("#input_cert_pw"));
			
			certPwEle.sendKeys(pwd);
			
			
			
			WebElement certLoginEle = driver.findElement(By.cssSelector("#btn_confirm_iframe"));
			
			certLoginEle.click(); 
			
			Thread.sleep(5000);
			
			WebElement selectBar = driver.findElement(By.cssSelector("#textbox81212923"));
			
			selectBar.click();
			
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

