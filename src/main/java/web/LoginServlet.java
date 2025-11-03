package web;

import dao.UserDao;
import jdbc.JdbcUserDao;
import model.User;
import security.AuthService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    private AuthService auth;

    @Override
    public void init() {
        UserDao userDao = new JdbcUserDao();
        this.auth = new AuthService(userDao);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String username = trim(req.getParameter("username"));
        String password = trim(req.getParameter("password"));

        if (username == null || password == null) {
            req.setAttribute("error", "Username and password are required.");
            doGet(req, resp);
            return;
        }

        Optional<User> user = auth.authenticate(username, password);
        if (user.isPresent()) {
            HttpSession session = req.getSession(true);
            session.setAttribute("user", user.get());
            // Optional: shorter timeout for dev
            session.setMaxInactiveInterval(30 * 60);
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
        } else {
            req.setAttribute("error", "Invalid username or password.");
            doGet(req, resp);
        }
    }

    private static String trim(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
