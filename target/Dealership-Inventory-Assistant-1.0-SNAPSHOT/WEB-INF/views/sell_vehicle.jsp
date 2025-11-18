<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Sell Vehicle</title>
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">

<%@ include file="/WEB-INF/jspf/header.jsp" %>

<div class="container py-4">
    <h1 class="mb-3">Sell Vehicle</h1>

    <c:if test="${empty vehicle}">
        <div class="alert alert-warning">Vehicle not found.</div>
    </c:if>

    <c:if test="${not empty vehicle}">
        <div class="card mb-3">
            <div class="card-body">
                <h5 class="card-title">
                    ${vehicle.year} ${vehicle.make} ${vehicle.model}
                </h5>
                <p class="card-text mb-1">
                    Color: ${vehicle.color} &mdash; Category: ${vehicle.category}
                </p>
                <p class="card-text mb-0">
                    List Price: <strong>${vehicle.price}</strong>
                    <c:if test="${not empty salesperson}">
                        &nbsp; | Commission Rate:
                        <strong>${salesperson.commissionRate}%</strong>
                    </c:if>
                </p>
            </div>
        </div>

        <form method="post" action="${pageContext.request.contextPath}/vehicles/sell" class="row g-3">
            <input type="hidden" name="vehicleId" value="${vehicle.vehicleId}"/>

            <div class="col-md-4">
                <label class="form-label">Sale Date</label>
                <input type="date" name="saleDate" class="form-control"
                       value="${today}">
            </div>

            <div class="col-md-4">
                <label class="form-label">Sale Price</label>
                <input type="number" name="salePrice" step="0.01" class="form-control"
                       value="${vehicle.price}">
            </div>

            <!-- Admin-only: choose which salesperson gets credit -->
            <c:if test="${sessionScope.user.role == 'admin'}">
                <div class="col-md-4">
                    <label class="form-label">Salesperson</label>
                    <select name="salespersonId" class="form-select">
                        <option value="">Choose...</option>
                        <c:forEach var="sp" items="${salespeople}">
                            <option value="${sp.salespersonId}">
                                ${sp.firstName} ${sp.lastName}
                            </option>
                        </c:forEach>
                    </select>
                </div>
            </c:if>

            <div class="col-12 d-grid mt-3">
                <button type="submit" class="btn btn-success btn-lg">Confirm Sale</button>
            </div>
        </form>
    </c:if>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
