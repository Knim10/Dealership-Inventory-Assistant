package web;

import dao.VehicleDao;
import jdbc.JdbcVehicleDao;
import model.User;
import model.Vehicle;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "EditVehicleServlet", urlPatterns = {"/vehicles/edit"})
public class EditVehicleServlet extends HttpServlet {
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (!isAdmin(session)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return;
        }

        String idStr = req.getParameter("id");
        Integer id = null;
        try {
            id = Integer.parseInt(idStr);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid vehicle ID");
            return;
        }

        Optional<Vehicle> opt = vehicleDao.findById(id);
        if (opt.isEmpty()) {
            session.setAttribute("flashError", "Vehicle not found.");
            resp.sendRedirect(req.getContextPath() + "/vehicles");
            return;
        }

        req.setAttribute("vehicle", opt.get());
        req.getRequestDispatcher("/WEB-INF/views/edit_vehicle.jsp").forward(req, resp);
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
        String idStr     = trim(req.getParameter("id"));
        String make      = trim(req.getParameter("make"));
        String model     = trim(req.getParameter("model"));
        String yearStr   = trim(req.getParameter("year"));
        String color     = trim(req.getParameter("color"));
        String category  = trim(req.getParameter("category"));
        String costStr   = trim(req.getParameter("cost"));
        String priceStr  = trim(req.getParameter("price"));
        String status    = trim(req.getParameter("status"));

        List<String> errors = new ArrayList<>();
        Integer id = null;

        try {
            id = Integer.parseInt(idStr);
        } catch (Exception e) {
            errors.add("Invalid vehicle ID.");
        }

        if (make == null)  errors.add("Make is required.");
        if (model == null) errors.add("Model is required.");

        Integer year = null;
        try {
            year = (yearStr == null) ? null : Integer.parseInt(yearStr);
            if (year == null || year < 1980 || year > 2035) {
                errors.add("Year must be between 1980 and 2035.");
            }
        } catch (NumberFormatException e) {
            errors.add("Year must be a number.");
        }

        Double cost = null;
        try {
            cost = (costStr == null) ? null : Double.parseDouble(costStr);
            if (cost == null || cost < 0) {
                errors.add("Cost must be a positive number.");
            }
        } catch (NumberFormatException e) {
            errors.add("Cost must be a valid number.");
        }

        Double price = null;
        try {
            price = (priceStr == null) ? null : Double.parseDouble(priceStr);
            if (price == null || price <= 0) {
                errors.add("Price must be a positive number.");
            }
        } catch (NumberFormatException e) {
            errors.add("Price must be a valid number.");
        }

        if (cost != null && price != null && price < cost) {
            errors.add("Price should generally be greater than or equal to cost.");
        }

        if (category == null) {
            category = "Car";
        }
        if (status == null) {
            status = "Available";
        }

        // If errors, forward back to form with current info
        if (!errors.isEmpty()) {
            Vehicle v = new Vehicle();
            v.setVehicleId(id);
            v.setMake(make);
            v.setModel(model);
            v.setYear(year);
            v.setColor(color);
            v.setCategory(category);
            v.setCost(cost);
            v.setPrice(price);
            v.setStatus(status);

            req.setAttribute("vehicle", v);
            req.setAttribute("errors", errors);
            req.getRequestDispatcher("/WEB-INF/views/edit_vehicle.jsp").forward(req, resp);
            return;
        }

        // No validation errors: update
        Vehicle v = new Vehicle();
        v.setVehicleId(id);
        v.setMake(make);
        v.setModel(model);
        v.setYear(year);
        v.setColor(color);
        v.setCategory(category);
        v.setCost(cost);
        v.setPrice(price);
        v.setStatus(status);

        boolean ok = vehicleDao.update(v);
        if (ok) {
            session.setAttribute("flashSuccess", "Vehicle updated successfully (ID " + id + ").");
        } else {
            session.setAttribute("flashError", "Unable to update vehicle (ID " + id + ").");
        }

        resp.sendRedirect(req.getContextPath() + "/vehicles");
    }

    private String trim(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
