package com.xgy.elasticsearchjava.service;

import com.xgy.elasticsearchjava.entity.Person;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

@Component
public interface PersonRest extends ElasticsearchRepository<Person,Long> {
}
