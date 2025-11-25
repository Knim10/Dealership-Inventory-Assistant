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
import java.util.logging.Logger;
import java.util.logging.Level;

@WebServlet(name = "MySalesServlet", urlPatterns = {"/sales/mine"})
public class MySalesServlet extends HttpServlet {

    private SaleDao saleDao;
    private static final Logger log = Logger.getLogger(MySalesServlet.class.getName());

    @Override
    public void init() {
        this.saleDao = new JdbcSaleDao();
    }

private boolean isSales(User u) {
    if (u == null) {
        log.warning("isSales check failed: user is null");
        return false;
    }

    String role = (u.getRole() == null) ? "" : u.getRole().trim();
    Integer spId = u.getSalespersonId();

    log.log(Level.INFO,
            "[DEBUG MINE] username={0}, role='{1}', salespersonId={2}",
            new Object[]{ u.getUsername(), role, spId });

    return "sales".equalsIgnoreCase(role) && spId != null;
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

        // Date filters (default: last 30 days)
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

        List<Sale> sales = saleDao.findBySalesperson(salespersonId, from, to);

        // Compute totals
        int totalCount = sales.size();
        double totalSalesAmount = 0.0;
        double totalCommission  = 0.0;

        for (Sale s : sales) {
            if (s.getSalePrice() != null) totalSalesAmount += s.getSalePrice();
            if (s.getCommissionEarned() != null) totalCommission += s.getCommissionEarned();
        }

        // Send to JSP
        req.setAttribute("sales", sales);
        req.setAttribute("totalCount", totalCount);
        req.setAttribute("totalSalesAmount", totalSalesAmount);
        req.setAttribute("totalCommission", totalCommission);
        req.setAttribute("fromDate", from);
        req.setAttribute("toDate", to);

        req.getRequestDispatcher("/WEB-INF/views/my_sales.jsp").forward(req, resp);
    }
}
