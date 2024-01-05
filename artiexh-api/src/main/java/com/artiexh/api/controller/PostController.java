package com.artiexh.api.controller;

import com.artiexh.api.base.common.Endpoint;
import com.artiexh.api.service.PostService;
import com.artiexh.model.domain.Post;
import com.artiexh.model.domain.PostComment;
import com.artiexh.model.mapper.PostCommentMapper;
import com.artiexh.model.mapper.PostMapper;
import com.artiexh.model.rest.PageResponse;
import com.artiexh.model.rest.PaginationAndSortingRequest;
import com.artiexh.model.rest.post.PostCommentDetail;
import com.artiexh.model.rest.post.PostDetail;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@RestController
@RequestMapping(Endpoint.Post.ROOT)
public class PostController {
	private final PostService postService;
	private final PostMapper postMapper;
	private final PostCommentMapper postCommentMapper;

	@PostMapping
	public PostDetail create(Authentication authentication, @Valid @RequestBody PostDetail postDetail) {
		long userId = (long) authentication.getPrincipal();
		try {
			Post post = postMapper.detailToDomain(postDetail);
			post = postService.create(userId, post);
			return postMapper.domainToDetail(post);
		} catch (IllegalArgumentException | EntityNotFoundException exception) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
		}
	}

	@GetMapping
	public PageResponse<PostDetail> getAll(
		@RequestParam(required = false) String username,
		@Valid @ParameterObject PaginationAndSortingRequest pagination) {
		try {
			Page<Post> post = postService.getAllPost(username, pagination.getPageable());
			return new PageResponse<>(post.map(postMapper::domainToDetail));
		} catch (IllegalArgumentException exception) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
		}
	}

	@PostMapping(Endpoint.Post.DETAIL + Endpoint.Post.COMMENT)
	public PostCommentDetail create(
		@PathVariable("id") Long postId,
		Authentication authentication,
		@Valid @RequestBody PostCommentDetail postCommentDetail) {
		long userId = (long) authentication.getPrincipal();
		try {
			PostComment postComment = postCommentMapper.detailToDomain(postCommentDetail);
			postComment = postService.create(userId, postId, postComment);
			return postCommentMapper.domainToDetail(postComment);
		} catch (IllegalArgumentException | EntityNotFoundException exception) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
		}
	}

	@GetMapping(Endpoint.Post.DETAIL + Endpoint.Post.COMMENT)
	public PageResponse<PostCommentDetail> getAll(
		@PathVariable("id") Long postId,
		@Valid @ParameterObject PaginationAndSortingRequest pagination) {
		try {
			Page<PostComment> post = postService.getAllPostComment(postId, pagination.getPageable());
			return new PageResponse<>(post.map(postCommentMapper::domainToDetail));
		} catch (IllegalArgumentException exception) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
		}
	}
}
