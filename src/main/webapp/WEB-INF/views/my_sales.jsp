<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"  uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>My Sales</title>
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>

<body class="bg-light">

<%@ include file="/WEB-INF/jspf/header.jsp" %>

<div class="container py-4">

    <h1 class="mb-3">My Sales</h1>
    <p class="text-muted">Sales activity for your account.</p>

    <!-- Filter form -->
    <form method="get" action="${pageContext.request.contextPath}/sales/mine" class="row g-3 mb-4">
        <div class="col-md-3">
            <label class="form-label">From Date</label>
            <input type="date" name="from" class="form-control" value="${fromDate}">
        </div>

        <div class="col-md-3">
            <label class="form-label">To Date</label>
            <input type="date" name="to" class="form-control" value="${toDate}">
        </div>

        <div class="col-md-2 d-flex align-items-end">
            <button class="btn btn-primary w-100">Filter</button>
        </div>
    </form>

    <!-- Summary Cards -->
    <div class="row mb-3">
        <div class="col-md-4">
            <div class="card shadow-sm">
                <div class="card-body text-center">
                    <div class="text-muted small">Total Vehicles Sold</div>
                    <div class="fs-4 fw-bold">${totalCount}</div>
                </div>
            </div>
        </div>

        <div class="col-md-4 mt-3 mt-md-0">
            <div class="card shadow-sm">
                <div class="card-body text-center">
                    <div class="text-muted small">Total Sales Amount</div>
                    <div class="fs-4 fw-bold">
                        <fmt:formatNumber value="${totalSalesAmount}" type="currency"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-md-4 mt-3 mt-md-0">
            <div class="card shadow-sm">
                <div class="card-body text-center">
                    <div class="text-muted small">Total Commission Earned</div>
                    <div class="fs-4 fw-bold">
                        <fmt:formatNumber value="${totalCommission}" type="currency"/>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Sales Table -->
    <div class="card shadow-sm">
        <div class="card-body p-0">
            
            <c:choose>
                <c:when test="${not empty sales}">
                    <div class="table-responsive">
                        <table class="table table-striped table-hover mb-0 align-middle">
                            <thead class="table-secondary">
                            <tr>
                                <th>Sale ID</th>
                                <th>Date</th>
                                <th>Vehicle ID</th>
                                <th class="text-end">Sale Price</th>
                                <th class="text-end">Commission</th>
                            </tr>
                            </thead>

                            <tbody>
                            <c:forEach var="s" items="${sales}">
                                <tr>
                                    <td>${s.saleId}</td>
                                    <td>${s.saleDate}</td>
                                    <td>${s.vehicleId}</td>

                                    <td class="text-end">
                                        <fmt:formatNumber value="${s.salePrice}" type="currency"/>
                                    </td>

                                    <td class="text-end">
                                        <fmt:formatNumber value="${s.commissionEarned}" type="currency"/>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:when>

                <c:otherwise>
                    <div class="p-4 text-muted">No sales in this timeframe.</div>
                </c:otherwise>

            </c:choose>

        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
