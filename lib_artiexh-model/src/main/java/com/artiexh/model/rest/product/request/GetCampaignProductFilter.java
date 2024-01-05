package com.artiexh.model.rest.product.request;

import com.artiexh.model.domain.ProductStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.opensearch.data.client.orhlc.NativeSearchQueryBuilder;
import org.opensearch.index.query.BoolQueryBuilder;
import org.opensearch.index.query.RangeQueryBuilder;
import org.opensearch.index.query.TermQueryBuilder;
import org.opensearch.index.query.TermsQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class GetCampaignProductFilter {

	private BigDecimal minPrice;
	private BigDecimal maxPrice;
	private Float averageRate;
	private Integer categoryId;
	private String[] tagNames;
	@JsonIgnore
	private Long campaignId;

	public Query getQuery() {
		var boolQuery = new BoolQueryBuilder().should(new TermsQueryBuilder("status", List.of(ProductStatus.AVAILABLE.getValue()))).minimumShouldMatch(1);

		if (campaignId != null) {
			boolQuery.must(new TermQueryBuilder("campaign.id", campaignId));
		}

		if (minPrice != null) {
			boolQuery.must(new RangeQueryBuilder("price.amount").gte(minPrice));
		}
		if (maxPrice != null) {
			boolQuery.must(new RangeQueryBuilder("price.amount").lte(maxPrice));
		}
		if (averageRate != null) {
			boolQuery.must(new TermQueryBuilder("averageRate", averageRate));
		}
		if (categoryId != null) {
			boolQuery.must(new TermQueryBuilder("category.id", categoryId));
		}
		if (tagNames != null && tagNames.length > 0) {
			var tagQuery = new BoolQueryBuilder();
			for (String tagName : tagNames) {
				tagQuery.should(new TermQueryBuilder("tags", tagName));
			}
			boolQuery.filter(tagQuery.minimumShouldMatch(1));
		}

		var queryBuilder = new NativeSearchQueryBuilder().withFilter(boolQuery);

		return queryBuilder.build();
	}
}
