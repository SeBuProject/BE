package com.example.sebu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.sebu.mapper.TestMapper;
import com.example.sebu.message.ResponseMessage;

@Service
public class TestService {

	@Autowired
	TestMapper testMapper;
	public ResponseMessage getData() {
		System.out.println("service 들어옴");
		ResponseMessage resp = new ResponseMessage();
		resp.setCode("20000");
		resp.setMessage("성공");
		resp.setData(testMapper.getData());
		return resp;
	}
}
