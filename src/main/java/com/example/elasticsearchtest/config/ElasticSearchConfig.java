package com.example.elasticsearchtest.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.RefreshPolicy;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@EnableElasticsearchRepositories
@Configuration
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {
    private static final String host = "fb9be65e4bc844a5b1bf20a5f52a1d2a.ap-northeast-2.aws.elastic-cloud.com:443";
    private static final String username = "elastic";
    private static final String password = "G2yx7VbzJCyf7fCIezDtStqk";

    @Override
    public RestHighLevelClient elasticsearchClient() {
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(host)
                .usingSsl()
                .withBasicAuth(username, password)
                .build();
        return RestClients.create(clientConfiguration).rest();
    }
    @Bean
    @Primary
    public ElasticsearchOperations elasticsearchTemplate1() {
        ElasticsearchRestTemplate elasticsearchRestTemplate = new ElasticsearchRestTemplate(elasticsearchClient());
        elasticsearchRestTemplate.setRefreshPolicy(RefreshPolicy.WAIT_UNTIL); // RefreshPolicy 설정

        return elasticsearchRestTemplate;
    }


}