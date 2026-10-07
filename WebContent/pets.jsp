<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User" %>
<%@ page import="model.Pet" %>
<%@ page import="java.util.List" %>
<%
  User user = (User) session.getAttribute("user");
  if (user == null) {
    response.sendRedirect(request.getContextPath() + "/login.jsp");
    return;
  }
  @SuppressWarnings("unchecked")
  List<Pet> pets = (List<Pet>) request.getAttribute("pets");
  if (pets == null) {
    response.sendRedirect(request.getContextPath() + "/pet");
    return;
  }
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>My Pets - PawPulse</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body>
  <jsp:include page="header.jsp" />
  <div class="container py-4">
    <h2 class="mb-4">My Pets</h2>
    <% if (request.getAttribute("error") != null) { %>
      <div class="alert alert-warning"><%= request.getAttribute("error") %></div>
    <% } %>
    <p><a href="${pageContext.request.contextPath}/pet-form.jsp" class="btn btn-paw">+ Add Pet</a></p>
    <div class="row g-3">
      <% for (Pet p : pets) { %>
        <div class="col-md-6 col-lg-4">
          <div class="card card-pet h-100">
            <div class="card-header"><%= p.getName() %></div>
            <div class="card-body">
              <p class="mb-1"><strong>Species:</strong> <%= p.getSpecies() != null ? p.getSpecies() : "-" %></p>
              <p class="mb-1"><strong>Breed:</strong> <%= p.getBreed() != null && !p.getBreed().isEmpty() ? p.getBreed() : "-" %></p>
              <p class="mb-1"><strong>Age:</strong> <%= p.getAge() != null ? p.getAge() + " yrs" : "-" %></p>
              <p class="mb-1"><strong>Weight:</strong> <%= p.getWeight() != null ? p.getWeight() + " kg" : "-" %></p>
              <p class="mb-2"><strong>Gender:</strong> <%= p.getGender() != null && !p.getGender().isEmpty() ? p.getGender() : "-" %></p>
              <a href="${pageContext.request.contextPath}/health?action=history&petId=<%= p.getPetId() %>" class="btn btn-sm btn-paw me-1">Health Records</a>
              <a href="${pageContext.request.contextPath}/pet?action=edit&id=<%= p.getPetId() %>" class="btn btn-sm btn-outline-secondary me-1">Edit</a>
              <a href="${pageContext.request.contextPath}/pet?action=delete&id=<%= p.getPetId() %>" class="btn btn-sm btn-outline-danger" onclick="return confirm('Delete this pet?');">Delete</a>
            </div>
          </div>
        </div>
      <% } %>
    </div>
    <% if (pets.isEmpty()) { %>
      <p class="text-muted mt-3">No pets added yet. <a href="${pageContext.request.contextPath}/pet-form.jsp">Add your first pet</a>.</p>
    <% } %>
  </div>
  <jsp:include page="footer.jsp" />
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
