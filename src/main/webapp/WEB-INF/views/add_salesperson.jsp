<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"  uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Add Salesperson</title>
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">

<%@ include file="/WEB-INF/jspf/header.jsp" %>

<div class="container py-4">
    <h1 class="mb-4">Add Salesperson</h1>

    <c:if test="${not empty errors}">
        <div class="alert alert-danger">
            <h6 class="mb-2">Please fix the following problems:</h6>
            <ul class="mb-0">
                <c:forEach var="e" items="${errors}">
                    <li>${e}</li>
                </c:forEach>
            </ul>
        </div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/salespersons/add"
          class="row g-3 needs-validation" novalidate>

        <div class="col-md-4">
            <label class="form-label">First Name</label>
            <input name="firstName" type="text" class="form-control"
                   value="${fn:escapeXml(param.firstName)}" required>
        </div>

        <div class="col-md-4">
            <label class="form-label">Last Name</label>
            <input name="lastName" type="text" class="form-control"
                   value="${fn:escapeXml(param.lastName)}" required>
        </div>

        <div class="col-md-4">
            <label class="form-label">Email</label>
            <input name="email" type="email" class="form-control"
                   value="${fn:escapeXml(param.email)}" required>
        </div>

        <div class="col-md-4">
            <label class="form-label">Phone</label>
            <input name="phone" type="text" class="form-control"
                   value="${fn:escapeXml(param.phone)}">
        </div>

        <div class="col-md-4">
            <label class="form-label">Commission Rate (%)</label>
            <input name="commissionRate" type="number" step="0.01" class="form-control"
                   value="${fn:escapeXml(param.commissionRate)}" required>
        </div>

        <div class="col-12 d-grid mt-3">
            <button type="submit" class="btn btn-primary btn-lg">Create Salesperson</button>
        </div>
    </form>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
