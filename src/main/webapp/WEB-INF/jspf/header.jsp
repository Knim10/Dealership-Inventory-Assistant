<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page isELIgnored="false" %>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/index.jsp">
            Dealership Inventory Assistant
        </a>

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                data-bs-target="#mainNavbar" aria-controls="mainNavbar"
                aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="mainNavbar">
            <!-- LEFT SIDE NAV LINKS -->
            <div class="navbar-nav me-auto">
                <a class="nav-link" href="${pageContext.request.contextPath}/vehicles">Inventory</a>

                <!-- Admin-only links -->
                <c:if test="${not empty sessionScope.user and sessionScope.user.role == 'admin'}">
                    <a class="nav-link" href="${pageContext.request.contextPath}/vehicles/add">Add Vehicle</a>
                    <a class="nav-link" href="${pageContext.request.contextPath}/sales/report">Sales Report</a>
                    <a class="nav-link" href="${pageContext.request.contextPath}/salespersons/add">Add Salesperson</a>
                </c:if>

                <!-- Sales-only links -->
                <c:if test="${not empty sessionScope.user and sessionScope.user.role == 'sales'}">
                    <a class="nav-link" href="${pageContext.request.contextPath}/sales/mine">My Sales</a>
                </c:if>

                <!-- Placeholder links for upcoming features -->
                <a class="nav-link disabled" href="#">Prospects (Coming Soon)</a>
            </div>

            <!-- RIGHT SIDE USER / LOGIN AREA -->
            <div class="d-flex align-items-center">
                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <span class="navbar-text text-light me-3">
                            Logged in as <strong>${sessionScope.user.username}</strong>
                        </span>
                        <a href="${pageContext.request.contextPath}/logout"
                           class="btn btn-sm btn-outline-light">Logout</a>
                    </c:when>
                    <c:otherwise>
                        <a class="btn btn-outline-light btn-sm"
                           href="${pageContext.request.contextPath}/login">Login</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</nav>
