package com.example.sebu.auth.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.sebu.auth.model.Member;

public interface MemberRepository extends CrudRepository<Member, Long>{

	Member findById(String id);
}
