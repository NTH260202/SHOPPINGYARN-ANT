package com.artiexh.model.rest.shop;

import com.artiexh.model.domain.ProductStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.opensearch.common.unit.Fuzziness;
import org.opensearch.data.client.orhlc.NativeSearchQueryBuilder;
import org.opensearch.index.query.BoolQueryBuilder;
import org.opensearch.index.query.QueryStringQueryBuilder;
import org.opensearch.index.query.TermQueryBuilder;
import org.opensearch.index.query.TermsQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShopProductFilter {
	@JsonIgnore
	private Long shopId;
	private String productName;

	public Query getQuery() {
		var boolQuery = new BoolQueryBuilder().should(new TermsQueryBuilder("status", List.of(ProductStatus.AVAILABLE.getValue()))).minimumShouldMatch(1);
		boolQuery.must(new TermQueryBuilder("shop.id", shopId));

		var queryBuilder = new NativeSearchQueryBuilder().withFilter(boolQuery);

		if (StringUtils.hasText(productName)) {
			Map<String, Float> fields = new HashMap<>();
			fields.put("name", 4f);
			queryBuilder.withQuery(new QueryStringQueryBuilder("*" + productName + "*").fuzziness(Fuzziness.AUTO).fields(fields));
		}

		return queryBuilder.build();
	}
}
