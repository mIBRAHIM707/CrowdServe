package com.crowdserve.controller;

import com.crowdserve.model.User;
import com.crowdserve.repository.TaskRepository;
import com.crowdserve.repository.UserRepository;
import com.crowdserve.service.NotificationService;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.OutputStream;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * Controller for handling reports and analytics pages.
 */
@Controller
@RequestMapping("/reports")
public class ReportsController {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final NotificationService notificationService;

    @Autowired
    public ReportsController(UserRepository userRepository, TaskRepository taskRepository, NotificationService notificationService) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.notificationService = notificationService;
    }

    /**
     * Reports center page - displays available reports
     */
    @GetMapping
    public String reports(Principal principal, Model model) {
        // Add navbar attributes
        model.addAttribute("activePage", "reports");
        model.addAttribute("pageTitle", "Reports Center");
        model.addAttribute("pageSubtitle", "Download your platform analytics & task summaries");
        
        // Add unread notifications count if user is authenticated
        if (principal != null) {
            Optional<User> userOpt = userRepository.findByEmail(principal.getName());
            if (userOpt.isPresent()) {
                long unreadCount = notificationService.getUnreadCount(userOpt.get());
                model.addAttribute("unreadCount", unreadCount);
            }
        }
        
        return "reports";
    }

    /**
     * Generate a sample CSV (user activity) and stream it as a file download.
     */
    @GetMapping("/download/csv")
    public void downloadCsv(HttpServletResponse response) throws IOException {
        String filename = "user-activity.csv";
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        String header = "id,fullName,email,activityCount\n";
        StringBuilder sb = new StringBuilder();
        sb.append(header);

        List<User> users = userRepository.findAll();
        for (User u : users) {
            int activity = taskRepository.findByPoster(u).size() + taskRepository.findByWorker(u).size();
            // CSV escape basic values (commas)
            String name = u.getFullName() != null ? u.getFullName().replace("\"", "\"\"") : "";
            String email = u.getEmail() != null ? u.getEmail().replace("\"", "\"\"") : "";
            sb.append(u.getId()).append(",\"").append(name).append("\",\"").append(email).append("\",").append(activity).append("\n");
        }

        try (OutputStream os = response.getOutputStream()) {
            os.write(sb.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8));
            os.flush();
        }
    }

    /**
     * Generate a simple PDF report (user activity) using OpenPDF and stream to client.
     */
    @GetMapping("/download/pdf")
    public void downloadPdf(HttpServletResponse response) throws IOException {
        String filename = "user-activity.pdf";
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            document.add(new Paragraph("User Activity Report"));
            document.add(new Paragraph("\n"));

            List<User> users = userRepository.findAll();
            for (User u : users) {
                int activity = taskRepository.findByPoster(u).size() + taskRepository.findByWorker(u).size();
                document.add(new Paragraph(String.format("%d - %s - %s - activity: %d", u.getId(), u.getFullName(), u.getEmail(), activity)));
            }

        } catch (DocumentException de) {
            throw new IOException("Failed to generate PDF", de);
        } finally {
            document.close();
        }
    }
}
