package com.artiexh.model.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomProduct {
	private Long id;
	private String name;
	private ProductVariant variant;
	private Artist artist;
	private Set<ImageSet> imageSet;
	private String combinationCode;
	private Long campaignLock;
	private String description;
	private Set<String> tags;
	private Media modelThumbnail;
	private ProductCategory category;
	private Integer maxItemPerOrder;
	private Instant createdDate;
	private Instant modifiedDate;
	private Set<ProductAttach> attaches;
}
