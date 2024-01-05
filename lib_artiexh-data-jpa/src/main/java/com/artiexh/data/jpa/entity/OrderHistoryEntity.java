package com.artiexh.data.jpa.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "order_history")
public class OrderHistoryEntity {
	@EmbeddedId
	private OrderHistoryId id;

	@Builder.Default
	@Column(name = "datetime", nullable = false)
	private Instant datetime = Instant.now();

	@Column(name = "message")
	private String message;

	@Column(name = "updated_by")
	private String updatedBy;

}