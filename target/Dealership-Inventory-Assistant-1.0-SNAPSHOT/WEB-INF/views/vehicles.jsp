<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Dealership Inventory</title>
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">

    <%@ include file="/WEB-INF/jspf/header.jsp" %>

<div class="container py-4">

    <!-- Determine current role once -->
    <c:set var="currentUser" value="${sessionScope.user}" />
    <c:set var="role" value="${empty currentUser ? '' : currentUser.role}" />

    <!-- Admin-only: Add Vehicle -->
    <c:if test="${role == 'admin'}">
        <div class="mb-3 text-end">
            <a href="${pageContext.request.contextPath}/vehicles/add"
               class="btn btn-success">
                + Add New Vehicle
            </a>
        </div>
    </c:if>

    <!-- Flash messages -->
    <c:if test="${not empty sessionScope.flashSuccess}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            ${sessionScope.flashSuccess}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <c:remove var="flashSuccess" scope="session"/>
    </c:if>

    <c:if test="${not empty sessionScope.flashError}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            ${sessionScope.flashError}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <c:remove var="flashError" scope="session"/>
    </c:if>

    <h1 class="mb-3">Dealership Inventory</h1>

    <!-- Search Form -->
    <form method="get" action="${pageContext.request.contextPath}/vehicles" class="row g-2 mb-4">
        <div class="col-sm-3">
            <input class="form-control" type="text" name="make" placeholder="Make" value="${param.make}">
        </div>
        <div class="col-sm-3">
            <input class="form-control" type="text" name="model" placeholder="Model" value="${param.model}">
        </div>
        <div class="col-sm-2">
            <input class="form-control" type="text" name="year" placeholder="Year" value="${param.year}">
        </div>
        <div class="col-sm-2">
            <input class="form-control" type="text" name="color" placeholder="Color" value="${param.color}">
        </div>
        <div class="col-sm-2 d-grid">
            <button class="btn btn-primary" type="submit">Search</button>
        </div>
    </form>

    <!-- Table -->
    <div class="card shadow-sm">
        <div class="card-body p-0">
            <c:choose>
                <c:when test="${not empty vehicles}">
                    <div class="table-responsive">
                        <table class="table table-striped table-hover mb-0 align-middle">
                            <thead class="table-secondary">
                            <tr>
                                <th>ID</th>
                                <th>Make</th>
                                <th>Model</th>
                                <th>Year</th>
                                <th>Color</th>
                                <th>Category</th>
                                <th class="text-end">Cost</th>
                                <th class="text-end">Price</th>
                                <th>Status</th>
                                <c:if test="${role == 'admin' or role == 'sales'}">
                                    <th>Actions</th>
                                </c:if>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="v" items="${vehicles}">
                                <tr>
                                    <td>${v.vehicleId}</td>
                                    <td>${v.make}</td>
                                    <td>${v.model}</td>
                                    <td>${v.year}</td>
                                    <td>${v.color}</td>
                                    <td>${v.category}</td>
                                    <td class="text-end">
                                        <c:out value="${v.cost}" />
                                    </td>
                                    <td class="text-end">
                                        <c:out value="${v.price}" />
                                    </td>
                                    <td>
                                        <span class="badge
                                            <c:choose>
                                                <c:when test='${v.status == "Available"}'>bg-success</c:when>
                                                <c:when test='${v.status == "Reserved"}'>bg-warning text-dark</c:when>
                                                <c:otherwise>bg-secondary</c:otherwise>
                                            </c:choose>">
                                            ${v.status}
                                        </span>
                                    </td>

                                    <c:if test="${role == 'admin' or role == 'sales'}">
                                        <td>
                                            <div class="d-flex flex-wrap gap-1">

                                                <!-- Sell button: visible to admin & sales for Available vehicles -->
                                                <c:if test="${v.status == 'Available'}">
                                                    <a class="btn btn-sm btn-success"
                                                       href="${pageContext.request.contextPath}/vehicles/sell?id=${v.vehicleId}">
                                                        Sell
                                                    </a>
                                                </c:if>

                                                <!-- Admin-only Edit/Delete -->
                                                <c:if test="${role == 'admin'}">
                                                    <a class="btn btn-sm btn-outline-primary"
                                                       href="${pageContext.request.contextPath}/vehicles/edit?id=${v.vehicleId}">
                                                        Edit
                                                    </a>
                                                    <form method="post"
                                                          action="${pageContext.request.contextPath}/vehicles/delete"
                                                          onsubmit="return confirm('Delete vehicle ID ${v.vehicleId}?');">
                                                        <input type="hidden" name="id" value="${v.vehicleId}">
                                                        <button type="submit" class="btn btn-sm btn-outline-danger">
                                                            Delete
                                                        </button>
                                                    </form>
                                                </c:if>

                                            </div>
                                        </td>
                                    </c:if>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="p-4">
                        <p class="mb-0 text-muted">
                            No vehicles found. Try adjusting your search or seed the database.
                        </p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <div class="mt-3 text-muted">
        <small>Total: <strong><c:out value="${fn:length(vehicles)}" /></strong></small>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
