package com.example.book.restful;

import com.example.book.restful.controller.HelloworldController;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
//import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = BookRestfulApplication.class)
@WebAppConfiguration
class BookRestfulApplicationTests {
    private MockMvc mvc;
    @Before
    public void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(new HelloworldController()).build();
    }
    @Test
    void contextLoads() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/helloworld")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("Hello World")));
    }
}

