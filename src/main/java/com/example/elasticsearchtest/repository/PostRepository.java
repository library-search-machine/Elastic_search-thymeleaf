package com.example.elasticsearchtest.repository;

import com.example.elasticsearchtest.domain.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post,Long> {
}
