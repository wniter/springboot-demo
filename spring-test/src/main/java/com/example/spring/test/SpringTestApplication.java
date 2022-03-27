package com.example.spring.test;

import com.example.spring.test.sang.AnnotationService;
import com.example.spring.test.sang.MethodService;
import com.example.spring.test.sang.UserFuctionService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SpringTestApplication {

    public static void main(String[] args) {
       ApplicationContext applicationContext = SpringApplication.run(SpringTestApplication.class, args);


//        UserFuctionService bean = applicationContext.getBean(UserFuctionService.class);
//        System.out.println(bean.sayHello("sang"));
//        AnnotationService bean = applicationContext.getBean(AnnotationService.class);
//        bean.add1();
//        bean.add2();
//        bean.add3();
//        MethodService service = applicationContext.getBean(MethodService.class);
//        service.add();
//
    }

}
