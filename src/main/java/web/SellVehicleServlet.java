package web;

import dao.SaleDao;
import dao.SalespersonDao;
import dao.VehicleDao;
import jdbc.JdbcSaleDao;
import jdbc.JdbcSalespersonDao;
import jdbc.JdbcVehicleDao;
import model.Sale;
import model.Salesperson;
import model.User;
import model.Vehicle;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.Optional;

@WebServlet(name = "SellVehicleServlet", urlPatterns = {"/vehicles/sell"})
public class SellVehicleServlet extends HttpServlet {

    private VehicleDao vehicleDao;
    private SaleDao saleDao;
    private SalespersonDao salespersonDao;

    @Override
    public void init() {
        this.vehicleDao = new JdbcVehicleDao();
        this.saleDao = new JdbcSaleDao();
        this.salespersonDao = new JdbcSalespersonDao();
    }

    private boolean canSell(User user) {
        if (user == null) return false;
        // Admins or sales with a linked salesperson record
        if ("admin".equalsIgnoreCase(user.getRole())) return true;
        return "sales".equalsIgnoreCase(user.getRole()) && user.getSalespersonId() != null;
    }

// src/main/java/web/SellVehicleServlet.java  (only the changed parts shown)

@Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

    HttpSession session = req.getSession(false);
    User user = (session != null) ? (User) session.getAttribute("user") : null;

    if (!canSell(user)) {
        resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not allowed to sell vehicles.");
        return;
    }

    String idStr = req.getParameter("id");
    int vehicleId;
    try {
        vehicleId = Integer.parseInt(idStr);
    } catch (Exception e) {
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid vehicle ID");
        return;
    }

    Optional<Vehicle> opt = vehicleDao.findById(vehicleId);
    if (opt.isEmpty()) {
        session.setAttribute("flashError", "Vehicle not found.");
        resp.sendRedirect(req.getContextPath() + "/vehicles");
        return;
    }

    Vehicle v = opt.get();
    if ("Sold".equalsIgnoreCase(v.getStatus())) {
        session.setAttribute("flashError", "Vehicle is already sold.");
        resp.sendRedirect(req.getContextPath() + "/vehicles");
        return;
    }

    req.setAttribute("vehicle", v);
    req.setAttribute("today", LocalDate.now());

    // For sales user: load their own salesperson record
    Salesperson sp = null;
    if (user != null && user.getSalespersonId() != null) {
        sp = salespersonDao.findById(user.getSalespersonId()).orElse(null);
    }
    req.setAttribute("salesperson", sp);

    // For admin: load list of all salespeople for dropdown
    if (user != null && "admin".equalsIgnoreCase(user.getRole())) {
        List<Salesperson> salespeople = salespersonDao.findAll();
        req.setAttribute("salespeople", salespeople);
    }

    req.getRequestDispatcher("/WEB-INF/views/sell_vehicle.jsp").forward(req, resp);
}


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (!canSell(user)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not allowed to sell vehicles.");
            return;
        }

        req.setCharacterEncoding("UTF-8");

        String vehicleIdStr = req.getParameter("vehicleId");
        String saleDateStr  = req.getParameter("saleDate");
        String salePriceStr = req.getParameter("salePrice");

        int vehicleId;
        try {
            vehicleId = Integer.parseInt(vehicleIdStr);
        } catch (Exception e) {
            session.setAttribute("flashError", "Invalid vehicle ID.");
            resp.sendRedirect(req.getContextPath() + "/vehicles");
            return;
        }

        // Reload vehicle
        Optional<Vehicle> opt = vehicleDao.findById(vehicleId);
        if (opt.isEmpty()) {
            session.setAttribute("flashError", "Vehicle not found.");
            resp.sendRedirect(req.getContextPath() + "/vehicles");
            return;
        }
        Vehicle v = opt.get();
        if ("Sold".equalsIgnoreCase(v.getStatus())) {
            session.setAttribute("flashError", "Vehicle is already sold.");
            resp.sendRedirect(req.getContextPath() + "/vehicles");
            return;
        }

        // Parse sale date & price
        LocalDate saleDate = (saleDateStr == null || saleDateStr.isBlank())
                ? LocalDate.now()
                : LocalDate.parse(saleDateStr);

        Double salePrice;
        try {
            salePrice = Double.parseDouble(salePriceStr);
        } catch (Exception e) {
            session.setAttribute("flashError", "Invalid sale price.");
            resp.sendRedirect(req.getContextPath() + "/vehicles/sell?id=" + vehicleId);
            return;
        }

        // Figure out salespersonId and commission rate
        Integer salespersonId = null;
        double commissionEarned = 0.0;

        if ("admin".equalsIgnoreCase(user.getRole())) {
            // For now, admin must choose a salesperson for the sale
            String spIdStr = req.getParameter("salespersonId");
            try {
                salespersonId = Integer.parseInt(spIdStr);
            } catch (Exception e) {
                session.setAttribute("flashError", "Admin must choose a salesperson for this sale.");
                resp.sendRedirect(req.getContextPath() + "/vehicles/sell?id=" + vehicleId);
                return;
            }
        } else if ("sales".equalsIgnoreCase(user.getRole())) {
            salespersonId = user.getSalespersonId();
        }

        Salesperson salesperson = null;
        if (salespersonId != null) {
            salesperson = salespersonDao.findById(salespersonId).orElse(null);
        }

        if (salesperson != null) {
            double rate = salesperson.getCommissionRate(); // e.g. 5.0 means 5%
            commissionEarned = salePrice * (rate / 100.0);
        }

        // Create sale record
        Sale sale = new Sale();
        sale.setVehicleId(vehicleId);
        sale.setSalespersonId(salespersonId);
        sale.setProspectId(null); // could wire this later
        sale.setSaleDate(saleDate);
        sale.setSalePrice(salePrice);
        sale.setCommissionEarned(commissionEarned);

        int saleId = saleDao.create(sale);

        // Mark vehicle as sold
        v.setStatus("Sold");
        vehicleDao.update(v);

        session.setAttribute("flashSuccess",
                "Vehicle sold (Sale ID " + saleId + "). Commission: " + commissionEarned);

        resp.sendRedirect(req.getContextPath() + "/vehicles");
    }
}
