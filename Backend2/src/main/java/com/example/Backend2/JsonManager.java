package com.example.Backend2;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JsonManager {


    public ArrayList<Vehicle> getVehicles() throws IOException{

        String path = "src/main/resources/vehicles.json";
        String jsonContent = new String(Files.readAllBytes(Paths.get(path)));

        Gson gson = new Gson();
        Type vehicleListType = new TypeToken<ArrayList<Vehicle>>(){}.getType();
        ArrayList<Vehicle> vehiclesList = gson.fromJson(jsonContent, vehicleListType);
        return vehiclesList;
    }

    public ArrayList<Vehicle> saveVehicles(ArrayList<Vehicle> vehicles) throws IOException{
        Gson gson = new Gson();
        String jsonContent = gson.toJson(vehicles);

        String path = "src/main/resources/vehicles.json";

        Writer writer = Files.newBufferedWriter(Paths.get(path));
        writer.write(jsonContent);
        writer.close();

        return vehicles;
    }
}
