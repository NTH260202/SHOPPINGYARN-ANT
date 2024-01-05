package com.artiexh.model.rest.product.request;

import com.artiexh.model.domain.CampaignType;
import com.artiexh.model.domain.ProductStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.opensearch.common.unit.Fuzziness;
import org.opensearch.data.client.orhlc.NativeSearchQueryBuilder;
import org.opensearch.index.query.*;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetAllProductFilter {
	private String keyword;
	private BigDecimal minPrice;
	private BigDecimal maxPrice;
	private Float averageRate;
	private Integer provinceId;
	private Integer districtId;
	private Integer wardId;
	private Integer categoryId;
	private String[] tagNames;
	@JsonIgnore
	private String username;

	public Query getQuery() {
		var publicProductQuery = new BoolQueryBuilder()
			.should(new TermsQueryBuilder("campaign.type", List.of(CampaignType.PUBLIC.getByteValue(), CampaignType.SHARE.getByteValue()))).minimumShouldMatch(1);
		var activeProductQuery = new BoolQueryBuilder()
			.should(new TermsQueryBuilder("status", List.of( ProductStatus.AVAILABLE.getByteValue()))).minimumShouldMatch(1);
		var boolQuery = new BoolQueryBuilder();
		boolQuery.must(publicProductQuery);
		boolQuery.must(activeProductQuery);
		if (username != null) {
			boolQuery.must(new TermQueryBuilder("owner.username", username));
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
		if (wardId != null) {
			boolQuery.must(new TermQueryBuilder("shop.shopWard.id", wardId));
		}
		if (districtId != null) {
			boolQuery.must(new TermQueryBuilder("shop.shopWard.district.id", districtId));
		}
		if (provinceId != null) {
			boolQuery.must(new TermQueryBuilder("shop.shopWard.district.province.id", provinceId));
		}
		if (tagNames != null && tagNames.length > 0) {
			var tagQuery = new BoolQueryBuilder();
			for (String tagName : tagNames) {
				tagQuery.should(new TermQueryBuilder("tags", tagName));
			}
			boolQuery.filter(tagQuery.minimumShouldMatch(1));
		}

		var dateQuery = new BoolQueryBuilder()
			.must(new RangeQueryBuilder("campaign.from").lte(Instant.now()))
			.must(new RangeQueryBuilder("campaign.to").gte(Instant.now()));

		var publishedProductFilter = new BoolQueryBuilder()
			.should(new TermQueryBuilder("campaign.isPrePublished", true))
			.should(dateQuery)
			.minimumShouldMatch(1);

		var queryBuilder = new NativeSearchQueryBuilder().withQuery(boolQuery).withFilter(publishedProductFilter);

		if (StringUtils.hasText(keyword)) {
			Map<String, Float> fields = new HashMap<>();
			fields.put("name", 3f);
			fields.put("owner.displayName", 2f);
			fields.put("owner.username", 1f);
			new QueryStringQueryBuilder("*" + keyword + "*").fuzziness(Fuzziness.AUTO).fields(fields);
			queryBuilder.withQuery(new QueryStringQueryBuilder("*" + keyword + "*").fuzziness(Fuzziness.AUTO).fields(fields));
		}

		return queryBuilder.build();
	}

	public Query matchAllQuery() {
		var queryBuilder = new NativeSearchQueryBuilder().withQuery(new MatchAllQueryBuilder());
		return queryBuilder.build();
	}
}
