package com.xgy.elasticsearchjava.service;

import com.xgy.elasticsearchjava.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MyInsertThread implements Runnable {

    private Thread t;
    private String threadName;
    @Autowired
    private OperatorImpl operator;


    public MyInsertThread(String name) {
        threadName = name;
        System.out.println("Creating " + threadName);
    }

    public void run() {
        System.out.println("Running " + threadName);
        //插入100条数据
        int i = 0;
        while (i < 10) {
            Person person = new Person();
            operator.addPerson(person);
            i++;
        }
        System.out.println("Thread " + threadName + " exiting.");

    }

    public void start() {
        System.out.println("Starting " + threadName);
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }

    public static void main(String[] args) {
        // 开始时间
        Long begin = new Date().getTime();

        //插入1000条数据
        MyInsertThread threadq = new MyInsertThread("m1");
        threadq.start();
        // 结束时间
        Long end = new Date().getTime();
        // 耗时
        System.out.println("1000条数据插入花费时间 : " + (end - begin) / 1000 + " s" + "  插入完成");
    }
}
