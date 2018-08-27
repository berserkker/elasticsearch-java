package com.xgy.elasticsearchjava;

import com.xgy.elasticsearchjava.config.AsyncTaskConfig;
import com.xgy.elasticsearchjava.service.AsyncTaskService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@SpringBootApplication
@EnableAsync
public class ElsdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElsdemoApplication.class, args);
    }

    /*private static void testVoid() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AsyncTaskConfig.class);
        AsyncTaskService asyncTaskService = context.getBean(AsyncTaskService.class);

        // 创建了20个线程
        for (int i = 1; i <= 20; i++) {
            asyncTaskService.insert();
        }

        context.close();
    }*/

}
