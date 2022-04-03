package com.example.springboot.adminmanager.service.word.impl;

import com.example.springboot.adminmanager.dao.support.IBaseDao;
import com.example.springboot.adminmanager.dao.word.IMeansDao;
import com.example.springboot.adminmanager.entity.word.Means;
import com.example.springboot.adminmanager.service.support.impl.BaseServiceImpl;
import com.example.springboot.adminmanager.service.word.IMeansService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: ChenWenLong
 * @Date: 2018/9/30/030 16:43
 * @Description:
 */
@Service
public class MeansServiceImpl extends BaseServiceImpl<Means, Integer> implements IMeansService {

    @Autowired
    private IMeansDao meansDao;

    @Override
    public IBaseDao<Means, Integer> getBaseDao() {
        return this.meansDao;
    }
}
