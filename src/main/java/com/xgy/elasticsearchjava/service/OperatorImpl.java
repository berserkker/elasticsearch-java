package com.xgy.elasticsearchjava.service;

import com.xgy.elasticsearchjava.entity.Person;
import com.xgy.elasticsearchjava.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class OperatorImpl {
    @Autowired
    private PersonRest personRest;

    private Tools tools = new Tools();


    public void addPerson(Person person){
        person.setId(new Random().nextLong());
        person.setUser(tools.randomName());
        person.setAge(new Random().nextInt(150));
        person.setDesc(tools.getChinese());
        personRest.save(person);
    }
}
