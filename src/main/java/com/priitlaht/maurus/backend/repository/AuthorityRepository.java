package com.priitlaht.maurus.backend.repository;

import com.priitlaht.maurus.backend.domain.Authority;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
