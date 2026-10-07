<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User" %>
<%
  User user = (User) session.getAttribute("user");
  if (user == null) {
    response.sendRedirect(request.getContextPath() + "/login.jsp");
    return;
  }
  String suggestion = (String) request.getAttribute("suggestion");
  String symptomsEntered = (String) request.getAttribute("symptomsEntered");
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>AI Health Suggestion – PawPulse</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body>
  <jsp:include page="header.jsp" />
  <div class="container py-4">
    <h2 class="mb-4">AI Health Analysis</h2>
    <p class="text-muted">Enter your pet's symptoms below. Our rule-based engine will suggest possible causes and when to see a vet. This is not a substitute for professional diagnosis.</p>
    <div class="card card-pet mb-4">
      <div class="card-body">
        <form action="${pageContext.request.contextPath}/health" method="post">
          <input type="hidden" name="action" value="suggest">
          <div class="mb-3">
            <label class="form-label">Symptoms (e.g., vomiting, lethargy, loss of appetite)</label>
            <textarea name="symptoms" class="form-control" rows="3" placeholder="Describe what you've noticed..."><%= symptomsEntered != null ? symptomsEntered : "" %></textarea>
          </div>
          <button type="submit" class="btn btn-paw">Get suggestion</button>
        </form>
      </div>
    </div>
    <% if (suggestion != null && !suggestion.isEmpty()) { %>
      <div class="card card-pet">
        <div class="card-header">Health suggestion</div>
        <div class="card-body">
          <div class="suggestion-box"><%= suggestion %></div>
        </div>
      </div>
    <% } %>
  </div>
  <jsp:include page="footer.jsp" />
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
