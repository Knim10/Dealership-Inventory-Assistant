<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Login — Dealership Inventory Assistant</title>
  <link rel="stylesheet"
        href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body class="bg-light d-flex align-items-center" style="min-height:100vh">
<div class="container">
  <div class="row justify-content-center">
    <div class="col-sm-10 col-md-6 col-lg-4">
      <div class="card shadow-sm">
        <div class="card-body">
          <h1 class="h4 mb-3">Sign in</h1>

          <c:if test="${not empty error}">
            <div class="alert alert-danger py-2">${error}</div>
          </c:if>

          <form method="post" action="${pageContext.request.contextPath}/login" novalidate>
            <div class="mb-3">
              <label class="form-label">Username</label>
              <input name="username" class="form-control" autocomplete="username" required>
            </div>
            <div class="mb-3">
              <label class="form-label">Password</label>
              <input name="password" type="password" class="form-control"
                     autocomplete="current-password" required>
            </div>
            <div class="d-grid">
              <button class="btn btn-primary" type="submit">Log In</button>
            </div>
          </form>

          <form method="post" action="${pageContext.request.contextPath}/logout" class="mt-3">
            <button class="btn btn-outline-secondary w-100" type="submit">Logout (debug)</button>
          </form>
        </div>
      </div>
      <div class="text-center text-muted mt-2">
        <small>&copy; Dealership Inventory Assistant</small>
      </div>
    </div>
  </div>
</div>
</body>
</html>
