package com.artiexh.data.jpa.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "post_comment")
@EntityListeners(AuditingEntityListener.class)
public class PostCommentEntity extends BaseAuditEntity {
	@Id
	@Tsid
	private Long id;

	@Column(name = "content", length = 50)
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	private PostEntity post;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "owner_id", nullable = false)
	private UserEntity owner;
}
