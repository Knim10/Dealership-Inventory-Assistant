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

@WebServlet(name = "AddVehicleServlet", urlPatterns = {"/vehicles/add"})
public class AddVehicleServlet extends HttpServlet {
    private VehicleDao vehicleDao;

    @Override
    public void init() {
        vehicleDao = new JdbcVehicleDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null || !"admin".equalsIgnoreCase(user.getRole())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return;
        }

        req.getRequestDispatcher("/WEB-INF/views/add_vehicle.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null || !"admin".equalsIgnoreCase(user.getRole())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return;
        }

        req.setCharacterEncoding("UTF-8");

        String make     = trim(req.getParameter("make"));
        String model    = trim(req.getParameter("model"));
        String yearStr  = trim(req.getParameter("year"));
        String color    = trim(req.getParameter("color"));
        String category = trim(req.getParameter("category"));
        String costStr  = trim(req.getParameter("cost"));
        String priceStr = trim(req.getParameter("price"));

        List<String> errors = new ArrayList<>();

        // Basic validation
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

        // If validation errors, forward back to form with messages
        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            // Forward keeps the form values via request parameters
            req.getRequestDispatcher("/WEB-INF/views/add_vehicle.jsp").forward(req, resp);
            return;
        }

        // Build Vehicle and save
        Vehicle v = new Vehicle();
        v.setMake(make);
        v.setModel(model);
        v.setYear(year);
        v.setColor(color);
        v.setCategory(category);
        v.setCost(cost);
        v.setPrice(price);
        v.setStatus("Available");

        try {
            int id = vehicleDao.create(v);
            if (id <= 0) {
                session.setAttribute("flashError", "There was a problem saving the new vehicle.");
            } else {
                session.setAttribute("flashSuccess", "Vehicle added successfully (ID " + id + ").");
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("flashError", "Unexpected error: " + e.getMessage());
        }

        // PRG pattern: redirect to inventory page
        resp.sendRedirect(req.getContextPath() + "/vehicles");
    }

    private String trim(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
