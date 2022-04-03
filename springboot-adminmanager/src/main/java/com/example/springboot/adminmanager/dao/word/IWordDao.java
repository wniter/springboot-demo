package com.example.springboot.adminmanager.dao.word;

import com.example.springboot.adminmanager.dao.support.IBaseDao;
import com.example.springboot.adminmanager.entity.word.Word;
import org.springframework.stereotype.Repository;

/**
 * @author: ChenWenLong
 * @Date: 2018/9/30/030 15:38
 * @Description:
 */
@Repository
public interface IWordDao extends IBaseDao<Word, Integer> {
    /**
     * 单词查询
     *
     * @param word
     * @return
     */
    Word findByWord(String word);
}
