package com.xgy.elasticsearchjava;

import com.xgy.elasticsearchjava.config.AsyncTaskConfig;
import com.xgy.elasticsearchjava.entity.Person;
import com.xgy.elasticsearchjava.service.AsyncTaskService;
import com.xgy.elasticsearchjava.service.OperatorImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElsdemoApplication.class)
@ComponentScan("com.xgy.elasticsearchjava")
public class ElsdemoApplicationTests {
    /*@Autowired
    private AsyncTaskService asyncTaskService;*/
    @Autowired
    private OperatorImpl operatorImpl;

    @Test
    public void testVoid() {
        /*AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AsyncTaskConfig.class);
        AsyncTaskService asyncTaskService = context.getBean(AsyncTaskService.class);

         //创建了5个线程
        for (int i = 1; i <= 5; i++) {
            asyncTaskService.insert(i);
        }

        context.close();*/
    }

    /*@Test
    public void insertPerson(){
        asyncTaskService.insert(1);
    }*/
}