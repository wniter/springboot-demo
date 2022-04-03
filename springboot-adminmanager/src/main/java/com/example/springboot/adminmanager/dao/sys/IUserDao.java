package com.example.springboot.adminmanager.dao.sys;

import com.example.springboot.adminmanager.dao.support.IBaseDao;
import com.example.springboot.adminmanager.entity.sys.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserDao extends IBaseDao<User, Integer> {

    User findByUserName(String username);

    Page<User> findAllByNickNameContaining(String searchText, Pageable pageable);

}
