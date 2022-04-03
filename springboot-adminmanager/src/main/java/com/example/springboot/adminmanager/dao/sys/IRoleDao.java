package com.example.springboot.adminmanager.dao.sys;

import com.example.springboot.adminmanager.dao.support.IBaseDao;
import com.example.springboot.adminmanager.entity.sys.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoleDao extends IBaseDao<Role, Integer> {

    Page<Role> findAllByNameContainingOrDescriptionContaining(String searchText1, String searchText2, Pageable pageable);

}
