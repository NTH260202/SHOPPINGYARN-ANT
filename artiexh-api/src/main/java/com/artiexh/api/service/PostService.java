package com.artiexh.api.service;

import com.artiexh.model.domain.Post;
import com.artiexh.model.domain.PostComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
	Post create(Long userId, Post post);

	Page<Post> getAllPost(String username, Pageable pageable);

	Page<Post> getAllPost(Long artistId, Pageable pageable);

	PostComment create(Long userId, Long postId, PostComment post);

	Page<PostComment> getAllPostComment(Long postId, Pageable pageable);
}
