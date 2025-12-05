# CrowdServe Navbar: Architecture & Visual Guide

**Date**: December 5, 2025

---

## 📐 Component Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                      Spring Boot Application                         │
├─────────────────────────────────────────────────────────────────────┤
│                                                                       │
│  ┌──────────────────────────────────────────────────────────────┐   │
│  │ Request: GET /dashboard (user authenticated)                 │   │
│  └────────────────────┬─────────────────────────────────────────┘   │
│                       │                                               │
│  ┌────────────────────▼─────────────────────────────────────────┐   │
│  │ DashboardController.dashboard()                               │   │
│  │                                                                │   │
│  │ 1. Load tasks: taskService.getAllOpenTasks()                 │   │
│  │ 2. Add navbar attributes to Model:                            │   │
│  │    - activePage = "dashboard"                                │   │
│  │    - pageTitle = "CrowdServe Dashboard"                      │   │
│  │    - pageSubtitle = "Open tasks feed..."                     │   │
│  │    - unreadCount = 3 (from notificationService)              │   │
│  └────────────────────┬─────────────────────────────────────────┘   │
│                       │                                               │
│  ┌────────────────────▼─────────────────────────────────────────┐   │
│  │ Thymeleaf Processes: "dashboard.html"                         │   │
│  │                                                                │   │
│  │ 1. Encounters: <th:block th:replace="fragments/navbar :: navbar"> │
│  │ 2. Looks for fragment: fragments/navbar.html                 │   │
│  │ 3. Finds: <header th:fragment="navbar">...</header>          │   │
│  │ 4. Replaces <th:block> with <header> content                 │   │
│  │ 5. Evaluates expressions:                                     │   │
│  │    - ${pageTitle} → "CrowdServe Dashboard"                   │   │
│  │    - ${activePage == 'dashboard' ? 'active' : ''} → 'active' │   │
│  │    - ${unreadCount > 0} → true, displays badge               │   │
│  └────────────────────┬─────────────────────────────────────────┘   │
│                       │                                               │
│  ┌────────────────────▼─────────────────────────────────────────┐   │
│  │ Thymeleaf Renders Final HTML:                                 │   │
│  │                                                                │   │
│  │ <header class="app-header">                                   │   │
│  │   <div class="brand">                                          │   │
│  │     <div class="logo">CS</div>                                │   │
│  │     <h1>CrowdServe Dashboard</h1>                             │   │
│  │     <div class="meta">Open tasks feed...</div>                │   │
│  │   </div>                                                       │   │
│  │   <div class="app-nav">                                        │   │
│  │     <ul class="nav-list">                                      │   │
│  │       <li><a class="nav-link active">Dashboard</a></li>       │   │
│  │       <li><a class="nav-link">My Tasks</a></li>               │   │
│  │       <li><a class="nav-link">                                │   │
│  │         Notifications                                         │   │
│  │         <span class="badge bg-danger">3</span>                │   │
│  │       </a></li>                                                │   │
│  │       <li><a class="nav-link">Reports</a></li>                │   │
│  │     </ul>                                                       │   │
│  │   </div>                                                       │   │
│  │   <div class="controls">                                       │   │
│  │     <a class="btn btn-outline">Home</a>                       │   │
│  │     <a class="btn btn-outline">Create Task</a>                │   │
│  │     <a class="btn btn-primary">Logout</a>                     │   │
│  │   </div>                                                       │   │
│  │ </header>                                                      │   │
│  └────────────────────┬─────────────────────────────────────────┘   │
│                       │                                               │
│  ┌────────────────────▼─────────────────────────────────────────┐   │
│  │ CSS Styling (dashboard.css):                                  │   │
│  │                                                                │   │
│  │ header.app-header { display: flex; ... }                      │   │
│  │ .brand { display: flex; gap: 14px; ... }                      │   │
│  │ .logo { gradient background, box-shadow }                     │   │
│  │ .nav-link.active { color: primary, background: primary-soft }│   │
│  │ .badge { padding, border-radius, font-size }                  │   │
│  │ .bg-danger { background-color: var(--danger), color: white } │   │
│  └────────────────────┬─────────────────────────────────────────┘   │
│                       │                                               │
│  ┌────────────────────▼─────────────────────────────────────────┐   │
│  │ Browser Renders Page with Styled Navbar:                      │   │
│  │                                                                │   │
│  │ ┌──────────────────────────────────────────────────────────┐ │   │
│  │ │ [CS] CrowdServe Dashboard  [Home][+][Logout]             │ │   │
│  │ │      Open tasks feed...                                   │ │   │
│  │ ├──────────────────────────────────────────────────────────┤ │   │
│  │ │ Dashboard  My Tasks  Notifications 🔴3  Reports         │ │   │
│  │ │                                                           │ │   │
│  │ │ [Open Tasks...]                                           │ │   │
│  │ └──────────────────────────────────────────────────────────┘ │   │
│  └──────────────────────────────────────────────────────────────┘   │
│                                                                       │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 🎨 Navbar Visual Design

### Navbar Layout (Flexbox)

```
┌──────────────────────────────────────────────────────────────┐
│                      header.app-header                        │
│  display: flex; align-items: center; justify-content: flex-start │
├──────┬────────────────────────┬───────────────────┬──────────┤
│      │                        │                   │          │
│Brand │     app-nav            │                   │Controls  │
│      │ (flex: 1)              │                   │          │
│      │                        │                   │          │
│ flex-│ Dashboard  My Tasks   │                   │ Home     │
│shrink│ Notif  Reports        │                   │ Create   │
│ 0    │                        │                   │ Logout   │
│      │                        │                   │          │
│      │                        │                   │flex-     │
│      │                        │                   │shrink: 0 │
│      │                        │                   │margin-   │
│      │                        │                   │left: auto│
└──────┴────────────────────────┴───────────────────┴──────────┘
```

### Responsive Breakpoints

```
Desktop (1200px+)
┌──────┬────────────────────────────────────┬──────────────┐
│Brand │ Dashboard  My Tasks  Notif  Reports│Home +Logout  │
└──────┴────────────────────────────────────┴──────────────┘

Tablet (980px)
┌──────┬──────────────────────────────┬──────────────┐
│Brand │Dashboard My Tasks Notif Rep. │Home + Logout │
└──────┴──────────────────────────────┴──────────────┘

Mobile (740px)
┌──────────────────────────────────┐
│Brand                              │
├──────────────────────────────────┤
│Dashboard  My Tasks  Notifications │
│Reports                            │
├──────────────────────────────────┤
│Home  Create Task  Logout          │
└──────────────────────────────────┘
```

---

## 🔄 Data Flow: How Navbar Gets Its Data

```
┌─────────────────────────────────────────────────────────────┐
│ User navigates: http://localhost:8080/dashboard             │
└────────────────────────┬──────────────────────────────────────┘
                         │
                         ▼
        ┌─────────────────────────────────┐
        │ Spring Security checks auth      │
        │ Principal = currentUser          │
        └────────────┬────────────────────┘
                     │
                     ▼
        ┌─────────────────────────────────┐
        │ DashboardController.dashboard()  │
        │ @GetMapping("/dashboard")        │
        └────────────┬────────────────────┘
                     │
         ┌───────────┼───────────┐
         │           │           │
         ▼           ▼           ▼
    ┌────────┐  ┌────────┐  ┌──────────────┐
    │ Load   │  │ Add    │  │ Get unread   │
    │ tasks  │  │ page   │  │ count        │
    │        │  │ title  │  │              │
    └────────┘  └────────┘  └──────────────┘
         │           │           │
         └───────────┼───────────┘
                     │
                     ▼
        ┌──────────────────────────────┐
        │ model.addAttribute()          │
        │ tasks = [Task, Task, ...]     │
        │ activePage = "dashboard"      │
        │ pageTitle = "..."             │
        │ pageSubtitle = "..."          │
        │ unreadCount = 3               │
        └────────────┬─────────────────┘
                     │
                     ▼
        ┌──────────────────────────────┐
        │ return "dashboard"             │
        │ (Thymeleaf renders template)  │
        └────────────┬─────────────────┘
                     │
                     ▼
        ┌──────────────────────────────┐
        │ dashboard.html                │
        │ <th:block th:replace=         │
        │  "fragments/navbar :: navbar" │
        │ </th:block>                   │
        └────────────┬─────────────────┘
                     │
                     ▼
        ┌──────────────────────────────┐
        │ fragments/navbar.html         │
        │ <header th:fragment="navbar"> │
        │  <h1>${pageTitle}</h1>        │
        │  <a th:classappend=           │
        │   "${activePage == 'dash..'   │
        │   ? 'active' : ''}">          │
        │   Dashboard</a>               │
        │  <span th:if=                 │
        │   "${unreadCount > 0}">       │
        │   ${unreadCount}</span>       │
        │ </header>                     │
        └────────────┬─────────────────┘
                     │
                     ▼
        ┌──────────────────────────────┐
        │ Processed HTML with values:  │
        │ - pageTitle injected         │
        │ - active class applied       │
        │ - badge displayed (if > 0)   │
        └────────────┬─────────────────┘
                     │
                     ▼
        ┌──────────────────────────────┐
        │ CSS styles applied            │
        │ header.app-header {           │
        │   display: flex;              │
        │   colors, shadows, spacing    │
        │ }                             │
        │ .nav-link.active {            │
        │   color: blue;                │
        │   background: light-blue;     │
        │ }                             │
        └────────────┬─────────────────┘
                     │
                     ▼
        ┌──────────────────────────────┐
        │ Browser renders:              │
        │ ┌──────────────────────────┐ │
        │ │[CS] CrowdServe Dashboard  │ │
        │ │ Dashboard  My Tasks  ...  │ │
        │ │ [Active link highlighted] │ │
        │ │ [Badge: 3]                │ │
        │ └──────────────────────────┘ │
        └──────────────────────────────┘
```

---

## 🗂️ File Structure & Dependencies

```
CrowdServe/
│
├── src/main/java/com/crowdserve/
│   ├── controller/
│   │   ├── DashboardController.java          ← adds model attributes
│   │   ├── MyTasksController.java            ← adds model attributes
│   │   ├── NotificationController.java       ← adds model attributes
│   │   └── ReportsController.java            ← adds model attributes
│   │
│   ├── service/
│   │   ├── NotificationService.java          ← countUnreadNotificationsForUser()
│   │   ├── TaskService.java
│   │   └── UserService.java
│   │
│   └── model/
│       ├── User.java
│       ├── Task.java
│       ├── Notification.java
│       └── ...
│
├── src/main/resources/
│   ├── templates/
│   │   ├── dashboard.html                    ← includes navbar fragment
│   │   ├── my-tasks.html                     ← includes navbar fragment
│   │   ├── notifications.html                ← includes navbar fragment
│   │   ├── reports.html                      ← includes navbar fragment
│   │   ├── login.html                        (no navbar)
│   │   ├── register.html                     (no navbar)
│   │   └── fragments/
│   │       └── navbar.html                   ← THE FRAGMENT
│   │
│   ├── static/
│   │   └── css/
│   │       └── dashboard.css                 ← styles navbar
│   │
│   └── application.properties                ← Thymeleaf config
│
└── pom.xml                                   (dependencies)
```

### File Dependencies

```
dashboard.html
    ├─ links to: /css/dashboard.css
    ├─ includes: fragments/navbar.html
    └─ uses model: activePage, pageTitle, pageSubtitle, unreadCount

fragments/navbar.html
    ├─ references: ${pageTitle}, ${pageSubtitle}, ${activePage}, ${unreadCount}
    └─ references CSS: .app-header, .brand, .logo, .nav-link, .active, .badge, .controls, .btn

dashboard.css
    ├─ styles: header.app-header
    ├─ styles: .brand, .logo
    ├─ styles: .app-nav, .nav-list, .nav-item, .nav-link
    ├─ styles: .nav-link.active (highlight current page)
    ├─ styles: .badge, .bg-danger (notification count)
    └─ styles: .controls, .btn, .btn-primary, .btn-outline

DashboardController.java
    ├─ calls: taskService.getAllOpenTasks()
    ├─ calls: notificationService.countUnreadNotificationsForUser()
    ├─ populates model: tasks, activePage, pageTitle, pageSubtitle, unreadCount
    └─ returns: "dashboard" (renders dashboard.html)

NotificationService.java
    └─ method: countUnreadNotificationsForUser(User) → Long
```

---

## 📊 Request/Response Cycle

```
User clicks "My Tasks" link in navbar
          │
          ▼
Browser: GET /my-tasks
          │
          ▼
Spring Security: Check authentication
          │
          ├─ Authenticated? ✓
          │
          ▼
Route to: MyTasksController.myTasks()
          │
          ├─ Load posted tasks
          ├─ Load assigned tasks
          │
          ├─ model.addAttribute("postedTasks", ...)
          ├─ model.addAttribute("assignedTasks", ...)
          │
          ├─ model.addAttribute("activePage", "my-tasks")     ← navbar knows which page
          ├─ model.addAttribute("pageTitle", "My Tasks")       ← navbar displays title
          ├─ model.addAttribute("pageSubtitle", "...")         ← navbar displays subtitle
          ├─ model.addAttribute("unreadCount", 2)              ← navbar shows badge
          │
          ├─ return "my-tasks"
          │
          ▼
Thymeleaf: Render my-tasks.html
          │
          ├─ Process: <th:block th:replace="fragments/navbar :: navbar">
          │   │
          │   ├─ Insert: <header class="app-header">
          │   │   │
          │   │   ├─ <h1>${pageTitle}</h1> → "My Tasks"
          │   │   ├─ <div class="meta">${pageSubtitle}</div> → "Posted tasks..."
          │   │   │
          │   │   ├─ <a th:classappend="${activePage == 'my-tasks' ? 'active' : ''}">
          │   │   │   My Tasks
          │   │   └─ </a> → active link highlighted
          │   │   │
          │   │   ├─ <span th:if="${unreadCount > 0}" class="badge">
          │   │   │   ${unreadCount}
          │   │   └─ </span> → badge displays "2"
          │   │
          │   └─ </header>
          │
          ├─ Process: page content (<h2>Posted Tasks</h2>, etc.)
          │
          ▼
CSS: Apply styles
          │
          ├─ header.app-header → flexbox layout, spacing
          ├─ .nav-link.active → blue background, blue text
          ├─ .badge.bg-danger → red background, white text
          │
          ▼
HTML Response with styled navbar and content
          │
          ▼
Browser renders final page:
          │
          ┌────────────────────────────────┐
          │ [CS] My Tasks                   │
          │      Posted tasks...            │
          ├────────────────────────────────┤
          │Dashboard [My Tasks] Notif Rept │
          │         ^(highlighted in blue)  │
          │                            🔴 2 │
          ├────────────────────────────────┤
          │ Posted Tasks Section            │
          │ - Task 1                        │
          │ - Task 2                        │
          │                                 │
          │ Assigned Tasks Section          │
          │ - Task 3                        │
          └────────────────────────────────┘
```

---

## 🔌 Integration Points

### Point 1: Controller → Model
```java
// Every controller method adds 4 attributes
model.addAttribute("activePage", "page-key");
model.addAttribute("pageTitle", "Page Title");
model.addAttribute("pageSubtitle", "Subtitle");
model.addAttribute("unreadCount", notificationService.count(...));
```

### Point 2: Template → Fragment
```html
<!-- Every authenticated template includes fragment -->
<th:block th:replace="fragments/navbar :: navbar"></th:block>
```

### Point 3: Fragment → CSS
```html
<!-- Fragment uses CSS classes -->
<header class="app-header">
  <a class="nav-link" th:classappend="${activePage == '...' ? 'active' : ''}">
  <span class="badge bg-danger">
```

### Point 4: CSS → Browser
```css
/* CSS styles the navbar */
header.app-header { display: flex; ... }
.nav-link.active { color: var(--primary); ... }
.badge { padding: 4px 8px; ... }
```

---

## ✅ Quality Gate: Component Checklist

```
┌─────────────────┬──────────┬──────────────────────┐
│ Component       │ Status   │ Verification         │
├─────────────────┼──────────┼──────────────────────┤
│ Fragment HTML   │ ✅       │ <header> element     │
│ Fragment CSS    │ ✅       │ Selects header.app   │
│ Template Include│ ✅       │ th:block th:replace  │
│ Controllers (4) │ ✅       │ All add attributes   │
│ Configuration   │ ✅       │ Cache disabled       │
│ Documentation   │ ✅       │ 4 files created      │
└─────────────────┴──────────┴──────────────────────┘
```

---

**Architecture Version**: 1.0  
**Last Updated**: December 5, 2025  
**Status**: Production Ready
