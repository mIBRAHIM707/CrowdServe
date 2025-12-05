// =============================================================================
// CONTROLLER BEST PRACTICES FOR NAVBAR INTEGRATION
// File: src/main/java/com/crowdserve/controller/ControllerNavbarExample.java
// =============================================================================

/*
 * This file demonstrates how to properly add navbar attributes to Spring MVC
 * controllers when rendering Thymeleaf templates that include the navbar fragment.
 * 
 * KEY REQUIREMENTS:
 * 1. Add four model attributes to EVERY controller method that returns a template
 * 2. Use @ControllerAdvice (optional) to inject common attributes globally
 * 3. Ensure NotificationService is available (via dependency injection)
 */

package com.crowdserve.controller;

import com.crowdserve.model.User;
import com.crowdserve.repository.UserRepository;
import com.crowdserve.service.NotificationService;
import com.crowdserve.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Optional;

// =============================================================================
// PATTERN #1: INDIVIDUAL CONTROLLER (Dashboard Example)
// =============================================================================

/**
 * Example: Adding navbar attributes to individual controller methods.
 * 
 * NAVBAR MODEL ATTRIBUTES:
 *   - activePage (String): Page identifier for highlighting (e.g., "dashboard")
 *   - pageTitle (String): Page heading (e.g., "CrowdServe Dashboard")
 *   - pageSubtitle (String): Subtitle under heading (e.g., "Open tasks feed...")
 *   - unreadCount (Long): Number of unread notifications for badge
 * 
 * RECOMMENDATION: Apply this pattern to EVERY controller method returning a template.
 */
@Controller
@RequestMapping("/dashboard")
public class DashboardControllerExample {

    // =========================================================================
    // REQUIRED DEPENDENCIES
    // =========================================================================
    
    private final TaskService taskService;
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public DashboardControllerExample(
        TaskService taskService,
        NotificationService notificationService,
        UserRepository userRepository
    ) {
        this.taskService = taskService;
        this.notificationService = notificationService;
        this.userRepository = userRepository;
    }

    // =========================================================================
    // PATTERN: Add navbar attributes to every template-returning method
    // =========================================================================

    @GetMapping
    public String dashboard(Principal principal, Model model) {
        
        // ─────────────────────────────────────────────────────────────────
        // PAGE-SPECIFIC CONTENT
        // ─────────────────────────────────────────────────────────────────
        model.addAttribute("tasks", taskService.getAllOpenTasks());

        // ─────────────────────────────────────────────────────────────────
        // NAVBAR ATTRIBUTES (REQUIRED FOR ALL TEMPLATE-RETURNING METHODS)
        // ─────────────────────────────────────────────────────────────────
        
        // 1. activePage: Unique identifier for this page (used to highlight link in navbar)
        //    Values should match hrefs in navbar fragment: dashboard, my-tasks, notifications, reports
        model.addAttribute("activePage", "dashboard");
        
        // 2. pageTitle: Main heading displayed in navbar (e.g., page name)
        model.addAttribute("pageTitle", "CrowdServe Dashboard");
        
        // 3. pageSubtitle: Secondary text under heading (e.g., page description)
        model.addAttribute("pageSubtitle", "Open tasks feed — find work or assign workers");
        
        // 4. unreadCount: Badge count on Notifications link
        //    (only displayed if > 0, so safe to pass 0 or null)
        if (principal != null) {
            Optional<User> userOpt = userRepository.findByEmail(principal.getName());
            if (userOpt.isPresent()) {
                long unreadCount = notificationService.countUnreadNotificationsForUser(userOpt.get());
                model.addAttribute("unreadCount", unreadCount);
            }
        } else {
            model.addAttribute("unreadCount", 0L);
        }

        return "dashboard";
    }
}


// =============================================================================
// PATTERN #2: GLOBAL CONTROLLER ADVICE (Recommended for larger projects)
// =============================================================================

/**
 * This pattern uses @ControllerAdvice to inject common attributes (like unreadCount)
 * to ALL controllers without repeating the code in every method.
 * 
 * BENEFITS:
 *   - DRY principle: Write unreadCount logic once
 *   - Consistency: All pages have unread badge without extra effort
 *   - Maintainability: Change notification logic in one place
 * 
 * LIMITATION:
 *   - Still need to add activePage, pageTitle, pageSubtitle per controller method
 *     (because these are page-specific)
 * 
 * USAGE:
 *   1. Create this class in a new package or add to existing @ControllerAdvice
 *   2. All @Controller classes automatically get these @ModelAttribute methods
 *   3. In each controller method, only add page-specific attributes
 */
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalNavbarAttributesAdvice {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public GlobalNavbarAttributesAdvice(
        NotificationService notificationService,
        UserRepository userRepository
    ) {
        this.notificationService = notificationService;
        this.userRepository = userRepository;
    }

    /**
     * Automatically add unreadCount to model for ALL controllers.
     * This runs BEFORE each controller method, making unreadCount available
     * in all templates without repeating the code.
     */
    @ModelAttribute("unreadCount")
    public long addUnreadCount(Principal principal) {
        if (principal == null) {
            return 0L;
        }

        Optional<User> user = userRepository.findByEmail(principal.getName());
        if (user.isEmpty()) {
            return 0L;
        }

        return notificationService.countUnreadNotificationsForUser(user.get());
    }
}

/**
 * With this @ControllerAdvice in place, controllers become simpler:
 * 
 * @GetMapping("/dashboard")
 * public String dashboard(Model model) {
 *     model.addAttribute("activePage", "dashboard");
 *     model.addAttribute("pageTitle", "CrowdServe Dashboard");
 *     model.addAttribute("pageSubtitle", "Open tasks feed...");
 *     // unreadCount is automatically added by GlobalNavbarAttributesAdvice
 *     return "dashboard";
 * }
 */


// =============================================================================
// PATTERN #3: MIXED APPROACH (Recommended for production)
// =============================================================================

/**
 * Combine both patterns:
 * - Use @ControllerAdvice for unreadCount (common to all pages)
 * - Add activePage/pageTitle/pageSubtitle in each controller method (page-specific)
 * 
 * This balances DRY with clarity and flexibility.
 */
@Controller
@RequestMapping("/my-tasks")
public class MyTasksControllerMixedExample {

    private final TaskService taskService;
    // No need to inject NotificationService — it's in the @ControllerAdvice

    public MyTasksControllerMixedExample(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public String myTasks(Principal principal, Model model) {
        
        // PAGE-SPECIFIC CONTENT
        model.addAttribute("postedTasks", taskService.getPostedTasks(principal.getName()));
        model.addAttribute("assignedTasks", taskService.getAssignedTasks(principal.getName()));

        // NAVBAR ATTRIBUTES (page-specific)
        model.addAttribute("activePage", "my-tasks");
        model.addAttribute("pageTitle", "My Tasks");
        model.addAttribute("pageSubtitle", "Posted tasks and assigned work");
        
        // unreadCount is added automatically by @ControllerAdvice
        // No need to add it here!

        return "my-tasks";
    }
}


// =============================================================================
// REFERENCE: ALL PAGES IN CROWDSERVE
// =============================================================================

/*
 * Pages that include the navbar fragment and require the 4 attributes:
 * 
 * 1. DASHBOARD
 *    - activePage: "dashboard"
 *    - pageTitle: "CrowdServe Dashboard"
 *    - pageSubtitle: "Open tasks feed — find work or assign workers"
 *    - Controller: DashboardController
 * 
 * 2. MY TASKS
 *    - activePage: "my-tasks"
 *    - pageTitle: "My Tasks"
 *    - pageSubtitle: "Posted tasks and assigned work"
 *    - Controller: MyTasksController
 * 
 * 3. NOTIFICATIONS
 *    - activePage: "notifications"
 *    - pageTitle: "Your Notifications"
 *    - pageSubtitle: "Stay updated with task activity"
 *    - Controller: NotificationController
 * 
 * 4. REPORTS
 *    - activePage: "reports"
 *    - pageTitle: "Reports Center"
 *    - pageSubtitle: "Download your platform analytics & task summaries"
 *    - Controller: ReportsController
 * 
 * PAGES NOT INCLUDING NAVBAR (public/login pages):
 *    - login.html, register.html, index.html
 *    - These do NOT include navbar (users not authenticated yet)
 *    - These do NOT need the 4 attributes
 */


// =============================================================================
// SNIPPET: Extracting active page name from request
// =============================================================================

/**
 * ALTERNATIVE APPROACH (if you want to derive activePage automatically):
 * 
 * Instead of hardcoding activePage in each method, extract it from the request path.
 * This reduces code duplication but is less explicit.
 * 
 * NOT RECOMMENDED for CrowdServe (too much complexity for simple benefit)
 * Provided as reference only.
 */
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;

public class AutoPageDetectionExample {

    /**
     * Automatically detect activePage from request URL.
     * Example: /dashboard → activePage = "dashboard"
     *          /my-tasks → activePage = "my-tasks"
     */
    private static String deriveActivePage(HttpServletRequest request) {
        String uri = request.getRequestURI();
        
        // Extract path after context root
        String path = uri.replaceAll("^/", "");
        
        // Split and get first segment
        String pageName = path.split("/")[0];
        
        return pageName;
    }

    // USAGE (not recommended for this project):
    // String activePage = deriveActivePage(request);
    // model.addAttribute("activePage", activePage);
}


// =============================================================================
// SNIPPET: Null-Safe Page Title Helper
// =============================================================================

/**
 * Helper method to provide sensible defaults if page title/subtitle are forgotten.
 * Reduces risk of blank headings in navbar.
 */
public class NavbarTitleHelper {

    public static String getPageTitle(String activePage) {
        return switch(activePage) {
            case "dashboard" -> "CrowdServe Dashboard";
            case "my-tasks" -> "My Tasks";
            case "notifications" -> "Your Notifications";
            case "reports" -> "Reports Center";
            default -> "CrowdServe";
        };
    }

    public static String getPageSubtitle(String activePage) {
        return switch(activePage) {
            case "dashboard" -> "Open tasks feed — find work or assign workers";
            case "my-tasks" -> "Posted tasks and assigned work";
            case "notifications" -> "Stay updated with task activity";
            case "reports" -> "Download your platform analytics & task summaries";
            default -> "Connecting tasks with workers";
        };
    }

    // USAGE:
    // String title = NavbarTitleHelper.getPageTitle("dashboard");
    // model.addAttribute("pageTitle", title);
}
