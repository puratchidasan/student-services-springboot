package com.bnpam.cucumber.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.in28minutes.springboot.StudentServicesApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StudentServicesApplication.class)
public class SpringContextTest {

    @Test
    public void whenSpringContextIsBootstrapped_thenNoExceptions() {
    }
}