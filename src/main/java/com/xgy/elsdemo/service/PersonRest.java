package com.xgy.elsdemo.service;

import com.xgy.elsdemo.entity.Person;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

@Component
public interface PersonRest extends ElasticsearchRepository<Person,Long> {
}
