// src/main/java/web/SalesReportServlet.java
package web;

import dao.SaleDao;
import dao.SalespersonDao;
import jdbc.JdbcSaleDao;
import jdbc.JdbcSalespersonDao;
import model.Sale;
import model.Salesperson;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@WebServlet(name = "SalesReportServlet", urlPatterns = {"/sales/report"})
public class SalesReportServlet extends HttpServlet {

    private SaleDao saleDao;
    private SalespersonDao salespersonDao;

    @Override
    public void init() {
        this.saleDao = new JdbcSaleDao();
        this.salespersonDao = new JdbcSalespersonDao();
    }

    private boolean isAdmin(HttpSession session) {
        if (session == null) return false;
        User u = (User) session.getAttribute("user");
        return u != null && "admin".equalsIgnoreCase(u.getRole());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (!isAdmin(session)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return;
        }

        // All salespersons for dropdown
        List<Salesperson> salespeople = salespersonDao.findAll();
        //System.out.println("[DEBUG] salespeople size = " + salespeople.size()); // uncomment for debugging
        req.setAttribute("salespeople", salespeople);

        // Parse filters
        String salespersonIdStr = req.getParameter("salespersonId");
        String fromStr = req.getParameter("from");
        String toStr   = req.getParameter("to");

        LocalDate today = LocalDate.now();
        LocalDate from = today.minusDays(30);
        LocalDate to = today;

        if (fromStr != null && !fromStr.isBlank()) {
            try { from = LocalDate.parse(fromStr); } catch (DateTimeParseException ignored) {}
        }
        if (toStr != null && !toStr.isBlank()) {
            try { to = LocalDate.parse(toStr); } catch (DateTimeParseException ignored) {}
        }

        Integer salespersonId = null;
        if (salespersonIdStr != null && !salespersonIdStr.isBlank()) {
            try { salespersonId = Integer.parseInt(salespersonIdStr); } catch (NumberFormatException ignored) {}
        }

        List<Sale> sales;
        if (salespersonId != null && salespersonId > 0) {
            sales = saleDao.findBySalesperson(salespersonId, from, to);
        } else {
            sales = saleDao.findByDateRange(from, to);
        }

        // Compute totals
        int totalCount = sales.size();
        double totalSalesAmount = 0.0;
        double totalCommission  = 0.0;
        for (Sale s : sales) {
            if (s.getSalePrice() != null) totalSalesAmount += s.getSalePrice();
            if (s.getCommissionEarned() != null) totalCommission += s.getCommissionEarned();
        }

        // Expose to JSP
        req.setAttribute("sales", sales);
        req.setAttribute("totalCount", totalCount);
        req.setAttribute("totalSalesAmount", totalSalesAmount);
        req.setAttribute("totalCommission", totalCommission);
        req.setAttribute("fromDate", from);
        req.setAttribute("toDate", to);
        req.setAttribute("selectedSalespersonId", salespersonId);

        req.getRequestDispatcher("/WEB-INF/views/sales_report.jsp").forward(req, resp);
    }
}
