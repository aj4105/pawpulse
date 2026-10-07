<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  if (session != null && session.getAttribute("user") != null) {
    response.sendRedirect(request.getContextPath() + "/dashboard.jsp");
  } else {
    response.sendRedirect(request.getContextPath() + "/login.jsp");
  }
%>
