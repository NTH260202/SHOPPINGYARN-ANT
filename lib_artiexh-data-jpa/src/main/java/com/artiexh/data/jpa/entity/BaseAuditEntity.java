package com.artiexh.data.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@SuperBuilder(toBuilder = true)
public class BaseAuditEntity {
	@Column(name = "created_date", nullable = false, updatable = false)
	@CreatedDate
	private Instant createdDate;

	@Column(name = "modified_date")
	@LastModifiedDate
	private Instant modifiedDate;

	@PrePersist
	public void onCreate() {
		this.createdDate = Instant.now();
		this.modifiedDate = Instant.now();
	}

	@PreUpdate
	public void onUpdate() {
		this.modifiedDate = Instant.now();
	}
}
