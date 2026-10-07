<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User" %>
<%@ page import="model.Vet" %>
<%@ page import="java.util.List" %>
<%
  User user = (User) session.getAttribute("user");
  if (user == null) {
    response.sendRedirect(request.getContextPath() + "/login.jsp");
    return;
  }
  @SuppressWarnings("unchecked")
  List<Vet> vets = (List<Vet>) request.getAttribute("vets");
  if (vets == null) vets = java.util.Collections.emptyList();
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Vet &amp; Emergency Support – PawPulse</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body>
  <jsp:include page="header.jsp" />
  <div class="container py-4">
    <h2 class="mb-4">Vet &amp; Emergency Support</h2>
    <% if (request.getAttribute("error") != null) { %>
      <div class="alert alert-warning"><%= request.getAttribute("error") %></div>
    <% } %>
    <div class="alert alert-info">
      <strong>Emergency:</strong> In case of emergency, contact your nearest vet or emergency pet hospital. Keep their numbers handy.
    </div>
    <div class="row g-3">
      <% for (Vet v : vets) { %>
        <div class="col-md-6 col-lg-4">
          <div class="card card-pet h-100">
            <div class="card-header"><%= v.getClinicName() != null ? v.getClinicName() : "Clinic" %></div>
            <div class="card-body">
              <% if (v.getAddress() != null && !v.getAddress().isEmpty()) { %>
                <p class="mb-1"><strong>Address:</strong> <%= v.getAddress() %></p>
              <% } %>
              <% if (v.getPhone() != null && !v.getPhone().isEmpty()) { %>
                <p class="mb-1"><strong>Phone:</strong> <a href="tel:<%= v.getPhone() %>"><%= v.getPhone() %></a></p>
              <% } %>
              <% if (v.getEmergencyPhone() != null && !v.getEmergencyPhone().isEmpty()) { %>
                <p class="mb-1"><strong class="text-danger">Emergency:</strong> <a href="tel:<%= v.getEmergencyPhone() %>"><%= v.getEmergencyPhone() %></a></p>
              <% } %>
              <% if (v.getEmail() != null && !v.getEmail().isEmpty()) { %>
                <p class="mb-1"><strong>Email:</strong> <a href="mailto:<%= v.getEmail() %>"><%= v.getEmail() %></a></p>
              <% } %>
              <% if (v.getWorkingHours() != null && !v.getWorkingHours().isEmpty()) { %>
                <p class="mb-0 small text-muted"><%= v.getWorkingHours() %></p>
              <% } %>
            </div>
          </div>
        </div>
      <% } %>
    </div>
    <% if (vets.isEmpty()) { %>
      <p class="text-muted">No vet clinics in database. Add clinics via SQL or admin to see them here.</p>
    <% } %>
  </div>
  <jsp:include page="footer.jsp" />
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
