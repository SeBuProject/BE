package com.example.sebu.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.sebu.message.ResponseMessage;
import com.example.sebu.service.RestTemplateService;
import com.example.sebu.service.TestService;
import com.example.sebu.utils.Utils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
@Tag(name = "세무사를부탁해(세부) 기능 API", description = "사업자상태 조회, 크롤링, mybatis 등 API")
@Log4j2
@RestController
@RequestMapping("/api/v1/contents")
public class TestController {
	
	  @Value("${api.servicekey}")
	  private String serviceKey;
	  @Value("${homtax.pwd}")
	  private String pwd;
	  @Autowired
	  TestService testService;
	  
	  @Autowired
	  RestTemplateService restTemplateService;
	  
	  @Operation(summary = "사업자상태조회", description = "수백, 수천개의 사업자번호가 있는 엑셀로 사업자상태를 조회")
	  @Parameter(name = "file", description = "엑셀형식파일")
	  @PostMapping(value = "/businessStatusInqr")
	  public ResponseMessage readExcel(@RequestParam("file") MultipartFile file, Model model)
	      throws IOException { // 2
		
		ResponseMessage resp = new ResponseMessage();
		ArrayList<String> param = new ArrayList<String>();
		String cellVal = "";
		String bNoPattern = "^\\d{3}-\\d{2}-\\d{5}$";
	    String extension = FilenameUtils.getExtension(file.getOriginalFilename()); // 3

	    if (!extension.equals("xlsx") && !extension.equals("xls")) {
	    	resp.setCode("40000");
	    	resp.setMessage("파일 확장자 오류");
	    	return resp;
	    }

	    Workbook workbook = null;

	    if (extension.equals("xlsx")) {
	      workbook = new XSSFWorkbook(file.getInputStream());
	    } else if (extension.equals("xls")) {
	      workbook = new HSSFWorkbook(file.getInputStream());
	    }

	     Sheet worksheet = workbook.getSheetAt(0);
	    int cellType = 0;
	    for (int i = 0; i < worksheet.getPhysicalNumberOfRows(); i++) { 
	    	Row row = worksheet.getRow(i);
			      Iterator<Cell> iter = row.iterator();
			      while(iter.hasNext()) {
			    	  Cell cell = iter.next();
			    	  cellType = cell.getCellType();
			    	  if(cellType == 0) {
			    		  cellVal = String.valueOf(((Double)cell.getNumericCellValue()).intValue());
			    	  }else if(cellType == 1) {
			    		  cellVal = cell.getStringCellValue();
			    	  }else {
			    		  cellVal = "";
			    	  }
			    	  
			    	  if(cellVal != "" && cellVal != null) {
			    		  if(cellVal.length() == 12 && cellVal.matches(bNoPattern)) {
			    			  cellVal = cellVal.replaceAll("-", "");
					    	  param.add(cellVal);
			    		  }
			    	  }
			      }
			      /*if(cellVal != "") {
			    	  
			    	  cellVal = cellVal.replaceAll("-", "");
			    	  param.add(cellVal);
			      	}*/
			      
			      //System.out.println(param);
	    	}   
	    
	    resp = restTemplateService.businessStatusInquiry("https://api.odcloud.kr/api/nts-businessman/v1/status?serviceKey="+serviceKey+"", param);
	    
	    return resp;

	  }
	  @Operation(summary = "홈택스크롤링", description = "셀레니움을 이용한 홈택스 크롤링")
	  @Parameter(name = "none", description = "파라미터가 필요없는 api 입니다")
	  @GetMapping(value = "/apiTest")
	  public void apiTest() {
		  
		  Utils.seleniumTest(pwd);
		// Utils.callHometaxLogin();
		  

	  }
	  @Operation(summary = "DB조회테스트", description = "DB조회 테스트 API")
	  @Parameter(name = "none", description = "파라미터가 필요없는 api 입니다")
	  @GetMapping(value = "/mybat")
	  public ResponseMessage myBat() {
		 
	     ResponseMessage rsp =testService.getData();

	    return rsp;

	  }
	}
	

