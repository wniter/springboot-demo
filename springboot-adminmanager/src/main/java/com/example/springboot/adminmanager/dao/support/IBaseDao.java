package com.example.springboot.adminmanager.dao.support;

import com.example.springboot.adminmanager.entity.support.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface IBaseDao<T extends BaseEntity, ID extends Serializable>
        extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

}
