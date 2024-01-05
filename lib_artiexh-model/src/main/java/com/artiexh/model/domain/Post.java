package com.artiexh.model.domain;

import lombok.*;

import java.time.Instant;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Post {

	private Long id;

	private String description;

	private Artist owner;

	private Set<ProductAttach> attaches;

	private int likes;

	private int numOfComments;

	private Instant createdDate;

	private Instant modifiedDate;
}
