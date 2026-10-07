<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User" %>
<%@ page import="model.Pet" %>
<%@ page import="model.HealthRecord" %>
<%@ page import="java.util.List" %>
<%
  User user = (User) session.getAttribute("user");
  if (user == null) {
    response.sendRedirect(request.getContextPath() + "/login.jsp");
    return;
  }
  Pet pet = (Pet) request.getAttribute("pet");
  @SuppressWarnings("unchecked")
  List<HealthRecord> history = (List<HealthRecord>) request.getAttribute("healthHistory");
  if (history == null) history = java.util.Collections.emptyList();
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Health Records – PawPulse</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body>
  <jsp:include page="header.jsp" />
  <div class="container py-4">
    <% if (pet != null) { %>
      <h2 class="mb-4">Health Records – <%= pet.getName() %></h2>
      <% if (request.getAttribute("error") != null) { %>
        <div class="alert alert-warning"><%= request.getAttribute("error") %></div>
      <% } %>

      <div class="card card-pet mb-4">
        <div class="card-header">Add new record</div>
        <div class="card-body">
          <form action="${pageContext.request.contextPath}/health" method="post" class="row g-3">
            <input type="hidden" name="action" value="addRecord">
            <input type="hidden" name="petId" value="<%= pet.getPetId() %>">
            <div class="col-md-4">
              <label class="form-label">Type</label>
              <select name="recordType" class="form-select">
                <option value="Vaccination">Vaccination</option>
                <option value="Checkup">Checkup</option>
                <option value="Illness">Illness</option>
                <option value="Surgery">Surgery</option>
                <option value="Other">Other</option>
              </select>
            </div>
            <div class="col-md-4">
              <label class="form-label">Date</label>
              <input type="date" name="recordDate" class="form-control" value="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) %>">
            </div>
            <div class="col-md-4">
              <label class="form-label">Next due (e.g. for vaccination)</label>
              <input type="text" name="nextDueDate" class="form-control" placeholder="YYYY-MM-DD">
            </div>
            <div class="col-12">
              <label class="form-label">Description</label>
              <input type="text" name="description" class="form-control" placeholder="Brief description">
            </div>
            <div class="col-md-6">
              <label class="form-label">Vet name</label>
              <input type="text" name="vetName" class="form-control">
            </div>
            <div class="col-12">
              <button type="submit" class="btn btn-paw">Add Record</button>
            </div>
          </form>
        </div>
      </div>

      <div class="card card-pet">
        <div class="card-header">Health history</div>
        <div class="card-body p-0">
          <% if (history.isEmpty()) { %>
            <p class="p-4 text-muted mb-0">No health records yet.</p>
          <% } else { %>
            <div class="table-responsive">
              <table class="table table-health table-hover mb-0">
                <thead>
                  <tr><th>Date</th><th>Type</th><th>Description</th><th>Vet</th><th>Next due</th></tr>
                </thead>
                <tbody>
                  <% for (HealthRecord r : history) { %>
                    <tr>
                      <td><%= r.getRecordDate() != null ? new java.text.SimpleDateFormat("MMM d, yyyy").format(r.getRecordDate()) : "—" %></td>
                      <td><%= r.getRecordType() != null ? r.getRecordType() : "—" %></td>
                      <td><%= r.getDescription() != null ? r.getDescription() : "—" %></td>
                      <td><%= r.getVetName() != null ? r.getVetName() : "—" %></td>
                      <td><%= r.getNextDueDate() != null ? r.getNextDueDate() : "—" %></td>
                    </tr>
                  <% } %>
                </tbody>
              </table>
            </div>
          <% } %>
        </div>
      </div>
      <p class="mt-3"><a href="${pageContext.request.contextPath}/pet" class="btn btn-outline-secondary">Back to My Pets</a></p>
    <% } else { %>
      <p class="text-muted">Select a pet to view health records. <a href="${pageContext.request.contextPath}/pet">My Pets</a></p>
    <% } %>
  </div>
  <jsp:include page="footer.jsp" />
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
