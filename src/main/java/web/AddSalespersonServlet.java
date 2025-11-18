package web;

import dao.SalespersonDao;
import jdbc.JdbcSalespersonDao;
import model.Salesperson;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "AddSalespersonServlet", urlPatterns = {"/salespersons/add"})
public class AddSalespersonServlet extends HttpServlet {

    private SalespersonDao salespersonDao;

    @Override
    public void init() {
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

        req.getRequestDispatcher("/WEB-INF/views/add_salesperson.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (!isAdmin(session)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return;
        }

        req.setCharacterEncoding("UTF-8");

        String firstName = trim(req.getParameter("firstName"));
        String lastName  = trim(req.getParameter("lastName"));
        String email     = trim(req.getParameter("email"));
        String phone     = trim(req.getParameter("phone"));
        String rateStr   = trim(req.getParameter("commissionRate"));

        List<String> errors = new ArrayList<>();

        if (firstName == null) errors.add("First name is required.");
        if (lastName == null)  errors.add("Last name is required.");
        if (email == null)     errors.add("Email is required.");

        Double rate = null;
        try {
            rate = (rateStr == null) ? null : Double.parseDouble(rateStr);
            if (rate == null || rate < 0 || rate > 100) {
                errors.add("Commission rate must be between 0 and 100.");
            }
        } catch (NumberFormatException e) {
            errors.add("Commission rate must be a valid number.");
        }

        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            req.getRequestDispatcher("/WEB-INF/views/add_salesperson.jsp").forward(req, resp);
            return;
        }

        Salesperson sp = new Salesperson();
        sp.setFirstName(firstName);
        sp.setLastName(lastName);
        sp.setEmail(email);
        sp.setPhone(phone);
        sp.setCommissionRate(rate);

        int newId = salespersonDao.create(sp);
        if (newId > 0) {
            session.setAttribute("flashSuccess",
                    "Salesperson created successfully (ID " + newId + ").");
        } else {
            session.setAttribute("flashError",
                    "There was a problem creating the new salesperson.");
        }

        // Redirect to sales report so the new salesperson is visible in the dropdown
        resp.sendRedirect(req.getContextPath() + "/sales/report");
    }

    private String trim(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
