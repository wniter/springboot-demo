package com.example.springboot.adminmanager.service.word;

import com.example.springboot.adminmanager.entity.word.Word;
import com.example.springboot.adminmanager.service.support.IBaseService;
import org.springframework.data.domain.Page;

/**
 * @author: ChenWenLong
 * @Date: 2018/9/30/030 15:43
 * @Description:
 */
public interface IWordService extends IBaseService<Word, Integer> {

    Word get(String word);

    Page<Word> query(final String word, final String means, Integer page, Integer size);
}
