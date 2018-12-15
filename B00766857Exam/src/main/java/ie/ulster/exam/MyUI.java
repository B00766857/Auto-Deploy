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
            "<H1>Microsoft Lending Library</H1> <p/> <h3>Please enter the details below and click Book</h3>");
        logo.setContentMode(com.vaadin.shared.ui.ContentMode.HTML);    
        
        List<Device> dv = new ArrayList<Device>();

        

        Slider durationSlider = new Slider(0, 21);
        durationSlider.setCaption("How long are you borrowing devices for?");
        durationSlider.setOrientation(SliderOrientation.HORIZONTAL);
        durationSlider.setWidth("500px");

        final Label slidervalue = new Label();

        durationSlider.addValueChangeListener(event -> {
            int value = event.getValue().intValue();
            slidervalue.setValue(String.valueOf(value));
        });

        final ComboBox offsite = new ComboBox<String>();
        offsite.setItems("Yes", "No");
        offsite.setCaption("Offsite Lending?");
        
        Label status = new Label("Your lending request is not booked yet.", ContentMode.HTML);
        
        
        try     
        {
	    // Connect with JDBC driver to a database
	    connection = DriverManager.getConnection(connectionString);
     
        ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM devicelending;");
// Convert the resultset that comes back into a List - we need a Java class to represent the data (Customer.java in this case)

// While there are more records in the resultset
while(rs.next())
{   
    // Add a new Device instantiated with the fields from the record 
	dv.add(new Device(rs.getString("device"), 
				rs.getInt("duration"), 
				rs.getInt("noofcopies"), 
                rs.getBoolean("offsite")));
}

        } 
        catch (Exception e) 
        {
	    // This will show an error message if something went wrong
	    layout.addComponent(new Label(e.getMessage()));
        }

       
      // Add my component, grid is populated with devices listed in the devices table in our SQL db
Grid<Device> myGrid = new Grid<>();

// Set the items (List)
myGrid.setItems(dv);
// Configure the order and the caption of the grid
myGrid.addColumn(Device::getName).setCaption("Device");
myGrid.addColumn(Device::getDuration).setCaption("Duration");
myGrid.addColumn(Device::getNoOfCopies).setCaption("Number of Copies");
myGrid.addColumn(Device::getOffSite).setCaption("Offsite Allowed");
myGrid.setSizeFull();
myGrid.setSelectionMode(SelectionMode.MULTI);

// Add the grid to the list
layout.addComponent(myGrid);  
        final TextField name = new TextField();
        name.setCaption("Type your name here:");

        Button button = new Button("Book");
        button.addClickListener(e -> {
           
         
           if (name.getValue().isEmpty()){
               status.setValue("<strong> Please enter lending request name.");
               return;
           }
           if (durationSlider.getValue().intValue() == 0){
               status.setValue("Please confirm how long you wish to borrow items for");
               return;
           }

        

           //For Multiple Device selection
           Set<Device> selected = myGrid.getSelectedItems();

            if (selected.size()==0){
            status.setValue("<stong> Please select a device");
            return;
            }

           for (Device d : selected){
            if (d.getNoOfCopies() == 0){
                status.setValue("<strong>You cannot select an item that is not in stock.</strong>");
                return;
            }
           }

           for (Device d : selected){
            if ((d.getOffSite()== false) && (offsite.getValue() == "yes")){
                status.setValue("<strong>This item is not allowed to be taken offsite.</strong>");
                return;
            }
           }

    
           
            status.setValue("<h3>Success! The group is booked now</h3>");
                return;



        });
        

        hlayout.addComponents(name, durationSlider, offsite);

        layout.addComponents(logo, hlayout, button, status, myGrid, new Label("B00766857"));
        
        setContent(layout);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
