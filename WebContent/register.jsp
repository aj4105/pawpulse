<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  if (session != null && session.getAttribute("user") != null) {
    response.sendRedirect(request.getContextPath() + "/dashboard.jsp");
    return;
  }
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Register – PawPulse</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body>
  <div class="container py-5">
    <div class="row justify-content-center">
      <div class="col-md-6">
        <div class="card card-pet">
          <div class="card-header text-center py-3">🐾 Create your PawPulse account</div>
          <div class="card-body p-4">
            <% if (request.getAttribute("error") != null) { %>
              <div class="alert alert-danger"><%= request.getAttribute("error") %></div>
            <% } %>
            <form action="${pageContext.request.contextPath}/user" method="post">
              <input type="hidden" name="action" value="register">
              <div class="mb-3">
                <label class="form-label">Email</label>
                <input type="email" name="email" class="form-control" required placeholder="you@example.com">
              </div>
              <div class="mb-3">
                <label class="form-label">Password</label>
                <input type="password" name="password" class="form-control" required minlength="6">
              </div>
              <div class="mb-3">
                <label class="form-label">Full name</label>
                <input type="text" name="fullName" class="form-control" placeholder="Your name">
              </div>
              <div class="mb-3">
                <label class="form-label">Phone</label>
                <input type="text" name="phone" class="form-control" placeholder="Phone number">
              </div>
              <button type="submit" class="btn btn-paw w-100">Register</button>
            </form>
            <p class="mt-3 text-center mb-0">
              Already have an account? <a href="${pageContext.request.contextPath}/login.jsp">Login</a>
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
