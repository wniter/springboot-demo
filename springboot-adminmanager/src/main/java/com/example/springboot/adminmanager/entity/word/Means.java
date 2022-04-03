package com.example.springboot.adminmanager.entity.word;

import com.example.springboot.adminmanager.entity.support.BaseEntity;
import lombok.Data;

import javax.persistence.*;

/**
 * @author: ChenWenLong
 * @Date: 2018/9/30/030 10:57
 * @Description:
 */
@Entity
@Data
@Table(name = "word_means")
public class Means extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Integer id;


    private String means;

}
