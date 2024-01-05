package com.artiexh.model.rest;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageResponse<T> {
	private Integer page;
	private Integer pageSize;
	private Long totalSize;
	private Integer totalPage;
	private List<T> items;

	public PageResponse(Page<T> page) {
		if (page == null) {
			return;
		}
		this.page = page.getNumber() + 1;
		this.pageSize = page.getSize();
		this.totalSize = page.getTotalElements();
		this.totalPage = page.getTotalPages();
		this.items = page.getContent();
	}

}

