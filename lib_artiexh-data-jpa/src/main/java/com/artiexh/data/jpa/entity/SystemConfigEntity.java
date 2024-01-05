package com.artiexh.data.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "system_config")
@EntityListeners(AuditingEntityListener.class)
public class SystemConfigEntity extends BaseAuditEntity {
	@Id
	@Column(name = "`key`", nullable = false, length = 25)
	private String key;

	@Lob
	@Column(name = "value")
	private String value;

	@Column(name = "updated_by")
	private String updatedBy;
}