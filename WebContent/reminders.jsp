<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User" %>
<%@ page import="model.Reminder" %>
<%@ page import="java.util.List" %>
<%
  User user = (User) session.getAttribute("user");
  if (user == null) {
    response.sendRedirect(request.getContextPath() + "/login.jsp");
    return;
  }
  @SuppressWarnings("unchecked")
  List<Reminder> reminders = (List<Reminder>) request.getAttribute("reminders");
  if (reminders == null) reminders = java.util.Collections.emptyList();
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Reminders – PawPulse</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body>
  <jsp:include page="header.jsp" />
  <div class="container py-4">
    <h2 class="mb-4">Upcoming Reminders</h2>
    <% if (request.getAttribute("error") != null) { %>
      <div class="alert alert-warning"><%= request.getAttribute("error") %></div>
    <% } %>
    <div class="card card-pet">
      <div class="card-body">
        <% if (reminders.isEmpty()) { %>
          <p class="text-muted mb-0">No upcoming reminders. Reminders are created automatically when you add vaccination records with a next-due date.</p>
        <% } else { %>
          <div class="list-group list-group-flush">
            <% for (Reminder r : reminders) {
              long days = r.getDueDate() != null ? (r.getDueDate().getTime() - System.currentTimeMillis()) / (24*60*60*1000) : 999;
              String badge = days <= 0 ? "reminder-urgent" : (days <= 3 ? "reminder-urgent" : "reminder-due");
            %>
              <div class="list-group-item d-flex justify-content-between align-items-center <%= badge %>">
                <div>
                  <strong><%= r.getTitle() != null ? r.getTitle() : "Reminder" %></strong>
                  <% if (r.getDescription() != null && !r.getDescription().isEmpty()) { %><br><small class="text-muted"><%= r.getDescription() %></small><% } %>
                  <br><small><%= r.getDueDate() != null ? new java.text.SimpleDateFormat("MMM d, yyyy").format(r.getDueDate()) : "" %> <% if (r.getReminderType() != null) { %> – <%= r.getReminderType() %><% } %></small>
                </div>
                <form action="${pageContext.request.contextPath}/health" method="post" class="d-inline">
                  <input type="hidden" name="action" value="completeReminder">
                  <input type="hidden" name="reminderId" value="<%= r.getReminderId() %>">
                  <button type="submit" class="btn btn-sm btn-outline-success">Mark done</button>
                </form>
              </div>
            <% } %>
          </div>
        <% } %>
      </div>
    </div>
  </div>
  <jsp:include page="footer.jsp" />
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
