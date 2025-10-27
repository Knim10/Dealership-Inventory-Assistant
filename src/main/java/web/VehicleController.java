package web;

import dao.VehicleDao;
import jdbc.JdbcVehicleDao;
import model.Vehicle;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "VehicleController", urlPatterns = {"/vehicles"})
public class VehicleController extends HttpServlet {
    private VehicleDao vehicleDao;

    @Override
    public void init() {
        this.vehicleDao = new JdbcVehicleDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String make  = trimOrNull(req.getParameter("make"));
        String model = trimOrNull(req.getParameter("model"));
        Integer year = parseIntOrNull(req.getParameter("year"));
        String color = trimOrNull(req.getParameter("color"));

        List<Vehicle> vehicles = (make != null || model != null || year != null || color != null)
                ? vehicleDao.search(make, model, year, color)
                : vehicleDao.findAll();

        req.setAttribute("vehicles", vehicles);
        req.getRequestDispatcher("/WEB-INF/views/vehicles.jsp").forward(req, resp);
    }

    private static String trimOrNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    private static Integer parseIntOrNull(String s) {
        try { return (s == null || s.isBlank()) ? null : Integer.parseInt(s.trim()); }
        catch (NumberFormatException ex) { return null; }
    }
}
