<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User" %>
<%@ page import="model.Pet" %>
<%
  User user = (User) session.getAttribute("user");
  if (user == null) {
    response.sendRedirect(request.getContextPath() + "/login.jsp");
    return;
  }
  Pet pet = (Pet) request.getAttribute("pet");
  boolean isEdit = pet != null && pet.getPetId() > 0;
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title><%= isEdit ? "Edit" : "Add" %> Pet – PawPulse</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body>
  <jsp:include page="header.jsp" />
  <div class="container py-4">
    <h2 class="mb-4"><%= isEdit ? "Edit Pet" : "Add New Pet" %></h2>
    <% if (request.getAttribute("error") != null) { %>
      <div class="alert alert-danger"><%= request.getAttribute("error") %></div>
    <% } %>
    <div class="card card-pet">
      <div class="card-body">
        <form action="${pageContext.request.contextPath}/pet" method="post">
          <input type="hidden" name="action" value="<%= isEdit ? "save" : "add" %>">
          <% if (isEdit) { %><input type="hidden" name="petId" value="<%= pet.getPetId() %>"><% } %>
          <div class="row g-3">
            <div class="col-md-6">
              <label class="form-label">Name *</label>
              <input type="text" name="name" class="form-control" required value="<%= pet != null && pet.getName() != null ? pet.getName() : "" %>">
            </div>
            <div class="col-md-6">
              <label class="form-label">Species</label>
              <select name="species" class="form-select">
                <option value="Dog" <%= pet != null && "Dog".equals(pet.getSpecies()) ? "selected" : "" %>>Dog</option>
                <option value="Cat" <%= pet != null && "Cat".equals(pet.getSpecies()) ? "selected" : "" %>>Cat</option>
                <option value="Bird" <%= pet != null && "Bird".equals(pet.getSpecies()) ? "selected" : "" %>>Bird</option>
                <option value="Other" <%= pet != null && "Other".equals(pet.getSpecies()) ? "selected" : "" %>>Other</option>
              </select>
            </div>
            <div class="col-md-6">
              <label class="form-label">Breed</label>
              <input type="text" name="breed" class="form-control" value="<%= pet != null && pet.getBreed() != null ? pet.getBreed() : "" %>">
            </div>
            <div class="col-md-3">
              <label class="form-label">Age (years)</label>
              <input type="number" name="age" class="form-control" min="0" value="<%= pet != null && pet.getAge() != null ? pet.getAge() : "" %>">
            </div>
            <div class="col-md-3">
              <label class="form-label">Weight (kg)</label>
              <input type="number" name="weight" class="form-control" min="0" step="0.1" value="<%= pet != null && pet.getWeight() != null ? pet.getWeight() : "" %>">
            </div>
            <div class="col-md-6">
              <label class="form-label">Gender</label>
              <select name="gender" class="form-select">
                <option value="">—</option>
                <option value="Male" <%= pet != null && "Male".equals(pet.getGender()) ? "selected" : "" %>>Male</option>
                <option value="Female" <%= pet != null && "Female".equals(pet.getGender()) ? "selected" : "" %>>Female</option>
              </select>
            </div>
          </div>
          <div class="mt-4">
            <button type="submit" class="btn btn-paw"><%= isEdit ? "Save" : "Add Pet" %></button>
            <a href="${pageContext.request.contextPath}/pet" class="btn btn-outline-secondary">Cancel</a>
          </div>
        </form>
      </div>
    </div>
  </div>
  <jsp:include page="footer.jsp" />
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
