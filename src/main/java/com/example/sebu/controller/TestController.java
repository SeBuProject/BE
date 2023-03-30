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

import lombok.extern.log4j.Log4j2;
@Log4j2
@RestController
@RequestMapping("/api/v1/contents")
public class TestController {
	
	  @Value("${api.servicekey}")
	  private String serviceKey;
	  @Autowired
	  TestService testService;
	  
	  @Autowired
	  RestTemplateService restTemplateService;
	  
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
	  
	  @GetMapping(value = "/apiTest")
	  public String apiTest() {
		 String param = "param";
		 restTemplateService.businessStatusInquiry("https://api.odcloud.kr/api/nts-businessman/v1/status?serviceKey="+serviceKey+"", param);
		  
	    return "excelList";

	  }
	  
	  @GetMapping(value = "/mybat")
	  public String myBat() {
		 
	     System.out.println(testService.getData());

	    return "excelList";

	  }
	}
	

