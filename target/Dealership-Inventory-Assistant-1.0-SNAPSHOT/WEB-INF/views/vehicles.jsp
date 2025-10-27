<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Dealership Inventory</title>
    <!-- Optional Bootstrap (local or CDN) -->
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">
<div class="container py-4">
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
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="p-4">
                        <p class="mb-0 text-muted">No vehicles found. Try adjusting your search or seed the database.</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <div class="mt-3 text-muted">
        <small>Total: <strong><c:out value='${fn:length(vehicles)}' /></strong></small>
    </div>
</div>
</body>
</html>
