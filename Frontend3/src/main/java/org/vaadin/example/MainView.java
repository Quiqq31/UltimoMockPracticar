package org.vaadin.example;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Paragraph;
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

        add(availabilityFilter);
        add(grid);
    }
}
