<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Add New Vehicle</title>
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">

<%@ include file="/WEB-INF/jspf/header.jsp" %>

<div class="container py-4">
    <h1 class="mb-4">Add New Vehicle</h1>

    <!-- Global error list -->
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

    <form method="post" action="${pageContext.request.contextPath}/vehicles/add"
          class="row g-3 needs-validation" novalidate>

        <div class="col-md-4">
            <label class="form-label">Make</label>
            <input name="make" type="text" class="form-control"
                   value="${fn:escapeXml(param.make)}" required>
        </div>

        <div class="col-md-4">
            <label class="form-label">Model</label>
            <input name="model" type="text" class="form-control"
                   value="${fn:escapeXml(param.model)}" required>
        </div>

        <div class="col-md-2">
            <label class="form-label">Year</label>
            <input name="year" type="number" class="form-control"
                   min="1980" max="2035" value="${fn:escapeXml(param.year)}" required>
        </div>

        <div class="col-md-2">
            <label class="form-label">Color</label>
            <input name="color" type="text" class="form-control"
                   value="${fn:escapeXml(param.color)}">
        </div>

        <div class="col-md-4">
            <label class="form-label">Category</label>
            <select name="category" class="form-select">
                <c:set var="cat" value="${empty param.category ? 'Car' : param.category}" />
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
                   value="${fn:escapeXml(param.cost)}" required>
        </div>

        <div class="col-md-4">
            <label class="form-label">Price</label>
            <input name="price" type="number" step="0.01" class="form-control"
                   value="${fn:escapeXml(param.price)}" required>
        </div>

        <div class="col-12 d-grid mt-3">
            <button type="submit" class="btn btn-primary btn-lg">Add Vehicle</button>
        </div>
    </form>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
