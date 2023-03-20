package com.example.sebu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.sebu.mapper.TestMapper;

@Service
public class TestService {

	@Autowired
	TestMapper testMapper;
	public int getData() {
		System.out.println("service 들어옴");
		return testMapper.getData();
	}
}
