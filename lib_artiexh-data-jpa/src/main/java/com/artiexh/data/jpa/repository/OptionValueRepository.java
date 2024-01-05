package com.artiexh.data.jpa.repository;

import com.artiexh.data.jpa.entity.OptionValueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionValueRepository extends JpaRepository<OptionValueEntity, Long> {
}
