package com.crowdserve.repository;

import com.crowdserve.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // Find notifications for a user ordered by timestamp desc
    List<Notification> findByUserIdOrderByTimestampDesc(Long userId);

    // Find unread notifications for a user
    List<Notification> findByUserIdAndReadFalse(Long userId);
}
