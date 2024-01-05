package com.artiexh.data.jpa.entity;

import com.artiexh.data.jpa.entity.embededmodel.ReferenceData;
import io.hypersistence.utils.hibernate.id.Tsid;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "notification")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@EntityListeners(AuditingEntityListener.class)
public class NotificationEntity extends BaseAuditEntity {
	@Id
	@Tsid
	@Column(name = "id", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "owner_id")
	private AccountEntity owner;

	@Column(name = "title")
	private String title;

	@Column(name = "content")
	private String content;

	@Column(name = "read_at")
	private Instant readAt;

	@Column(name = "type")
	private Byte type;

	@Type(JsonType.class)
	@Column(name = "reference_data", columnDefinition = "json", nullable = false)
	private ReferenceData referenceData;

}
