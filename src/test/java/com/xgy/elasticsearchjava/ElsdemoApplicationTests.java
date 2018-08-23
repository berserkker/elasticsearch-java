package com.xgy.elasticsearchjava;

import com.xgy.elasticsearchjava.service.ExcuteThread;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElsdemoApplicationTests {

    @Autowired
    private ExcuteThread excuteThread;

    @Test
    public void contextLoads() {

        excuteThread.insertEls();
    }
}