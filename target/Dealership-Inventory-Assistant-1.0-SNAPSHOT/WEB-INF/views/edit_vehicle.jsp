<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Edit Vehicle</title>
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">

<%@ include file="/WEB-INF/jspf/header.jsp" %>

<div class="container py-4">
    <h1 class="mb-4">Edit Vehicle</h1>

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

    <c:if test="${empty vehicle}">
        <div class="alert alert-warning">Vehicle not found.</div>
    </c:if>

    <c:if test="${not empty vehicle}">
        <form method="post" action="${pageContext.request.contextPath}/vehicles/edit"
              class="row g-3 needs-validation" novalidate>

            <input type="hidden" name="id" value="${vehicle.vehicleId}" />

            <div class="col-md-4">
                <label class="form-label">Make</label>
                <input name="make" type="text" class="form-control"
                       value="${fn:escapeXml(vehicle.make)}" required>
            </div>

            <div class="col-md-4">
                <label class="form-label">Model</label>
                <input name="model" type="text" class="form-control"
                       value="${fn:escapeXml(vehicle.model)}" required>
            </div>

            <div class="col-md-2">
                <label class="form-label">Year</label>
                <input name="year" type="number" class="form-control"
                       min="1980" max="2035" value="${vehicle.year}" required>
            </div>

            <div class="col-md-2">
                <label class="form-label">Color</label>
                <input name="color" type="text" class="form-control"
                       value="${fn:escapeXml(vehicle.color)}">
            </div>

            <div class="col-md-4">
                <label class="form-label">Category</label>
                <select name="category" class="form-select">
                    <c:set var="cat" value="${vehicle.category}" />
                    <option value="Car"   ${cat == 'Car'   ? 'selected' : ''}>Car</option>
                    <option value="Truck" ${cat == 'Truck' ? 'selected' : ''}>Truck</option>
                    <option value="SUV"   ${cat == 'SUV'   ? 'selected' : ''}>SUV</option>
                    <option value="Van"   ${cat == 'Van'   ? 'selected' : ''}>Van</option>
                    <option value="Other" ${cat == 'Other' ? 'selected' : ''}>Other</option>
                </select>
            </div>

            <div class="col-md-4">
                <label class="form-label">Cost</label>
                <input name="cost" type="number" step="0.01" class="form-control"
                       value="${vehicle.cost}" required>
            </div>

            <div class="col-md-4">
                <label class="form-label">Price</label>
                <input name="price" type="number" step="0.01" class="form-control"
                       value="${vehicle.price}" required>
            </div>

            <div class="col-md-4">
                <label class="form-label">Status</label>
                <select name="status" class="form-select">
                    <c:set var="st" value="${vehicle.status}" />
                    <option value="Available" ${st == 'Available' ? 'selected' : ''}>Available</option>
                    <option value="Reserved"  ${st == 'Reserved'  ? 'selected' : ''}>Reserved</option>
                    <option value="Sold"      ${st == 'Sold'      ? 'selected' : ''}>Sold</option>
                </select>
            </div>

            <div class="col-12 d-grid mt-3">
                <button type="submit" class="btn btn-primary btn-lg">Save Changes</button>
            </div>
        </form>
    </c:if>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
