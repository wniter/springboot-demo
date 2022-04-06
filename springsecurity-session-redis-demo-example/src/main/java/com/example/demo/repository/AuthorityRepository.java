package com.example.demo.repository;

import demo.domain.Authority;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RestResource(exported = false)
public interface AuthorityRepository extends CrudRepository<Authority, Long> {

	List<Authority> findByUsername(String username);

}
