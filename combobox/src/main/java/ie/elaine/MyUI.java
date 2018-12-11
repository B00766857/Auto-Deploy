package ie.elaine;

import javax.servlet.annotation.WebServlet;

import java.sql.*;
import java.util.*;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Notification;






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

        

        String connectionString = "jdbc:sqlserver://elainedb.database.windows.net:1433;database=classDB;" + 
			  "database=classDB;" + 
			  "user=Elaine@elainedb;" + 
			  "password=CloudDev123;" + 
			  "encrypt=true;" + 
			  "trustServerCertificate=false;" + 
			  "hostNameInCertificate=*.database.windows.net;" +
              "loginTimeout=30;";

     

        
           

        final HorizontalLayout layout = new HorizontalLayout();
        Label logo = new Label("<H1><u>Work CO.</u></H1> <p/> <h3>employee record system</h3><br>", ContentMode.HTML);
            
        TextField staffID = new TextField("Staff ID");
        staffID.setMaxLength(5);
        TextField tname = new TextField("Name of Person");
        TextField phone = new TextField("Phone number");
              
        Button addButton = new Button("Add");

        //Button clear = new Button("Clear");
        //clear.addClickListener(e -> {
          //  myGrid.removeAllColumns();
          //  people.clear(); // Clear the list of people
         //   name.setValue("");
            phone.setValue("");
          // staffID.setValue("");
        //});  

       layout.addComponent(logo);
              setContent(layout);
                          
        final VerticalLayout vlayout = new VerticalLayout();

        vlayout.addComponents(staffID,  phone, addButton);


        try 
        {
            // Connect with JDBC driver to a database
            connection = DriverManager.getConnection(connectionString);
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM customerTable;");
            // Convert the resultset that comes back into a List - we need a Java class to represent the data (Customer.java in this case)
            List<Customer> customers = new ArrayList<Customer>();
            // While there are more records in the resultset
            while(rs.next())
            {   
	        // Add a new Customer instantiated with the fields from the record (that we want, we might not want all the fields, note how I skip the id)
	        customers.add(new Customer(rs.getString("first_name"), 
				rs.getString("last_name"), 
				rs.getBoolean("paid"), 
				rs.getDouble("amount")));
            }
            // Add my component, grid is templated with Customer
            Grid<Customer> myGrid = new Grid<>();
            // Set the items (List)
            myGrid.setItems(customers);
            // Configure the order and the caption of the grid
            myGrid.addColumn(Customer::getFirst_name).setCaption("Name");
            myGrid.addColumn(Customer::getLast_name).setCaption("Surname");
            myGrid.addColumn(Customer::getAmount).setCaption("Total Amount");
            myGrid.addColumn(Customer::isPaid).setCaption("Paid");

        // Add the grid to the list
        vlayout.addComponent(myGrid);
        } 
        catch (Exception e) 
        {
            // This will show an error message if something went wrong
            vlayout.addComponent(new Label(e.getMessage()));
        }

        
      setContent(vlayout); 
        
        final TextField name = new TextField();
        tname.setCaption("Type your name here:");

        Button button = new Button("Add Elaine");
        button.addClickListener(e -> {
            layout.addComponent(new Label("Thanks " + name.getValue() 
                    + ", it works!"));
        });
        
        //Button 1
        Button button1 = new Button("Add Donna");
        button.addClickListener(e -> {
            layout.addComponent(new Label("Thanks " + name.getValue() 
                    + ", it works!"));
        });

           // create a combo box for customers
    ComboBox<String> comboBox = new ComboBox<>("Browsers");
    comboBox.setItems("Google Chrome", "Mozilla Firefox", "Opera",
            "Apple Safari", "Microsoft Edge");
    
    comboBox.addValueChangeListener(event -> {
        if (event.getSource().isEmpty()) {
            Notification.show("No browser selected");
        } else {
            Notification.show("Selected browser: " + event.getValue());
        }
    });

        vlayout.addComponents(tname, button, button1, comboBox);
        
        setContent(vlayout);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
