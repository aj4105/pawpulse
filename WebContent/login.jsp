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
  <title>Login – PawPulse</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body>
  <div class="container py-5">
    <div class="row justify-content-center">
      <div class="col-md-5">
        <div class="card card-pet">
          <div class="card-header text-center py-3">🐾 Sign in to PawPulse</div>
          <div class="card-body p-4">
            <% if (request.getAttribute("error") != null) { %>
              <div class="alert alert-danger"><%= request.getAttribute("error") %></div>
            <% } %>
            <form action="${pageContext.request.contextPath}/user" method="post">
              <input type="hidden" name="action" value="login">
              <div class="mb-3">
                <label class="form-label">Email</label>
                <input type="email" name="email" class="form-control" required placeholder="you@example.com">
              </div>
              <div class="mb-3">
                <label class="form-label">Password</label>
                <input type="password" name="password" class="form-control" required>
              </div>
              <button type="submit" class="btn btn-paw w-100">Login</button>
            </form>
            <p class="mt-3 text-center mb-0">
              Don't have an account? <a href="${pageContext.request.contextPath}/register.jsp">Register</a>
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
