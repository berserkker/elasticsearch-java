package com.xgy.elasticsearchjava;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElsdemoApplicationTests {

    /*@Test
    public void testVoid() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AsyncTaskConfig.class);
        AsyncTaskService asyncTaskService = context.getBean(AsyncTaskService.class);

        *//* 创建了3个线程 *//*
        for (int i = 1; i <= 5; i++) {
            asyncTaskService.insert();
        }

        context.close();
    }*/
}