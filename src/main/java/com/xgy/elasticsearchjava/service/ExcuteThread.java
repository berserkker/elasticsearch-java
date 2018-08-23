package com.xgy.elasticsearchjava.service;

import java.util.Date;

public class ExcuteThread {

    public void insertEls() {
        // 开始时间
        Long begin = new Date().getTime();

        //插入1000条数据
        for (int i = 0; i < 10; i++) {
            MyInsertThread threadq = new MyInsertThread("m"+i);
            threadq.start();
        }
        // 结束时间
        Long end = new Date().getTime();
        // 耗时
        System.out.println("1000条数据插入花费时间 : " + (end - begin) / 1000 + " s" + "  插入完成");
    }

    /*MyInsertThread getMyThread(int i) {

        ApplicationContext ac = new FileSystemXmlApplicationContext("application.properties");
        return (MyInsertThread) ac.getBean(new MyInsertThread("m" + i).getClass());
    }*/
}
