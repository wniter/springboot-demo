package com.example.springboot.mail.service;


import com.example.springboot.mail.dto.Email;

public interface MailService {


    /**
     * 发送邮件
     *
     * @Author: lifq
     */
    public void sendMail(Email email);

}