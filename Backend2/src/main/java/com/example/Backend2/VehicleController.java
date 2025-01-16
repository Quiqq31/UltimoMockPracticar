package com.example.Backend2;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.DeleteMapping;
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
    public ArrayList<Vehicle> createVehicle(@RequestBody Vehicle newVehicle) throws IOException {
        JsonManager manager = new JsonManager();
        ArrayList<Vehicle> vehicles = manager.getVehicles();

        //When no data is imput correctly (before we implement the frontend)
        if(newVehicle.getYear() == 0){
            newVehicle.setMake("Toyota");
            newVehicle.setModel("Supra");
            newVehicle.setYear(2009);
            newVehicle.setType("Sedan");
            newVehicle.setLicensePlate("SUP-8888");
            newVehicle.setAvailability(true);
        }
        // uuid is generated automatically in the Vehicle class constructor

        vehicles.add(newVehicle);
        manager.saveVehicles(vehicles);
        return vehicles;
    }

    @DeleteMapping("/vehicles/{licensePlate}")
    public boolean deletVehicle(@PathVariable String licensePlate) throws IOException {
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
        if(deletedVehicles.size() > 0){
            return true;
        } else {
            return false;
        }
    }


}
