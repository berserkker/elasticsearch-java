package com.xgy.elasticsearchjava.service;

import com.xgy.elasticsearchjava.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
//任务执行类
public class AsyncTaskService {
    @Autowired
    private OperatorImpl operatorImpl;

    @Async
    //异步方法
    public void insert(int i){
        System.out.println("开始插入--------"+i);
        int k =0;
        while (k<100){
            operatorImpl.addPerson(new Person());
            k++;
        }

    }
}
