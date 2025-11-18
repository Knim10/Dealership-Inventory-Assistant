<!-- src/main/webapp/WEB-INF/views/sales_report.jsp -->
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"  uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Sales Report</title>
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">

<%@ include file="/WEB-INF/jspf/header.jsp" %>

<div class="container py-4">
    <h1 class="mb-3">Sales Report</h1>
    <p class="text-muted">Admin-only report of sales, filtered by date range and salesperson.</p>
    
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


    <!-- Filter form -->
    <form method="get" action="${pageContext.request.contextPath}/sales/report" class="row g-3 mb-4">
        <div class="col-md-3">
            <label class="form-label">From Date</label>
            <input type="date" name="from" class="form-control"
                   value="${fromDate}">
        </div>
        <div class="col-md-3">
            <label class="form-label">To Date</label>
            <input type="date" name="to" class="form-control"
                   value="${toDate}">
        </div>
        <div class="col-md-4">
            <label class="form-label">Salesperson</label>
            <select name="salespersonId" class="form-select">
                <option value="">All Salespersons</option>
                <c:forEach var="sp" items="${salespeople}">
                    <option value="${sp.salespersonId}"
                        <c:if test="${selectedSalespersonId == sp.salespersonId}">selected</c:if>>
                        ${sp.firstName} ${sp.lastName}
                    </option>
                </c:forEach>
            </select>
        </div>
        <div class="col-md-2 d-flex align-items-end">
            <button type="submit" class="btn btn-primary w-100">Run Report</button>
        </div>
    </form>

    <!-- Summary -->
    <div class="row mb-3">
        <div class="col-md-4">
            <div class="card shadow-sm">
                <div class="card-body text-center">
                    <div class="text-muted small">Total Sales</div>
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
                    <div class="text-muted small">Total Commission</div>
                    <div class="fs-4 fw-bold">
                        <fmt:formatNumber value="${totalCommission}" type="currency"/>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Details table -->
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
                                <th>Salesperson ID</th>
                                <th>Prospect ID</th>
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
                                    <td>${s.salespersonId}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${s.prospectId != null}">
                                                ${s.prospectId}
                                            </c:when>
                                            <c:otherwise>
                                                <span class="text-muted">N/A</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="text-end">
                                        <fmt:formatNumber value="${s.salePrice}" type="currency"/>
                                    </td>
                                    <td class="text-end">
                                        <c:choose>
                                            <c:when test="${s.commissionEarned != null}">
                                                <fmt:formatNumber value="${s.commissionEarned}" type="currency"/>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="text-muted">N/A</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="p-4">
                        <p class="mb-0 text-muted">
                            No sales found for the selected filters.
                        </p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

</div>
                    

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
