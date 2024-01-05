package com.artiexh.data.jpa.repository;

import com.artiexh.data.jpa.entity.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<CountryEntity, Short> {
}
