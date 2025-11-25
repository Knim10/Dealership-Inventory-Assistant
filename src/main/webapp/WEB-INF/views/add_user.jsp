<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Add User</title>
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">

<%@ include file="/WEB-INF/jspf/header.jsp" %>

<div class="container py-4">
    <h1 class="mb-3">Add User (Admin)</h1>
    <p class="text-muted">Create a user and optionally link to a salesperson.</p>

    <c:if test="${not empty errors}">
        <div class="alert alert-danger">
            <ul class="mb-0">
                <c:forEach var="e" items="${errors}">
                    <li>${e}</li>
                </c:forEach>
            </ul>
        </div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/admin/users/add" class="row g-3">

        <!-- User fields -->
        <div class="col-md-4">
            <label class="form-label">Username</label>
            <input name="username" class="form-control"
                   value="${fn:escapeXml(param.username)}" required>
        </div>

        <div class="col-md-4">
            <label class="form-label">Password</label>
            <input name="password" type="password" class="form-control" required>
        </div>

        <div class="col-md-4">
            <label class="form-label">Role</label>
            <select name="role" class="form-select" required>
                <option value="">Choose...</option>
                <option value="sales" ${param.role == 'sales' ? 'selected' : ''}>Sales</option>
                <option value="admin" ${param.role == 'admin' ? 'selected' : ''}>Admin</option>
            </select>
        </div>

        <hr class="mt-4">

        <!-- Salesperson link mode -->
        <div class="col-12">
            <label class="form-label fw-bold">Link to Salesperson</label>

            <div class="form-check">
                <input class="form-check-input" type="radio" name="salespersonMode"
                       id="modeExisting" value="existing"
                       ${empty param.salespersonMode || param.salespersonMode == 'existing' ? 'checked' : ''}>
                <label class="form-check-label" for="modeExisting">
                    Link to existing salesperson
                </label>
            </div>

            <div class="ms-4 mt-2">
                <select name="existingSalespersonId" class="form-select">
                    <option value="">Choose salesperson...</option>
                    <c:forEach var="sp" items="${salespeople}">
                        <option value="${sp.salespersonId}"
                            ${param.existingSalespersonId == sp.salespersonId ? 'selected' : ''}>
                            ${sp.firstName} ${sp.lastName}
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-check mt-3">
                <input class="form-check-input" type="radio" name="salespersonMode"
                       id="modeNew" value="new"
                       ${param.salespersonMode == 'new' ? 'checked' : ''}>
                <label class="form-check-label" for="modeNew">
                    Create new salesperson and link
                </label>
            </div>
        </div>

        <!-- New salesperson fields -->
        <div class="row g-3 ms-3 mt-2">
            <div class="col-md-4">
                <label class="form-label">First Name</label>
                <input name="spFirstName" class="form-control"
                       value="${fn:escapeXml(param.spFirstName)}">
            </div>

            <div class="col-md-4">
                <label class="form-label">Last Name</label>
                <input name="spLastName" class="form-control"
                       value="${fn:escapeXml(param.spLastName)}">
            </div>

            <div class="col-md-4">
                <label class="form-label">Email</label>
                <input name="spEmail" type="email" class="form-control"
                       value="${fn:escapeXml(param.spEmail)}">
            </div>

            <div class="col-md-4">
                <label class="form-label">Phone</label>
                <input name="spPhone" class="form-control"
                       value="${fn:escapeXml(param.spPhone)}">
            </div>

            <div class="col-md-4">
                <label class="form-label">Commission Rate (%)</label>
                <input name="spRate" type="number" step="0.01" class="form-control"
                       value="${fn:escapeXml(param.spRate)}">
            </div>
        </div>

        <div class="col-12 d-grid mt-4">
            <button class="btn btn-primary btn-lg">Create User</button>
        </div>

    </form>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
