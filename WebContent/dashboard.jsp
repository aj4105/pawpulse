<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User" %>
<%@ page import="model.Pet" %>
<%@ page import="implementors.PetOperationsImpl" %>
<%@ page import="implementors.HealthOperationsImpl" %>
<%@ page import="operations.PetOperations" %>
<%@ page import="operations.HealthOperations" %>
<%@ page import="java.util.List" %>
<%
  User user = (User) session.getAttribute("user");
  if (user == null) {
    response.sendRedirect(request.getContextPath() + "/login.jsp");
    return;
  }
  PetOperations petOps = new PetOperationsImpl();
  HealthOperations healthOps = new HealthOperationsImpl();
  List<Pet> pets = petOps.getPetsByUserId(user.getUserId());
  List<model.Reminder> reminders = healthOps.getUpcomingReminders(user.getUserId());
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Dashboard – PawPulse</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body>
  <jsp:include page="header.jsp" />
  <div class="container py-4">
    <h2 class="mb-4">Welcome, <%= user.getFullName() != null && !user.getFullName().isEmpty() ? user.getFullName() : user.getEmail() %> 🐾</h2>

    <div class="row">
      <div class="col-lg-8">
        <div class="card card-pet mb-4">
          <div class="card-header">My Pets</div>
          <div class="card-body">
            <% if (pets.isEmpty()) { %>
              <p class="text-muted mb-0">No pets yet. <a href="${pageContext.request.contextPath}/pet-form.jsp">Add your first pet</a>.</p>
            <% } else { %>
              <div class="row g-3">
                <% for (Pet p : pets) { %>
                  <div class="col-md-6">
                    <div class="card border-0 bg-light rounded-3 p-3">
                      <strong><%= p.getName() %></strong> – <%= p.getSpecies() != null ? p.getSpecies() : "Pet" %>
                      <% if (p.getBreed() != null && !p.getBreed().isEmpty()) { %><br><small class="text-muted"><%= p.getBreed() %></small><% } %>
                      <div class="mt-2">
                        <a href="${pageContext.request.contextPath}/health?action=history&petId=<%= p.getPetId() %>" class="btn btn-sm btn-paw">Health</a>
                        <a href="${pageContext.request.contextPath}/pet?action=edit&id=<%= p.getPetId() %>" class="btn btn-sm btn-outline-secondary">Edit</a>
                      </div>
                    </div>
                  </div>
                <% } %>
              </div>
              <a href="${pageContext.request.contextPath}/pet-form.jsp" class="btn btn-paw mt-3">+ Add Pet</a>
            <% } %>
          </div>
        </div>
      </div>
      <div class="col-lg-4">
        <div class="card card-pet mb-4">
          <div class="card-header">Upcoming Reminders</div>
          <div class="card-body">
            <% if (reminders.isEmpty()) { %>
              <p class="text-muted mb-0 small">No upcoming reminders.</p>
            <% } else {
              for (model.Reminder r : reminders) {
                if (r.getDueDate() != null) {
                  java.util.Calendar c = java.util.Calendar.getInstance();
                  c.setTime(r.getDueDate());
                  long days = (r.getDueDate().getTime() - System.currentTimeMillis()) / (24*60*60*1000);
                  String badge = days <= 3 ? "reminder-urgent" : "reminder-due";
            %>
              <div class="<%= badge %> p-2 mb-2 rounded bg-light">
                <strong><%= r.getTitle() %></strong><br>
                <small><%= new java.text.SimpleDateFormat("MMM d, yyyy").format(r.getDueDate()) %></small>
                <a href="${pageContext.request.contextPath}/health?action=reminders" class="small">View all</a>
              </div>
            <% } } } %>
            <a href="${pageContext.request.contextPath}/health?action=reminders" class="btn btn-sm btn-paw">View all reminders</a>
          </div>
        </div>
      </div>
    </div>

    <div class="card card-pet">
      <div class="card-body">
        <h5 class="card-title">Quick actions</h5>
        <a href="${pageContext.request.contextPath}/health?action=suggest" class="btn btn-paw me-2">AI Health Suggestion</a>
        <a href="${pageContext.request.contextPath}/health?action=vets" class="btn btn-outline-secondary">Vet &amp; Emergency</a>
      </div>
    </div>
  </div>
  <jsp:include page="footer.jsp" />
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
