// src/main/java/web/MySalesServlet.java
package web;

import dao.SaleDao;
import jdbc.JdbcSaleDao;
import model.Sale;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@WebServlet(name = "MySalesServlet", urlPatterns = {"/sales/mine"})
public class MySalesServlet extends HttpServlet {

    private SaleDao saleDao;

    @Override
    public void init() {
        this.saleDao = new JdbcSaleDao();
    }

    private boolean isSales(User u) {
        return u != null && "sales".equalsIgnoreCase(u.getRole()) && u.getSalespersonId() != null;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (!isSales(user)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Only sales users can view this page.");
            return;
        }

        Integer salespersonId = user.getSalespersonId();
        if (salespersonId == null) {
            session.setAttribute("flashError", "Your account is not linked to a salesperson record.");
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        // Date filters (default: last 30 days)
        String fromStr = req.getParameter("from");
        String toStr   = req.getParameter("to");

        LocalDate today = LocalDate.now();
        LocalDate from = today.minusDays(30);
        LocalDate to   = today;

        if (fromStr != null && !fromStr.isBlank()) {
            try { from = LocalDate.parse(fromStr); } catch (DateTimeParseException ignored) {}
        }
        if (toStr != null && !toStr.isBlank()) {
            try { to = LocalDate.parse(toStr); } catch (DateTimeParseException ignored) {}
        }

        List<Sale> sales = saleDao.findBySalesperson(salespersonId, from, to);

        int totalCount = sales.size();
        double totalSalesAmount = 0.0;
        double totalCommission  = 0.0;
        for (Sale s : sales) {
            if (s.getSalePrice() != null) totalSalesAmount += s.getSalePrice();
            if (s.getCommissionEarned() != null) totalCommission += s.getCommissionEarned();
        }

        req.setAttribute("sales", sales);
        req.setAttribute("totalCount", totalCount);
        req.setAttribute("totalSalesAmount", totalSalesAmount);
        req.setAttribute("totalCommission", totalCommission);
        req.setAttribute("fromDate", from);
        req.setAttribute("toDate", to);

        req.getRequestDispatcher("/WEB-INF/views/my_sales.jsp").forward(req, resp);
    }
}
