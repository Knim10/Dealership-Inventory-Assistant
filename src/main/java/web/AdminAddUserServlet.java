package web;

import dao.SalespersonDao;
import dao.UserDao;
import jdbc.JdbcSalespersonDao;
import jdbc.JdbcUserDao;
import model.Salesperson;
import model.User;
import org.mindrot.jbcrypt.BCrypt;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "AdminAddUserServlet", urlPatterns = {"/admin/users/add"})
public class AdminAddUserServlet extends HttpServlet {

    private UserDao userDao;
    private SalespersonDao salespersonDao;

    @Override
    public void init() {
        this.userDao = new JdbcUserDao();
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
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Admins only.");
            return;
        }

        req.setAttribute("salespeople", salespersonDao.findAll());
        req.getRequestDispatcher("/WEB-INF/views/add_user.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (!isAdmin(session)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Admins only.");
            return;
        }

        req.setCharacterEncoding("UTF-8");

        String username = trim(req.getParameter("username"));
        String password = trim(req.getParameter("password"));
        String role     = trim(req.getParameter("role"));

        // radio choice: "existing" or "new"
        String salespersonMode = trim(req.getParameter("salespersonMode"));

        List<String> errors = new ArrayList<>();

        if (username == null) errors.add("Username is required.");
        if (password == null) errors.add("Password is required.");
        if (role == null) errors.add("Role is required.");

        if (password != null && password.length() < 6) {
            errors.add("Password must be at least 6 characters.");
        }

        Integer salespersonIdToLink = null;

        if ("existing".equalsIgnoreCase(salespersonMode)) {
            String spIdStr = trim(req.getParameter("existingSalespersonId"));
            if (spIdStr == null) {
                errors.add("Choose an existing salesperson to link.");
            } else {
                try {
                    salespersonIdToLink = Integer.parseInt(spIdStr);
                } catch (NumberFormatException e) {
                    errors.add("Invalid salesperson selection.");
                }
            }
        }
        else if ("new".equalsIgnoreCase(salespersonMode)) {
            // fields for new salesperson
            String fn   = trim(req.getParameter("spFirstName"));
            String ln   = trim(req.getParameter("spLastName"));
            String email= trim(req.getParameter("spEmail"));
            String phone= trim(req.getParameter("spPhone"));
            String rateStr = trim(req.getParameter("spRate"));

            if (fn == null) errors.add("New salesperson first name is required.");
            if (ln == null) errors.add("New salesperson last name is required.");
            if (email == null) errors.add("New salesperson email is required.");

            Double rate = null;
            try {
                rate = (rateStr == null) ? null : Double.parseDouble(rateStr);
                if (rate == null || rate < 0 || rate > 100) {
                    errors.add("Commission rate must be between 0 and 100.");
                }
            } catch (NumberFormatException e) {
                errors.add("Commission rate must be a number.");
            }

            if (errors.isEmpty()) {
                Salesperson sp = new Salesperson();
                sp.setFirstName(fn);
                sp.setLastName(ln);
                sp.setEmail(email);
                sp.setPhone(phone);
                sp.setCommissionRate(rate);

                salespersonIdToLink = salespersonDao.create(sp);

                if (salespersonIdToLink <= 0) {
                    errors.add("Failed to create salesperson.");
                }
            }
        }

        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            req.setAttribute("salespeople", salespersonDao.findAll());
            req.getRequestDispatcher("/WEB-INF/views/add_user.jsp").forward(req, resp);
            return;
        }

        // Hash password
        String hash = BCrypt.hashpw(password, BCrypt.gensalt());

        User u = new User();
        u.setUsername(username);
        u.setPasswordHash(hash);
        u.setRole(role);
        u.setSalespersonId(salespersonIdToLink);

        int newUserId = userDao.create(u);

        if (newUserId > 0) {
            session.setAttribute("flashSuccess",
                    "User '" + username + "' created successfully.");
        } else {
            session.setAttribute("flashError",
                    "There was a problem creating the user.");
        }

        resp.sendRedirect(req.getContextPath() + "/index.jsp");
    }

    private String trim(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
