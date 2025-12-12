[33mcommit 565d58fa5ef309e7aaa218ffb86a86ea01e40430[m[33m ([m[1;36mHEAD[m[33m -> [m[1;32mmybranch[m[33m, [m[1;31morigin/mybranch[m[33m)[m
Author: Muhammad Mehdi Raza <muhammadmehdiraza2004@gmail.com>
Date:   Tue Dec 9 21:42:24 2025 +0500

    Fixed the issue; User is not able to post multiple tasks

[33mcommit 4cd71903248adf6c617f3a33ca8a94917e6ca1fb[m
Author: Muhammad Mehdi Raza <muhammadmehdiraza2004@gmail.com>
Date:   Tue Dec 9 20:46:28 2025 +0500

    Fix the issue of my task, it was producing whitelabel error

[33mcommit 99cce5a53e18fabd977c1cf52f66929700e833eb[m
Author: Muhammad Mehdi Raza <muhammadmehdiraza2004@gmail.com>
Date:   Tue Dec 9 20:39:16 2025 +0500

    Fixed the notification issue such that when one account has posted the task and another account has accepted and completed, the first account one can see that the task has completed and the one which commpleted the task gets the notification that task has been completed

[33mcommit c50fe4ada228d095d7e9a88db1fb589d0aca7547[m
Author: Muhammad Mehdi Raza <muhammadmehdiraza2004@gmail.com>
Date:   Tue Dec 9 20:29:06 2025 +0500

    Fixed Endpoints as Login And register were only working

[33mcommit 529dd0b7ef6279e854f0dd54dd9454abbe33de3e[m
Author: Muhammad Mehdi Raza <muhammadmehdiraza2004@gmail.com>
Date:   Tue Dec 9 19:14:41 2025 +0500

    Fix DTO accessors and notification lookups
    
    - UserServiceImpl: use DTO getters; set username from email.
    - NotificationController: use findByUser* repo methods; use getUser().getId().
    - RegistrationController: use no-arg UserRegistrationDto.
    - Dash/MyTasks/Reports: call getUnreadCount().
    - UserServiceImplTest: use 3-arg ctor and getters.
    - AuthenticationIntegrationTest: delete tasks before users; register/login via email; expect /tasks redirect.

[33mcommit 7c03c4912acdf6c4099f28ac0aa9f7511b2deb0c[m[33m ([m[1;31morigin/main[m[33m, [m[1;31morigin/HEAD[m[33m, [m[1;32mmain[m[33m)[m
Author: mIBRAHIM707 <ibrahimclash707@gmail.com>
Date:   Sun Dec 7 22:50:14 2025 +0500

    chore: Remove PR documentation files that are not needed in main project

[33mcommit d3f66317a464aaeed833b400c3922961638d821d[m
Merge: c3ce5d6 5acdb37
Author: mIBRAHIM707 <ibrahimclash707@gmail.com>
Date:   Sun Dec 7 22:48:48 2025 +0500

    Merge pull request #6: Feature/registration UI - Enhanced UI with navigation bar and improved styling

[33mcommit 5acdb37942e2b58c826f91abdf8eb4b0e897311f[m
Merge: 1f3fb07 c3ce5d6
Author: mIBRAHIM707 <ibrahimclash707@gmail.com>
Date:   Sun Dec 7 22:48:28 2025 +0500

    Merge main into pr-6: Resolved conflicts between registration UI feature and main branch updates
    
    - Kept PR#6's improved UI with navbar from register/login/index pages
    - Accepted main branch's better architecture for Notification model (Lombok, proper relationships)
    - Accepted main branch's interface-based NotificationService
    - Merged UserRegistrationDto to use class instead of record for Spring form binding
    - Resolved pom.xml to include all dependencies from both branches
    - Kept enhanced CSS styling from PR#6 for better UI/UX

[33mcommit c3ce5d64300e153ace269e7038facdcb09647001[m
Author: mIBRAHIM707 <ibrahimclash707@gmail.com>
Date:   Sun Dec 7 22:14:49 2025 +0500

    .

[33mcommit 6904494d6aaff10abe09810c7f994200e93e35b1[m
Merge: cc04835 60f16f1
Author: Muhammad Ibrahim <147312029+mIBRAHIM707@users.noreply.github.com>
Date:   Sun Dec 7 22:08:45 2025 +0500

    Merge pull request #5 from mIBRAHIM707/task-Filttering-Strategy-Pattern
    
    Add strategy pattern for task filtering

[33mcommit cc048354865d30446844067c502fb4b9a0d28e25[m
Merge: 5f1de2a 7da6c35
Author: Muhammad Ibrahim <147312029+mIBRAHIM707@users.noreply.github.com>
Date:   Sun Dec 7 22:08:30 2025 +0500

    Merge pull request #4 from mIBRAHIM707/ReportServiceImpl-and-ReportController
    
    Report service impl and report controller

[33mcommit 1f3fb07e15c56ec861ff66da8847a004cacb2b56[m[33m ([m[1;31morigin/feature/registration-ui[m[33m)[m
Author: tughral1 <151851339+tughral1@users.noreply.github.com>
Date:   Fri Dec 5 17:44:41 2025 +0500

    Navbar and registration page styles improved
    
    Home page needs to be done. Login page needs work.

[33mcommit 5b511f3a19a91406289761a1844b5a00737c0998[m
Author: tughral1 <151851339+tughral1@users.noreply.github.com>
Date:   Fri Dec 5 16:03:38 2025 +0500

    My tasks + Navbar

[33mcommit b46c3d81f6a00c25b4f030f40aaa008eeb510cfa[m
Author: tughral1 <151851339+tughral1@users.noreply.github.com>
Date:   Thu Dec 4 13:58:03 2025 +0500

    Fixed Notifications
    
    Removed inline styles

[33mcommit 0b914f1ec265f2511c10b263af298f27fd047328[m
Author: tughral1 <151851339+tughral1@users.noreply.github.com>
Date:   Thu Dec 4 13:55:38 2025 +0500

    Implemented Notiffications
    
    Added a notifications feature with AJAX support and report downloads:
    
    New Files:
    
    Notification.java — database entity for notifications
    NotificationRepository.java — database queries for notifications
    NotificationController.java — endpoints to view and mark notifications as read (AJAX + fallback)
    notifications.html — notifications page with instant UI updates
    notifications.js — client-side AJAX handler with CSRF support
    ReportsController.java — CSV and PDF download endpoints
    reports.html — reports page
    Updates:
    
    Added OpenPDF dependency for PDF generation
    Extended dashboard.css with report and notification styles

[33mcommit 2bc527849e2551888d52a1b091d2da3913b7bf63[m
Author: tughral1 <151851339+tughral1@users.noreply.github.com>
Date:   Thu Dec 4 11:04:27 2025 +0500

    Created ReportsController.java along with reports.html
    
    The stylesheet for reports is in dashboard.css since the styles in dashboard and reports had to be consistent. As for the controller for reportsController  Jagtaar can edit it later on when he gets to work on it.

[33mcommit 2eeb6d7f5fab8fb8ae6f0b7fbb48a92086602f6d[m
Author: tughral1 <151851339+tughral1@users.noreply.github.com>
Date:   Thu Dec 4 10:49:45 2025 +0500

    Create DashboardController.java
    
    Separate folder for the controller

[33mcommit d0251dde68bd6f935ffa76f917eecbe452a4c04c[m
Author: tughral1 <151851339+tughral1@users.noreply.github.com>
Date:   Thu Dec 4 10:40:02 2025 +0500

    Update dashboard.css

[33mcommit c7833da9cff849811f6934bd673bc32ee7551767[m
Author: tughral1 <151851339+tughral1@users.noreply.github.com>
Date:   Thu Dec 4 10:30:24 2025 +0500

    Update dashboard.css

[33mcommit 4a8050ec7b8e199c42b0210c415f007f059f473f[m
Author: tughral1 <151851339+tughral1@users.noreply.github.com>
Date:   Thu Dec 4 09:29:30 2025 +0500

    Update dashboard.html

[33mcommit 60f16f132b07d82e8642ceafa808119eccbb6e6d[m[33m ([m[1;31morigin/task-Filttering-Strategy-Pattern[m[33m)[m
Author: jagtar singh <jagtar3125@gmail.com>
Date:   Wed Dec 3 18:48:12 2025 +0500

    Add strategy pattern for task filtering
    
    Introduces the Strategy design pattern for filtering tasks by status. Adds TaskFilterStrategy interface, concrete filters for assigned, completed, and open tasks, and a TaskFilterContext to manage and apply strategies at runtime.

[33mcommit 7da6c357f5fd1429d29e5f5eb14a279439549e59[m[33m ([m[1;31morigin/ReportServiceImpl-and-ReportController[m[33m)[m
Author: jagtar singh <jagtar3125@gmail.com>
Date:   Tue Dec 2 02:54:15 2025 +0500

    Add ReportController for report download endpoints
    
    Introduces ReportController with endpoints to download completed tasks and top contributors reports as CSV files. The controller uses ReportService to generate report data and sets appropriate HTTP headers for file downloads.
    endpoints:
    http://localhost:8080/reports/completed-tasks
    http://localhost:8080/reports/top-contributors

[33mcommit 4296b9a3a690872e96c87a5061578b8968b0b2c1[m
Author: jagtar singh <jagtar3125@gmail.com>
Date:   Tue Dec 2 02:38:03 2025 +0500

    Add ReportServiceImpl with CSV report generation
    
    Implements ReportServiceImpl to generate CSV reports for completed tasks and top contributors. The service uses TaskRepository to fetch data and formats the results as downloadable CSV files.

[33mcommit 5f1de2aa638bdf2abcb318a9fa512596d43caf17[m
Author: mIBRAHIM707 <ibrahimclash707@gmail.com>
Date:   Fri Nov 28 20:26:17 2025 +0500

    fix: Navigation bugs - logout, login redirect, UI improvements
    
    - SecurityConfig: Fixed login redirect to /tasks (dashboard) instead of /profile
    - SecurityConfig: Fixed logout configuration with proper URL, session invalidation, and cookie deletion
    - All templates: Changed logout from GET link to POST form (Spring Security requirement)
    - index.html: Complete redesign with hero section, feature cards, and dashboard link
    - profile.html: Already has proper navigation to dashboard
    
    Fixed issues:
    - Logout 404 error (was using GET, now uses POST)
    - Login redirecting to non-existent profile page
    - No way to reach dashboard from home page

[33mcommit 62b7bcbd8df9b211bfd8580884ff43427df74744[m
Author: mIBRAHIM707 <ibrahimclash707@gmail.com>
Date:   Fri Nov 28 20:08:41 2025 +0500

    feat(UI): Create create-task.html form and fix TaskController create endpoint

[33mcommit 1c78541e9992e095f3e0206ad2d7c54e2fb3e6a8[m
Author: mIBRAHIM707 <ibrahimclash707@gmail.com>
Date:   Fri Nov 28 20:06:30 2025 +0500

    feat(UI): Create task-details.html with accept/complete task buttons

[33mcommit a79c1e0edae65135a01e1c9f4db50f9710c852b5[m
Author: mIBRAHIM707 <ibrahimclash707@gmail.com>
Date:   Fri Nov 28 20:05:27 2025 +0500

    feat(Controller): Add accept/complete task endpoints using TaskWorkflowFacade

[33mcommit 8a107ad4e57fbdbfbf274ad4c1a033759145dde2[m
Author: mIBRAHIM707 <ibrahimclash707@gmail.com>
Date:   Fri Nov 28 20:04:28 2025 +0500

    feat(Observer): Add Notification model, repository, and NotificationService as TaskObserver implementation

[33mcommit e4b655d48449eecca7d31763a3fd02c1dc14ef02[m
Author: mIBRAHIM707 <ibrahimclash707@gmail.com>
Date:   Fri Nov 28 20:03:12 2025 +0500

    feat(Observer): Create TaskObserver interface for Observer pattern

[33mcommit 4e06569edf4e8e26da85c0d46a7d036c83873209[m
Author: mIBRAHIM707 <ibrahimclash707@gmail.com>
Date:   Fri Nov 28 20:02:32 2025 +0500

    feat(Facade): Create TaskWorkflowFacade - Facade pattern for task workflow operations

[33mcommit 2f9cce4454e205f6030c5460a618dd534b875db2[m
Author: mIBRAHIM707 <ibrahimclash707@gmail.com>
Date:   Fri Nov 28 20:01:53 2025 +0500

    feat(TaskService): Add assignWorker and markCompleted methods for task lifecycle management

[33mcommit 3310973948e113c556a59162dba697603639154e[m
Author: mIBRAHIM707 <ibrahimclash707@gmail.com>
Date:   Fri Nov 28 19:53:16 2025 +0500

    Add CSS styling, Bootstrap for register, actuator dependency - fixes from PR #2 while keeping username field

[33mcommit 0d187c1844241b826e3ece617bf9da0e7f7122a7[m
Author: tughral1 <151851339+tughral1@users.noreply.github.com>
Date:   Fri Nov 28 16:37:41 2025 +0500

    Changes in dashboard
    
    .

[33mcommit 9a2712687ab8e470c2948001c4e887da509b324f[m[33m ([m[1;31morigin/feature/backend-task-logic[m[33m)[m
Author: jagtar singh <jagtar3125@gmail.com>
Date:   Sun Nov 23 03:29:12 2025 +0500

    Implement user profile and dashboard views
    
    Added Thymeleaf templates for dashboard and profile pages. Updated TaskController to display open tasks and associate created tasks with the logged-in user. Enhanced UserController to fetch and update user profiles, handling both email and username lookups. Modified SecurityConfig to redirect users to the profile page after login. Implemented profile update logic in UserServiceImpl to persist changes to user details.

[33mcommit 3eca6cfc9e96d0ee74d739c5c79d7697c8e3a441[m[33m ([m[1;31morigin/dashboard_controller[m[33m)[m
Author: mIBRAHIM707 <ibrahimclash707@gmail.com>
Date:   Fri Nov 21 22:15:06 2025 +0500

    Update TaskController.java

[33mcommit be214323e74d28411fa65c83fc0e6bdd94f3ab1d[m
Author: mIBRAHIM707 <ibrahimclash707@gmail.com>
Date:   Fri Nov 21 22:05:51 2025 +0500

    adding more things

[33mcommit 7242a2f490fd509d7d10fd1a43a7f2ef60597451[m
Author: mIBRAHIM707 <ibrahimclash707@gmail.com>
Date:   Fri Nov 21 21:59:28 2025 +0500

    bugs fix kerte kerte main khatam hojaonga

[33mcommit f2a551e0436a2684942b70a42ba4c7d6aaffcafb[m
Author: mIBRAHIM707 <ibrahimclash707@gmail.com>
Date:   Fri Nov 21 21:35:54 2025 +0500

    more error fixing

[33mcommit 8cc81d248e1a93d17fbe51e43e3e7db4b0d4f40e[m
Author: mIBRAHIM707 <ibrahimclash707@gmail.com>
Date:   Fri Nov 21 21:24:49 2025 +0500

    fixed a bug

[33mcommit 80af804297df94fcf23cf2df424dd326c2e1efd5[m
Author: mIBRAHIM707 <ibrahimclash707@gmail.com>
Date:   Fri Nov 21 21:21:25 2025 +0500

    pluh
    
    added a h2 file based database, phir i updated user and added bio, username and rating to match assignment 2 and created report 2
    created DTO

[33mcommit e4e9716d71f67ba0e950f2d990de2e4417a0ec8f[m
Author: tughral1 <151851339+tughral1@users.noreply.github.com>
Date:   Sat Nov 8 15:00:17 2025 +0500

    Dashboard Work in Progress and some register revisions

[33mcommit 9fc2c99ba5d6daec2d88123a093d0f7541d8ba00[m
Author: tughral1 <151851339+tughral1@users.noreply.github.com>
Date:   Thu Nov 6 15:33:29 2025 +0500

    Update register.html

[33mcommit f270c88b412e0c7b0820ca98efedead1c7361373[m
Author: tughral1 <151851339+tughral1@users.noreply.github.com>
Date:   Thu Nov 6 15:17:12 2025 +0500

    Debugged Some errors which prevented the app from running

[33mcommit f367345b331c56db2dca3b2263f8666eacb4382c[m
Merge: 6583b0a 1822c1f
Author: tughral1 <151851339+tughral1@users.noreply.github.com>
Date:   Thu Nov 6 12:03:28 2025 +0500

    Merge pull request #1 from mIBRAHIM707/feature/registration-ui
    
    Created register.html within main/resources/templates  and created re…

[33mcommit 1822c1f9473951cd648eb0e88e438eaf6ee8ec95[m
Merge: c1c6f0a 6583b0a
Author: tughral1 <151851339+tughral1@users.noreply.github.com>
Date:   Thu Nov 6 12:02:30 2025 +0500

    Merge branch 'main' into feature/registration-ui

[33mcommit c1c6f0a5368cca2b3c9950df9c05b8437b0f32bf[m
Author: tughral1 <151851339+tughral1@users.noreply.github.com>
Date:   Thu Nov 6 11:56:29 2025 +0500

    Created register.html within main/resources/templates  and created registrationController.java within a new folder controller within main

[33mcommit 6583b0a2780d1a3fe4f16ae17c98280f7a1384a2[m
Author: mIBRAHIM707 <ibrahimclash707@gmail.com>
Date:   Wed Nov 5 16:53:09 2025 +0500

    .

[33mcommit 48663742ef59851d0c70effc7a117ec4b1c1faab[m
Merge: 6e19b74 203ad3d
Author: mIBRAHIM707 <ibrahimclash707@gmail.com>
Date:   Wed Nov 5 16:51:03 2025 +0500

    Merge branch 'main' of https://github.com/mIBRAHIM707/CrowdServe

[33mcommit 6e19b74e3f4b973bc25198d519dd1ee90fcdd39e[m
Author: mIBRAHIM707 <ibrahimclash707@gmail.com>
Date:   Wed Nov 5 16:50:58 2025 +0500

    created task backend for jagtaar

[33mcommit ad8429cce25611052b3c695845601161f7ed93c0[m
Author: mIBRAHIM707 <ibrahimclash707@gmail.com>
Date:   Wed Nov 5 16:43:39 2025 +0500

    added user registeration skeletions for tughral

[33mcommit 203ad3d2d9d1ce000e90216dab2c62b5a939b98b[m
Author: tughral1 <151851339+tughral1@users.noreply.github.com>
Date:   Wed Nov 5 16:20:11 2025 +0500

    t

[33mcommit a9ee5833ecbd7f44407fecddd0ebf251252d79d1[m
Author: mIBRAHIM707 <ibrahimclash707@gmail.com>
Date:   Wed Nov 5 16:00:15 2025 +0500

    made landing pages for login and register and fixed a security bug

[33mcommit 82c945ad4682ca43315ecdb7266de429af748f54[m
Author: mIBRAHIM707 <ibrahimclash707@gmail.com>
Date:   Wed Nov 5 15:42:05 2025 +0500

    Initial commit: Project skeleton and core architecture

[33mcommit e8c001bea114a212a9117855cbc80190920c56ca[m
Author: mIBRAHIM707 <ibrahimclash707@gmail.com>
Date:   Wed Nov 5 15:33:50 2025 +0500

    Initial commit
