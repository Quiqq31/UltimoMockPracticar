package org.vaadin.example;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.grid.Grid;

/**
 * A sample Vaadin view class.
 * <p>
 * To implement a Vaadin view just extend any Vaadin component and use @Route
 * annotation to announce it in a URL as a Spring managed bean.
 * <p>
 * A new instance of this class is created for every new user and every browser
 * tab/window.
 * <p>
 * The main view contains a text field for getting the user name and a button
 * that shows a greeting message in a notification.
 */
@Route
public class MainView extends VerticalLayout {

    /**
     * Construct a new Vaadin view.
     * <p>
     * Build the initial UI state for the user accessing the application.
     *
     * @param service
     *            The message service. Automatically injected Spring managed bean.
     * @throws InterruptedException 
     * @throws IOException 
     * @throws URISyntaxException 
     */
    public MainView(@Autowired GreetService service) throws URISyntaxException, IOException, InterruptedException {
        // Grid Element for Vehicles
        Grid<Vehicle> grid = new Grid<>(Vehicle.class, false);
        grid.addColumn(Vehicle::getLicensePlate).setHeader("License Plate");
        grid.addColumn(Vehicle::getMake).setHeader("Make");
        grid.addColumn(Vehicle::getModel).setHeader("Model");
        grid.addColumn(Vehicle::getYear).setHeader("Year");
        grid.addColumn(Vehicle::getType).setHeader("Type");
        grid.addColumn(Vehicle::getUuid).setHeader("UUID");
        grid.addColumn(Vehicle::getAvailability).setHeader("Availability");

        ArrayList<Vehicle> vehicle = DataService.getVehicles();

        grid.setItems(vehicle);

        // ComboBox for filtering by availability
        ComboBox<String> availabilityFilter = new ComboBox<>("Availability");
        availabilityFilter.setItems("All", "Available", "Not Available");
        availabilityFilter.setValue("All");

        availabilityFilter.addValueChangeListener(event -> {
            String filterValue = event.getValue();
            if ("Available".equals(filterValue)) {
                grid.setItems(vehicle.stream().filter(Vehicle::getAvailability).collect(Collectors.toList()));
            } else if ("Not Available".equals(filterValue)) {
                grid.setItems(vehicle.stream().filter(v -> !v.getAvailability()).collect(Collectors.toList()));
            } else {
                grid.setItems(vehicle);
            }
        });

        // Form for adding new vehicles
        TextField licensePlateField = new TextField("License Plate");
        TextField makeField = new TextField("Make");
        TextField modelField = new TextField("Model");
        TextField yearField = new TextField("Year");
        ComboBox<String> typeField = new ComboBox<>("Type");
        typeField.setItems("SUV", "Sedan", "Truck", "Coupe");
        ComboBox<String> availabilityField = new ComboBox<>("Availability");
        availabilityField.setItems("Available", "Not Available");

        Button addButton = new Button("Add Vehicle");
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickListener(e -> {
            String licensePlate = licensePlateField.getValue();
            String make = makeField.getValue();
            String model = modelField.getValue();
            int year = Integer.parseInt(yearField.getValue()); // XXXX
            String type = typeField.getValue();
            boolean availability = "Available".equals(availabilityField.getValue());

            Vehicle newVehicle = new Vehicle(make, model, year, type, licensePlate, availability);

            // Check if all fields are filled out, if not show an error message
            if (licensePlate.isEmpty() || make.isEmpty() || model.isEmpty() || yearField.isEmpty() || type == null || availabilityField.isEmpty()) {
                // Show an error message or notification
                Notification.show("All fields must be filled out.", 3000, Notification.Position.MIDDLE);
                return;
            }

            try {
                DataService.createVehicle(newVehicle);
                vehicle.add(newVehicle);
                grid.setItems(vehicle);
                Notification.show("Vehicle Created Succesfully", 3000, Notification.Position.MIDDLE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            
            // Clear the form fields
            licensePlateField.clear();
            makeField.clear();
            modelField.clear();
            yearField.clear();
            typeField.clear();
            availabilityField.clear();
        });

        HorizontalLayout formLayout = new HorizontalLayout(licensePlateField, makeField, modelField, yearField, typeField, availabilityField, addButton);
        
        //Form for deleting vehicles from the grid
        TextField deleteLicensePlateField = new TextField("License Plate to Delete");
        Button deleteButton = new Button("Delete Vehicle");
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteButton.addClickListener(e -> {
            String licensePlateToDelete = deleteLicensePlateField.getValue();
            if (licensePlateToDelete.isEmpty()) {
                Notification.show("License Plate must be filled out.", 3000, Notification.Position.MIDDLE);
            return;
            }

            Vehicle vehicleToDelete = vehicle.stream()
            .filter(v -> v.getLicensePlate().equals(licensePlateToDelete))
            .findFirst()
            .orElse(null);

            if (vehicleToDelete != null) {
                try {
                    DataService.deleteVehicle(vehicleToDelete.getLicensePlate());
                    vehicle.remove(vehicleToDelete);
                    grid.setItems(vehicle);
                    Notification.show("Vehicle Deleted Successfully", 3000, Notification.Position.MIDDLE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
            Notification.show("Vehicle not found.", 3000, Notification.Position.MIDDLE);
            }

            deleteLicensePlateField.clear();
        });

        HorizontalLayout deleteFormLayout = new HorizontalLayout(deleteLicensePlateField, deleteButton);
        add(deleteFormLayout);
        

        add(formLayout);
        add(availabilityFilter);
        add(grid);
    }
}
