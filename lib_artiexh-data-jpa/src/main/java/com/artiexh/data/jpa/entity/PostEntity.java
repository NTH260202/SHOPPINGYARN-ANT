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

import java.util.Set;

@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "post")
@EntityListeners(AuditingEntityListener.class)
public class PostEntity extends BaseAuditEntity {
	@Id
	@Tsid
	private Long id;

	@Column(name = "description", length = 1000)
	private String description;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "owner_id", nullable = false)
	private ArtistEntity owner;

	@OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name = "post_id")
	private Set<ProductAttachEntity> attaches;

	@OneToMany(
		fetch = FetchType.LAZY,
		orphanRemoval = true,
		cascade = CascadeType.ALL,
		mappedBy = "post")
	private Set<PostCommentEntity> comments;

	@Column(name = "likes")
	private int likes;

	@Column(name = "num_of_comments")
	private int numOfComments;
}
