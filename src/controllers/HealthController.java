package controllers;

import model.HealthRecord;
import model.User;
import model.Pet;
import operations.HealthOperations;
import operations.PetOperations;
import implementors.HealthOperationsImpl;
import implementors.PetOperationsImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Handles health records, reminders, AI suggestions, and vet list.
 */
@WebServlet("/health")
public class HealthController extends HttpServlet {

    private final HealthOperations healthOps = new HealthOperationsImpl();
    private final PetOperations petOps = new PetOperationsImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getLoggedInUser(req);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        String action = req.getParameter("action");
        if ("history".equals(action)) {
            String petIdStr = req.getParameter("petId");
            if (petIdStr != null) {
                try {
                    int petId = Integer.parseInt(petIdStr);
                    Pet pet = petOps.getPetById(petId, user.getUserId());
                    if (pet != null) {
                        List<HealthRecord> history = healthOps.getPetHealthHistory(petId, user.getUserId());
                        req.setAttribute("pet", pet);
                        req.setAttribute("healthHistory", history);
                    }
                } catch (Exception e) {
                    req.setAttribute("error", e.getMessage());
                }
            }
            req.getRequestDispatcher("/health-records.jsp").forward(req, resp);
            return;
        }
        if ("reminders".equals(action)) {
            try {
                req.setAttribute("reminders", healthOps.getUpcomingReminders(user.getUserId()));
            } catch (Exception e) {
                req.setAttribute("error", e.getMessage());
            }
            req.getRequestDispatcher("/reminders.jsp").forward(req, resp);
            return;
        }
        if ("vets".equals(action)) {
            try {
                req.setAttribute("vets", healthOps.getAllVets());
            } catch (Exception e) {
                req.setAttribute("error", e.getMessage());
            }
            req.getRequestDispatcher("/vet-support.jsp").forward(req, resp);
            return;
        }
        if ("suggest".equals(action)) {
            String symptoms = req.getParameter("symptoms");
            if (symptoms != null && !symptoms.trim().isEmpty()) {
                try {
                    req.setAttribute("suggestion", healthOps.getHealthSuggestion(symptoms.trim()));
                } catch (Exception e) {
                    req.setAttribute("suggestion", "Error: " + e.getMessage());
                }
            }
            req.setAttribute("symptomsEntered", symptoms);
            req.getRequestDispatcher("/ai-health.jsp").forward(req, resp);
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/dashboard.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        User user = getLoggedInUser(req);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        String action = req.getParameter("action");
        if ("addRecord".equals(action)) {
            String petIdStr = req.getParameter("petId");
            if (petIdStr != null) {
                try {
                    int petId = Integer.parseInt(petIdStr);
                    Pet pet = petOps.getPetById(petId, user.getUserId());
                    if (pet != null) {
                        HealthRecord rec = new HealthRecord();
                        rec.setPetId(petId);
                        rec.setRecordType(req.getParameter("recordType") != null ? req.getParameter("recordType") : "Checkup");
                        rec.setDescription(req.getParameter("description"));
                        rec.setVetName(req.getParameter("vetName"));
                        rec.setNextDueDate(req.getParameter("nextDueDate"));
                        String dateStr = req.getParameter("recordDate");
                        rec.setRecordDate(dateStr != null && !dateStr.isEmpty() ? java.sql.Date.valueOf(dateStr) : new Date());
                        healthOps.addHealthRecord(rec);
                    }
                } catch (Exception e) {
                    req.setAttribute("error", e.getMessage());
                }
            }
            String redirectPetId = req.getParameter("petId");
            resp.sendRedirect(req.getContextPath() + "/health?action=history&petId=" + (redirectPetId != null ? redirectPetId : ""));
            return;
        }
        if ("completeReminder".equals(action)) {
            String idStr = req.getParameter("reminderId");
            if (idStr != null) {
                try {
                    healthOps.markReminderCompleted(Integer.parseInt(idStr), user.getUserId());
                } catch (Exception ignored) { }
            }
            resp.sendRedirect(req.getContextPath() + "/health?action=reminders");
            return;
        }
        if ("suggest".equals(action)) {
            String symptoms = req.getParameter("symptoms");
            resp.sendRedirect(req.getContextPath() + "/health?action=suggest&symptoms=" + (symptoms != null ? java.net.URLEncoder.encode(symptoms, "UTF-8") : ""));
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/dashboard.jsp");
    }

    private User getLoggedInUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) return null;
        Object u = session.getAttribute("user");
        return (u instanceof User) ? (User) u : null;
    }
}
