package com.example.springboot.shiro.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springboot.shiro.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    User findByUserName(String username);
}
