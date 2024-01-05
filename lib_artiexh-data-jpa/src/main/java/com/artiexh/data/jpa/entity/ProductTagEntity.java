package com.artiexh.data.jpa.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "product_tag")
public class ProductTagEntity {

	@Id
	@Tsid
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "name", nullable = false, unique = true)
	private String name;

	@ManyToMany
	@JoinTable(name = "product_tag_mapping",
		joinColumns = @JoinColumn(name = "tag_id"),
		inverseJoinColumns = @JoinColumn(name = "product_code"))
	private Set<ProductInventoryEntity> products;

	@ManyToMany
	@JoinTable(name = "product_tag_mapping",
		joinColumns = @JoinColumn(name = "tag_id"),
		inverseJoinColumns = @JoinColumn(name = "product_code"))
	private Set<ProductInventoryEntity> productInventories;

	@Override
	public final boolean equals(Object o) {
		if (this == o) return true;
		if (o == null) return false;
		Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
		Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
		if (thisEffectiveClass != oEffectiveClass) return false;
		ProductTagEntity that = (ProductTagEntity) o;
		return getId() != null && Objects.equals(getId(), that.getId());
	}

	@Override
	public final int hashCode() {
		return getClass().hashCode();
	}
}