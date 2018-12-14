package ie.ulster.exam;

import java.util.*;
import java.util.stream.Collectors;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import javax.servlet.annotation.WebServlet;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.slider.SliderOrientation;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MultiSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Slider;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Grid.SelectionMode;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        
        Connection connection = null;
        String connectionString = "jdbc:sqlserver://b00766857-exam.database.windows.net:1433;database=b00766857-exam;" + 
        "database=b00766857-exam;" + 
        "user=b00766857@b00766857-exam;" + 
        "password=CloudDev123;" + 
        "encrypt=true;" + 
        "trustServerCertificate=false;" + 
        "hostNameInCertificate=*.database.windows.net;" +
        "loginTimeout=30;";

        final VerticalLayout layout = new VerticalLayout();
        final HorizontalLayout hlayout = new HorizontalLayout();

        Label logo = new Label(
            "<H1>Marty Party Planners</H1> <p/> <h3>Please enter the details below and click Book</h3>");
        logo.setContentMode(com.vaadin.shared.ui.ContentMode.HTML);    
        
        List<Room> rm = new ArrayList<Room>();

        

        Slider amountSlider = new Slider(0, 200);
        amountSlider.setCaption("How many people are invited to this party");
        amountSlider.setOrientation(SliderOrientation.HORIZONTAL);
        amountSlider.setWidth("500px");

        final Label slidervalue = new Label();

        amountSlider.addValueChangeListener(event -> {
            int value = event.getValue().intValue();
            slidervalue.setValue(String.valueOf(value));
        });

        final ComboBox children = new ComboBox<String>();
        children.setItems("Yes", "No");
        children.setCaption("Are children attending");
        
        Label status = new Label("Your party is not booked yet!", ContentMode.HTML);
        
        
        try     
        {
	    // Connect with JDBC driver to a database
	    connection = DriverManager.getConnection(connectionString);
     
        ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM room;");
// Convert the resultset that comes back into a List - we need a Java class to represent the data (Customer.java in this case)

// While there are more records in the resultset
while(rs.next())
{   
	// Add a new Customer instantiated with the fields from the record (that we want, we might not want all the fields, note how I skip the id)
	rm.add(new Room(rs.getInt("id"), 
				rs.getString("name"), 
				rs.getInt("capacity"), 
                rs.getString("feature"),
                rs.getBoolean("alcohol")));
}

        } 
        catch (Exception e) 
        {
	    // This will show an error message if something went wrong
	    layout.addComponent(new Label(e.getMessage()));
        }

       
      // Add my component, grid is templated with Customer
Grid<Room> myGrid = new Grid<>();

// Set the items (List)
myGrid.setItems(rm);
// Configure the order and the caption of the grid
myGrid.addColumn(Room::getId).setCaption("Id");
myGrid.addColumn(Room::getName).setCaption("Name");
myGrid.addColumn(Room::getCapacity).setCaption("Capacity");
myGrid.addColumn(Room::getFeature).setCaption("Feature");
myGrid.addColumn(Room::getAlcohol).setCaption("Alcohol");
myGrid.setSizeFull();
myGrid.setSelectionMode(SelectionMode.MULTI);

// Add the grid to the list
layout.addComponent(myGrid);  
        final TextField name = new TextField();
        name.setCaption("Type your name here:");

        Button button = new Button("Book");
        button.addClickListener(e -> {
           
         
           if (name.getValue().isEmpty()){
               status.setValue("<strong> Please enter party name");
               return;
           }
           if (children.getValue()==null){
               status.setValue("<strong>Please select children, yes or no");
               return;
           }

           //For Multiple Room selection
           Set<Room> selected = myGrid.getSelectedItems();

            if (selected.size()==0){
            status.setValue("<stong> Please select a room");
            return;
            }

           for (Room r : selected){
            if (r.getAlcohol()==true && children.getValue().equals("Yes")){
                status.setValue("<strong>You cannot select any rooms serving alcohol if children are attending");
                return;
            }
           }

           int totalcapacity = 0;
           for (Room r : selected){
            totalcapacity = totalcapacity + r.getCapacity();
            }
            if (amountSlider.getValue().intValue() > totalcapacity){
                status.setValue("<strong> You have selecred rooms with a max capacity of " + totalcapacity + 
                " which is not enough to hold " + amountSlider.getValue().intValue() + ". <storng>");
                return;
            }
           
            status.setValue("<strong>Your party is booked!");
                return;



        });
        

        hlayout.addComponents(name, amountSlider, children);

        layout.addComponents(logo, hlayout, button, status, myGrid, new Label("B00766857"));
        
        setContent(layout);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
