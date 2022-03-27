package com.example.spring.test.sang;

import org.springframework.stereotype.Service;

@Service
public class UserFuctionService {
    public String sayHello(String word) {return "你好" + word + "!";
    }
}
