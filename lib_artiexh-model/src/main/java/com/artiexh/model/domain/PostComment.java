package com.artiexh.model.domain;

import lombok.*;

import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostComment {
	private Long id;

	private String content;

	private Post post;

	private User owner;

	private Instant createdDate;

	private Instant modifiedDate;
}
