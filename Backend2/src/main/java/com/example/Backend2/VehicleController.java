package com.example.Backend2;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VehicleController {

    @GetMapping("/vehicles")
    public ArrayList<Vehicle> getVehicles() throws IOException {
        JsonManager manager = new JsonManager();
        ArrayList<Vehicle> vehicles = manager.getVehicles();
        return vehicles;
    }

    @PostMapping("/vehicles")
    public Vehicle createVehicle(@RequestBody Vehicle newVehicle) throws IOException {
        JsonManager manager = new JsonManager();
        ArrayList<Vehicle> vehicles = manager.getVehicles();
        vehicles.add(newVehicle);
        manager.saveVehicles(vehicles);
        return newVehicle;
    }

    @PostMapping("/vehicles/{licensePlate}")
    public String deletVehicle(@PathVariable String licensePlate) throws IOException {
        JsonManager manager = new JsonManager();
        ArrayList<Vehicle> vehiclesList = manager.getVehicles();
        ArrayList<Vehicle> newVehicles = new ArrayList<Vehicle>();
        ArrayList<Vehicle> deletedVehicles = new ArrayList<Vehicle>();

        for (Vehicle vehicleA : vehiclesList) {
            if(!vehicleA.getLicensePlate().equals(licensePlate)){
                newVehicles.add(vehicleA); // Add the vehicle to the newVehicles list
            } else {
                deletedVehicles.add(vehicleA); // Add the deleted vehicle to the deletedVehicles list
            }
        }

        manager.saveVehicles(newVehicles);
        
        return new String("Vehicle deleted successfully");
    }


}
