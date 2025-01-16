package org.vaadin.example;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class DataService {
    public static ArrayList<Vehicle> getVehicles() throws URISyntaxException, IOException, InterruptedException{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(new URI("http://localhost:8080/vehicles"))
            .GET()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();

        Gson gson = new Gson();
        Type vehicleListType = new TypeToken<ArrayList<Vehicle>>(){}.getType();

        ArrayList<Vehicle> vehiclesList = gson.fromJson(responseBody, vehicleListType);
        return vehiclesList;
    }

    public static Vehicle createVehicle(Vehicle newVehicle) throws IOException, InterruptedException{
        Gson gson = new Gson();
        String body = gson.toJson(newVehicle);
    
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/vehicles"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(body))
            .build();
    
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    
        return newVehicle;
    }

    public static boolean deleteVehicle(String licensePlate) throws IOException, InterruptedException{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/vehicles/" + licensePlate))
            .DELETE()
            .build();
    
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        
        return Boolean.parseBoolean(response.body());
    }

}
