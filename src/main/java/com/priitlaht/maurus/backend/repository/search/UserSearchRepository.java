package com.priitlaht.maurus.backend.repository.search;

import com.priitlaht.maurus.backend.domain.User;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserSearchRepository extends ElasticsearchRepository<User, Long> {
}
