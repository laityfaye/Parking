package com.parking.Controller;

import com.parking.Models.Car;
import com.parking.Repository.CarRepositoryImpl;
import com.parking.Service.CarService;
import com.parking.Service.ParkingService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/parking")
public class ParkingServlet extends HttpServlet {
    private ParkingService parkingService;

    @Override
    public void init() {
        CarService carRepository = new CarRepositoryImpl();
        this.parkingService = new ParkingService(carRepository, 10); // Parking de 10 places
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Car> cars = parkingService.getCars();
        int availableSpaces = parkingService.getAvailableSpaces();
        req.setAttribute("cars", cars);
        req.setAttribute("availableSpaces", availableSpaces);
        req.getRequestDispatcher("/WEB-INF/views/parking.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String licensePlate = req.getParameter("licensePlate");

        if ("enter".equals(action)) {
            Car car = new Car(licensePlate);
            if (parkingService.enterCar(car)) {
                req.setAttribute("message", "Voiture entrée avec succès !");
            } else {
                req.setAttribute("message", "Parking plein !");
            }
        } else if ("exit".equals(action)) {
            if (parkingService.exitCar(licensePlate)) {
                req.setAttribute("message", "Voiture sortie avec succès !");
            } else {
                req.setAttribute("message", "Voiture non trouvée !");
            }
        }

        doGet(req, resp); // Rafraîchir la page
    }
}