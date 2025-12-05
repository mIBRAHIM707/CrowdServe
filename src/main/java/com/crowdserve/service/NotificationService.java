package com.crowdserve.service;

import com.crowdserve.model.User;
import com.crowdserve.repository.NotificationRepository;
import org.springframework.stereotype.Service;

/**
 * Service for notification-related operations.
 */
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    /**
     * Count unread notifications for a specific user.
     *
     * @param user the user to count unread notifications for
     * @return the count of unread notifications
     */
    public long countUnreadNotificationsForUser(User user) {
        if (user == null) {
            return 0;
        }
        return notificationRepository.findByUserIdAndReadFalse(user.getId()).size();
    }

    /**
     * Count unread notifications for a user by ID.
     *
     * @param userId the user ID
     * @return the count of unread notifications
     */
    public long countUnreadNotificationsForUserId(Long userId) {
        if (userId == null) {
            return 0;
        }
        return notificationRepository.findByUserIdAndReadFalse(userId).size();
    }
}
