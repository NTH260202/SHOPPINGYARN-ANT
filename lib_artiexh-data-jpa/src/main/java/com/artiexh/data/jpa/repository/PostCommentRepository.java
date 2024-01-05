package com.artiexh.data.jpa.repository;

import com.artiexh.data.jpa.entity.PostCommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostCommentRepository extends JpaRepository<PostCommentEntity, Long> {
	Page<PostCommentEntity> findAllByPostId(Long postId, Pageable pageable);
}
