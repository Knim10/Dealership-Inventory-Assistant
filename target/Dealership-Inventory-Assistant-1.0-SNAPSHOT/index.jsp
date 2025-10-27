<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Dealership Inventory Assistant</title>
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body class="bg-light d-flex flex-column min-vh-100">

<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/index.jsp">
            Dealership Inventory Assistant
        </a>
        <div class="navbar-nav">
            <a class="nav-link" href="${pageContext.request.contextPath}/vehicles">Inventory</a>
            <a class="nav-link disabled" href="#">Sales (Coming Soon)</a>
            <a class="nav-link disabled" href="#">Prospects (Coming Soon)</a>
        </div>
    </div>
</nav>

<main class="flex-grow-1">
    <div class="container text-center py-5">
        <h1 class="display-5 mb-3">Welcome to the Dealership Inventory Assistant</h1>
        <p class="lead text-muted mb-4">
            Manage your dealership’s vehicle inventory, prospects, and sales all in one place.
        </p>

        <div class="d-grid gap-3 d-sm-flex justify-content-sm-center">
            <a href="${pageContext.request.contextPath}/vehicles" class="btn btn-primary btn-lg px-4">
                View Vehicle Inventory
            </a>
            <a href="#" class="btn btn-outline-secondary btn-lg px-4 disabled">
                Login (Coming Soon)
            </a>
        </div>
    </div>
</main>

<footer class="bg-dark text-white-50 text-center py-3 mt-auto">
    <small>
        &copy; <c:out value="${pageContext.request.serverName}" /> —
        Dealership Inventory Assistant —
        <c:out value="${pageContext.request.contextPath}" />
    </small>
</footer>

</body>
</html>
