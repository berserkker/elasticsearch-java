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
    public void insert(){
        int i =0;
        while (i<10){
            operatorImpl.addPerson(new Person());
            i++;
        }

    }
}
