// B01234567
package ie.examples;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.components.grid.MultiSelectionModel;

@Theme("mytheme")
public class MyUI extends UI {
    Connection connection = null;
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        Label logo = new Label("<H1>Feckin Flyers</H1> <p/> <h2>Termonfeckin's <strong>fifth</strong> best airline</h2><br>", ContentMode.HTML);
        // Database connection string
        String connectionString = "jdbc:sqlserver://b01234567-feckinflyers.database.windows.net:1433;database=B01234567-feckinflyers;user=b01234567@b01234567-feckinflyers;password={your password};encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
        try
        {
            // Connect and query the data
            connection = DriverManager.getConnection(connectionString);
            ResultSet rs = connection.createStatement().executeQuery("select * from flights;");
            // Create a list anc copy the resultset data into it - created a Flight.java to make this
            List<Flight> flights = new ArrayList<Flight>();
            while(rs.next())
            {
                flights.add(new Flight(rs.getLong("id"), 
                                       rs.getTime("departureTime"), 
                                       rs.getTime("arrivalTime"),
                                       rs.getString("departureAirport"),
                                       rs.getString("arrivalAirport")));
            }
            // Create my grid
            Grid<Flight> grid = new Grid<>();
            grid.setItems(flights);
            grid.addColumn(Flight::getDepartureTime).setCaption("Departs");
            grid.addColumn(Flight::getDepartureAirport).setCaption("From");
            grid.addColumn(Flight::getArrivalTime).setCaption("Arrives");
            grid.addColumn(Flight::getArrivalAirport).setCaption("To");
            grid.setSizeFull(); // This makes the grid the width of the page
            // This makes the grid 'multi-select', adds the checkboxes for selecting to the side
            grid.setSelectionMode(SelectionMode.MULTI);
            // Horizontal layout next
            final HorizontalLayout hlayout = new HorizontalLayout();
            TextField name = new TextField("Full name of passenger");
            
            ComboBox<String> gender = new ComboBox<String>("Gender");
            gender.setItems("gentleman", "lady"); 

            ComboBox<String> status = new ComboBox<String>("Status");
            status.setItems("infant", "child", "adult");
            Label totalCostLabel = new Label("Please select the flight(s) above, enter your details and click <strong>Calculate Cost</strong>", ContentMode.HTML);
            // Button has a click listener to calculate cost
            Button calcCostButton = new Button("Calculate Cost");
            calcCostButton.addClickListener(e -> {
                // If nothing selected in the grid
                if(grid.getSelectedItems().size() == 0)
                {
                    totalCostLabel.setValue("<strong>Please select at least one flight!</strong>");
                    return;
                }
                if(name.getValue().length() == 0)
                {
                    totalCostLabel.setValue("<strong>Please enter your name!</strong>");
                    return;
                }
                if(!gender.getSelectedItem().isPresent() || !status.getSelectedItem().isPresent())
                {
                    totalCostLabel.setValue("<strong>Please select gender and status</strong>");
                    return;
                }
                // Now work out the cost
                double cost = grid.getSelectedItems().size() * 10.50;
                if(gender.getValue().equals("lady"))
                {
                    cost = cost - 5;
                }
                if(status.getValue().equals("infant"))
                {
                    cost = cost / 4;
                }
                if(status.getValue().equals("child"))
                {
                    cost = cost / 2;
                }
                totalCostLabel.setValue("<h3>The total cost is <strong>€" + cost + "</strong></h3>");
    
            });
            hlayout.addComponents(name, gender, status);
    
            layout.addComponents(logo, grid, hlayout, calcCostButton, totalCostLabel, new Label("B01234567"));
            
        }
        catch(Exception e)
        {
            layout.addComponent(new Label(e.getMessage()));
        }
        
        
        setContent(layout);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}


