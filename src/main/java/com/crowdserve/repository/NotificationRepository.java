package com.crowdserve.repository;

import com.crowdserve.model.Notification;
import com.crowdserve.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Notification entity database operations.
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    /**
     * Finds all notifications for a specific user, ordered by creation date descending.
     *
     * @param user the user whose notifications to retrieve
     * @return list of notifications for the user
     */
    List<Notification> findByUserOrderByCreatedAtDesc(User user);

    /**
     * Finds all unread notifications for a specific user.
     *
     * @param user the user whose unread notifications to retrieve
     * @return list of unread notifications
     */
    List<Notification> findByUserAndIsReadFalseOrderByCreatedAtDesc(User user);

    /**
     * Counts unread notifications for a user.
     *
     * @param user the user
     * @return count of unread notifications
     */
    long countByUserAndIsReadFalse(User user);
}
