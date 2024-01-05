package com.artiexh.model.rest.product.request;

import com.artiexh.model.domain.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.opensearch.common.unit.Fuzziness;
import org.opensearch.data.client.orhlc.NativeSearchQueryBuilder;
import org.opensearch.index.query.BoolQueryBuilder;
import org.opensearch.index.query.QueryStringQueryBuilder;
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
public class SuggestionFilter {
	private String keyword;

	public Query getQuery() {
		var boolQuery = new BoolQueryBuilder()
			.should(new TermsQueryBuilder(
				"status",
				List.of(ProductStatus.AVAILABLE.getValue())
			))
			.minimumShouldMatch(1);

		var queryBuilder = new NativeSearchQueryBuilder().withFilter(boolQuery);

		if (StringUtils.hasText(keyword)) {
			Map<String, Float> fields = new HashMap<>();
			fields.put("name", 4f);
			fields.put("shop.shopName", 3f);
			fields.put("owner.displayName", 2f);
			fields.put("owner.username", 1f);
			new QueryStringQueryBuilder("*" + keyword + "*").fuzziness(Fuzziness.AUTO).fields(fields);
			queryBuilder.withQuery(new QueryStringQueryBuilder("*" + keyword + "*").fuzziness(Fuzziness.AUTO).fields(fields));
		}

		return queryBuilder.build();
	}
}
