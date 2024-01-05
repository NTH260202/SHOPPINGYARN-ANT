package com.artiexh.data.jpa.repository;

import com.artiexh.data.jpa.entity.NotificationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
	Page<NotificationEntity> findNotificationEntitiesByOwnerId(Long ownerId, Pageable pageable);

	@Modifying()
	@Query("update NotificationEntity notification set notification.readAt = instant where notification.id = :id")
	void markedAsRead(@Param("id") Long id);

	@Query(value = "select count(notification.id) from NotificationEntity notification where notification.owner.id = :userId and notification.readAt is null")
	int countUnreadMessages(@Param("userId") Long userId);
}
