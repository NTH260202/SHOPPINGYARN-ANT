package com.artiexh.data.jpa.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_address")
public class UserAddressEntity {
	@Id
	@Tsid
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "address", nullable = false)
	private String address;

	@Column(name = "type", nullable = false)
	private Byte type;

	@Builder.Default
	@Column(name = "is_default", nullable = false)
	private Boolean isDefault = false;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;

	@Column(name = "phone", nullable = false, length = 15)
	private String phone;

	@Column(name = "receiver_name", nullable = false)
	private String receiverName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ward_id")
	private WardEntity ward;

}