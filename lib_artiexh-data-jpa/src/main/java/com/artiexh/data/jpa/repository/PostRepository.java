package com.artiexh.data.jpa.repository;

import com.artiexh.data.jpa.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {
	Page<PostEntity> findAllByOwnerUsername(String username, Pageable pageable);

	@Modifying
	@Query("update PostEntity post set post.numOfComments = post.numOfComments + 1 where post.id = :postId")
	void updateNumOfComments(@Param("postId") Long postId);
}
