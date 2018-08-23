package com.xgy.elsdemo;

import com.xgy.elsdemo.entity.Person;
import com.xgy.elsdemo.service.OperatorImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElsdemoApplicationTests {

    @Autowired
    private OperatorImpl operator;

    @Test
    public void contextLoads() {

        //插入100条数据
        int i = 0;
        while (i < 100000) {
            Person person = new Person();
            operator.addPerson(person);
            i++;
        }
    }

}
