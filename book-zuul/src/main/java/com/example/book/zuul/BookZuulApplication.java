package com.example.book.zuul;

import com.example.book.zuul.filter.AccessFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@EnableZuulProxy
@SpringBootApplication
public class BookZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookZuulApplication.class, args);
    }


}
