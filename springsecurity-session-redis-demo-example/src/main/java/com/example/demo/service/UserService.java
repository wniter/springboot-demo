package com.example.demo.service;

import demo.domain.User;
import demo.domain.UserCreateForm;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户服务网接口定义
 * 
 * @author jiekechoo
 *
 */
public interface UserService {

	User create(UserCreateForm form);

	User getUserByUsername(String username);

	Object uploadImage(MultipartFile file, HttpServletRequest request);

	User getCurrentUser();

	String getCurrentUsername();

	Object listAllUsers(Pageable p);

	Object getUserList(int current, int rowCount, String searchPhrase);

}
