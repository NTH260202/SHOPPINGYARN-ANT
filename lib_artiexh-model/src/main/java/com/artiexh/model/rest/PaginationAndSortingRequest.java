package com.artiexh.model.rest;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaginationAndSortingRequest {
	@Min(value = 1, message = "Page size must be greater than 0")
	private Integer pageSize = PaginationAndSortingConst.DEFAULT_PAGE_SIZE;
	@Min(value = 1, message = "Page number must be greater than 0")
	private Integer pageNumber = PaginationAndSortingConst.DEFAULT_PAGE_NUMBER;
	private String sortBy = null;
	private Sort.Direction sortDirection = PaginationAndSortingConst.DEFAULT_SORT_DIRECTION;

	public Pageable getPageable() {
		if (StringUtils.hasText(sortBy)) {
			return PageRequest.of(pageNumber - 1, pageSize, sortDirection, sortBy);
		} else {
			return PageRequest.of(pageNumber - 1, pageSize);
		}
	}
}

