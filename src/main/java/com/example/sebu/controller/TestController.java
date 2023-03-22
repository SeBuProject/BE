package com.example.sebu.controller;

import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.sebu.service.RestTemplateService;
import com.example.sebu.service.TestService;

import lombok.extern.log4j.Log4j2;
@Log4j2
@RestController
public class TestController {
	
	  @Value("${api.servicekey}")
	  private String serviceKey;
	  @Autowired
	  TestService testService;
	  
	  @Autowired
	  RestTemplateService restTemplateService;

	  @PostMapping(value = "/home")
	  public String readExcel(@RequestParam("file") MultipartFile file, Model model)
	      throws IOException { // 2


	    String extension = FilenameUtils.getExtension(file.getOriginalFilename()); // 3

	    if (!extension.equals("xlsx") && !extension.equals("xls")) {
	      throw new IOException("엑셀파일만 업로드 해주세요.");
	    }

	    Workbook workbook = null;

	    if (extension.equals("xlsx")) {
	      workbook = new XSSFWorkbook(file.getInputStream());
	    } else if (extension.equals("xls")) {
	      workbook = new HSSFWorkbook(file.getInputStream());
	    }

	     Sheet worksheet = workbook.getSheetAt(0);

	    for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) { // 4

	      Row row = worksheet.getRow(i);


	      System.out.println(row.getCell(0).getStringCellValue());
	      System.out.println(row.getCell(1).getStringCellValue());

	    }

	    return "excelList";

	  }
	  
	  @GetMapping(value = "/apiTest")
	  public String apiTest() {
		 String param = "param";
		  String rsp = restTemplateService.callPostApi("https://api.odcloud.kr/api/nts-businessman/v1/status?serviceKey="+serviceKey+"", param);
		  
		  System.out.println(rsp);
	    return "excelList";

	  }
	  
	  @GetMapping(value = "/mybat")
	  public String myBat() {
		 
	     System.out.println(testService.getData());

	    return "excelList";

	  }
	}
	

