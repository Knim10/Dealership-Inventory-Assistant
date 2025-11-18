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

<%@ include file="/WEB-INF/jspf/header.jsp" %>

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
