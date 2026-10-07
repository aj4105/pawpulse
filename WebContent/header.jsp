<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<nav class="navbar navbar-expand-lg navbar-dark navbar-paw">
  <div class="container">
    <a class="navbar-brand" href="${pageContext.request.contextPath}/dashboard.jsp">
      <span class="paw-icon">🐾</span> PawPulse
    </a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navMenu">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navMenu">
      <ul class="navbar-nav ms-auto">
        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/dashboard.jsp">Home</a></li>
        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/pet">My Pets</a></li>
        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/health?action=suggest">AI Health</a></li>
        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/health?action=reminders">Reminders</a></li>
        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/health?action=vets">Vet Support</a></li>
        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/logout">Logout</a></li>
      </ul>
    </div>
  </div>
</nav>
