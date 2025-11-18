package web;

import dao.VehicleDao;
import jdbc.JdbcVehicleDao;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "DeleteVehicleServlet", urlPatterns = {"/vehicles/delete"})
public class DeleteVehicleServlet extends HttpServlet {
    private VehicleDao vehicleDao;

    @Override
    public void init() {
        vehicleDao = new JdbcVehicleDao();
    }

    private boolean isAdmin(HttpSession session) {
        if (session == null) return false;
        User u = (User) session.getAttribute("user");
        return u != null && "admin".equalsIgnoreCase(u.getRole());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (!isAdmin(session)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return;
        }

        String idStr = req.getParameter("id");
        int id;

        try {
            id = Integer.parseInt(idStr);
        } catch (Exception e) {
            session.setAttribute("flashError", "Invalid vehicle ID.");
            resp.sendRedirect(req.getContextPath() + "/vehicles");
            return;
        }

        boolean ok = vehicleDao.deleteById(id);
        if (ok) {
            session.setAttribute("flashSuccess", "Vehicle deleted (ID " + id + ").");
        } else {
            session.setAttribute("flashError", "Unable to delete vehicle (ID " + id + ").");
        }

        resp.sendRedirect(req.getContextPath() + "/vehicles");
    }
}
