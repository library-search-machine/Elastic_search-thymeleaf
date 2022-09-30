package com.example.elasticsearchtest;


import feign.Response;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import org.hibernate.bytecode.internal.bytebuddy.HibernateMethodLookupDispatcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import java.net.URI;
import java.net.URISyntaxException;


@SpringBootTest
class ElasticsearchtestApplicationTests {


    @Test
    void contextLoads1() {

        PageRequest pageRequest = PageRequest.of(0, 50);
        FieldSortBuilder idSortBuilder = SortBuilders.fieldSort("id").order(SortOrder.ASC);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                 .should(QueryBuilders.matchQuery("balance", "3607"))
                 .should(QueryBuilders.termQuery("balance", "3607"));

        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder)
                .withPageable(pageRequest)
                .withSort(idSortBuilder)
                .build();
        String query = nativeSearchQuery.getQuery().toString();
        System.out.println(query);
    }


//    List<Person> content = personRepository.search(nativeSearchQuery).getContent();


}
