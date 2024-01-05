package com.artiexh.data.jpa.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_history")
@EntityListeners(AuditingEntityListener.class)
public class ProductHistoryEntity extends BaseAuditEntity {
	@Id
	@Tsid
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "source_category", nullable = false)
	@Builder.Default
	private Byte sourceCategory = 0;

	@Column(name = "action", nullable = false)
	private Byte action;

	@Column(name = "source_id")
	private Long sourceId;

	@Column(name = "source_name")
	private String sourceName;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "product_history_id")
	private Set<ProductHistoryDetailEntity> productHistoryDetails = new HashSet<>();
}
