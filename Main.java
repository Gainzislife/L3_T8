/**
 * 
 * <h1>Project Management System</h1>
 * <h2>Main class</h2>
 * <p>This is the main class for the project</p>
 *
 * @author GJ Steyn
 * @version 2.00, 1 June 2020
 */

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class Main {
  // ATTRIBUTES
  // Constants
  private static final Scanner SCAN = new Scanner(System.in);
  public static final DateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd");
  
  // METHODS
  /**
  *
  * Creates a connection to the database.
  * <br>
  * This method is called when the program starts.
  *
  * @param database name
  * @return Connection
  * @see Main
  */
  private static Connection createConnection(String database) throws SQLException {
    // Connect to the library_db via jdbc:mysql: channel on localhost (this PC)
    // Use username "otheruser", password "swordfish"
    return DriverManager.getConnection(
          "jdbc:mysql://localhost:3306/" + database + "?useSSL=false",
          "otheruser",
          "swordfish"
          );
  }
  
  /**
  *
  * Displays all content from a table.
  * <br>
  * This method is used for testing, but can be added as a menu option.
  *
  * @param statement to connect to database
  * @param table name of the table
  */
  // For testing connection and showing results from a selected table
  private static void readTable(Statement statement, String table) throws SQLException {
    ResultSet results = statement.executeQuery("SELECT * FROM " + table);
    
    if(table.equals("projects")) {
      while(results.next()) {
        System.out.println(
            results.getInt("number") + ", "
            + results.getString("name" )
            );
      }
    } else {
      while(results.next()) {
        System.out.println(
            results.getInt("id") + ", "
            + results.getString("fname" )
            );
      }
    }
    
  }
  
  /**
  *
  * Display menu options
  * <br>
  * This method is part of the main screen for the program and is called on startup and when the user is done with any other option
  *
  */
  // Menu
  private static void showMenu() {
    // Output menu options
    System.out.println("POISED MAIN MENU");
    System.out.println("Select an option:");
    System.out.println("1 - create new project");
    System.out.println("2 - change due date of a project");
    System.out.println("3 - change the total amount paid for a project");
    System.out.println("4 - update a contractor's details");
    System.out.println("5 - finalise a project");
    System.out.println("6 - incomplete projects");
    System.out.println("7 - projects past due");
    System.out.println("0 - exit");
  }
  
  /**
  *
  * Method to add a new project.
  * <br>
  * This method is called when menu option 1 is entered.
  *
  * @param statement to connect to database
  * @return integer of rows affected
  */
  // Menu option 1
  private static int addProject(Statement statement) throws SQLException {
    System.out.println("** Add Project **");
    
    // Select last entry from projects table and add 1 to the project number
    ResultSet result = statement.executeQuery("SELECT * FROM projects ORDER BY number DESC LIMIT 1");
    result.next();
    int projNum = result.getInt("number");
    projNum++;
    
    // Customer details
    String custName;
    do {
      System.out.print("Client Name: ");
      custName = SCAN.nextLine();
      
      if(custName.equals("0")) {
        return 0;
      } else if(!isAlphaNumeric(custName)) {
        System.out.println("Invalid input. Please re-enter or '0' for main menu.");
      }
    } while(!isAlphaNumeric(custName));
    
    // Validate input
    String custSurname;
    do {
      System.out.print("Client Surname: ");
      custSurname = SCAN.nextLine();
      
      if(custSurname.equals("0")) {
        return 0;
      } else if(!isAlphaNumeric(custSurname)) {
        System.out.println("Invalid input. Please re-enter or '0' for main menu.");
      }
    } while(!isAlphaNumeric(custSurname));
    
    // Validate telephone input from user
    String custTel;
    do {
      System.out.print("Client Tel Number: ");
      custTel = SCAN.nextLine();
      
      if(custTel.equals("0")) {
        return 0;
      } else if(!validTel(custTel)) {
        System.out.println("Invalid Tel Number. Valid examples: 012 345 6789 or 0123456789. Or '0' for main menu.");
      }
    } while(!validTel(custTel));
    
    // Validate email input from user
    String custEmail;
    do {
      System.out.print("Client Email: ");
      custEmail = SCAN.nextLine();
      custEmail = custEmail.trim();
      
      if(custEmail.equals("0")) {
        return 0;
      } else if(!validEmail(custEmail)) {
        System.out.println("Invalid email. Valid example: name@mail.com. Or '0' for main menu.");
      }
    } while(!validEmail(custEmail));
    
    // Validate input
    String custAddress;
    do {
      System.out.print("Client Address: ");
      custAddress = SCAN.nextLine();
      
      if(custAddress.equals("0")) {
        return 0;
      } else if(!isAlphaNumeric(custAddress)) {
        System.out.println("Invalid input. Please re-enter or '0' for main menu.");
      }
    } while(!isAlphaNumeric(custAddress));
    
    
    // Select last entry from customers table and add 1 to the id
    result = statement.executeQuery("SELECT * FROM customers ORDER BY id DESC LIMIT 1");
    result.next();
    int custId = result.getInt("id");
    custId++;
    
    // Check building type input from user
    String type;
    do {
      System.out.print("Building Type: ");
      type = SCAN.nextLine();
      type = type.toLowerCase();
      
      if(type.equals("house") || type.equals("apartment") || type.equals("office")) {
        break;
      } else if(type.equals("0")) {
        return 0;
      } else {
        System.out.println("Type not recognised. Please re-enter or '0' for main menu.");
      }
    } while(true);
    
    // Validate address input from user
    String address;
    do {
      System.out.print("Address: ");
      address = SCAN.nextLine();
      
      if(address.equals("0")) {
        return 0;
      } else if(!isAlphaNumeric(address)) {
        System.out.println("Invalid input. Please re-enter or '0' for main menu.");
      }
    } while(!isAlphaNumeric(address));
    
    // Validate erf number input from user
    String erf;
    do {
      System.out.print("ERF Number: ");
      erf = SCAN.nextLine();
      
      if(erf.equals("0")) {
        return 0;
      } else if(!isAlphaNumeric(address)) {
        System.out.println("Invalid input. Please re-enter or '0' for main menu.");
      }
    } while(!isAlphaNumeric(erf));
    
    // Validate fee input from user
    String input;
    do {
      System.out.print("Project Fee: ");
      input = SCAN.nextLine();
      
      if(input.equals("0")) {
        return 0;
      } else if(!isNumeric(input)) {
        System.out.println("Warning! Only numerals allowed ('0' for main menu).");
      }
    } while(!isNumeric(input));
    
    int fee = Integer.parseInt(input.trim());
    
    // Validate date input from user
    Date deadline;
    do {
      System.out.print("Deadline (yyyy-mm-dd): ");
      input = SCAN.nextLine();
      
      if(input.equals("0")) {
        return 0;
      }
      
      try {
        deadline = Date.valueOf(input);
        break;
      } catch(Exception e) {
        System.out.println("Invalid date. Please re-enter or '0' for main menu.");
      }
    } while(true);
    
    // Contractor details
    String contName;
    do {
      System.out.print("Contractor Name: ");
      contName = SCAN.nextLine();
      
      if(contName.equals("0")) {
        return 0;
      } else if(!isAlphaNumeric(contName)) {
        System.out.println("Invalid input. Please re-enter or '0' for main menu.");
      }
    } while(!isAlphaNumeric(contName));
    
    String contSurname;
    do {
      System.out.print("Contractor Surname: ");
      contSurname = SCAN.nextLine();
      
      if(contSurname.equals("0")) {
        return 0;
      } else if(!isAlphaNumeric(custSurname)) {
        System.out.println("Invalid input. Please re-enter or '0' for main menu.");
      }
    } while(!isAlphaNumeric(custSurname));
    
    // Validate telephone input from user
    String contTel;
    do {
      System.out.print("Contractor Tel Number: ");
      contTel = SCAN.nextLine();
      
      if(contTel.equals("0")) {
        return 0;
      } else if(!validTel(contTel)) {
        System.out.println("Invalid Tel Number. Valid examples: 012 345 6789 or 0123456789. Or '0' for main menu.");
      }
    } while(!validTel(contTel));
    
    // Validate email input from user
    String contEmail;
    do {
      System.out.print("Contractor Email: ");
      contEmail = SCAN.nextLine();
      contEmail = contEmail.trim();
      
      if(contEmail.equals("0")) {
        return 0;
      } else if(!validEmail(contEmail)) {
        System.out.println("Invalid email. Valid example: name@mail.com. Or '0' for main menu.");
      }
    } while(!validEmail(contEmail));
    
    String contAddress;
    do {
      System.out.print("Contractor Address: ");
      contAddress = SCAN.nextLine();
      
      if(contAddress.equals("0")) {
        return 0;
      } else if(!isAlphaNumeric(contAddress)) {
        System.out.println("Invalid input. Valid example: name@mail.com. Or '0' for main menu.");
      }
    } while(!isAlphaNumeric(contAddress));
    
    // Select last entry from table: SELECT * FROM Table ORDER BY ID DESC LIMIT 1
    result = statement.executeQuery("SELECT * FROM contractors ORDER BY id DESC LIMIT 1");
    result.next();
    int contId = result.getInt("id");
    contId++;
    
    // Architect details
    String archName;
    do {
      System.out.print("Architect Name: ");
      archName = SCAN.nextLine();
      
      if(archName.equals("0")) {
        return 0;
      } else if(!isAlphaNumeric(archName)) {
        System.out.println("Invalid input. Valid example: name@mail.com. Or '0' for main menu.");
      }
    } while(!isAlphaNumeric(archName));
    
    String archSurname;
    do {
      System.out.print("Architect Surname: ");
      archSurname = SCAN.nextLine();
      
      if(archSurname.equals("0")) {
        return 0;
      } else if(!isAlphaNumeric(archSurname)) {
        System.out.println("Invalid input. Valid example: name@mail.com. Or '0' for main menu.");
      }
    } while(!isAlphaNumeric(archSurname));
    
    // Validate telephone input from user
    String archTel;
    do {
      System.out.print("Architect Tel Number: ");
      archTel = SCAN.nextLine();
      
      if(archTel.equals("0")) {
        return 0;
      } else if(!validTel(archTel)) {
        System.out.println("Invalid Tel Number. Valid examples: 012 345 6789 or 0123456789. Or '0' for main menu.");
      }
    } while(!validTel(archTel));
    
    // Validate email input from user
    String archEmail;
    do {
      System.out.print("Architect Email: ");
      archEmail = SCAN.nextLine();
      archEmail = archEmail.trim();
      
      if(archEmail.equals("0")) {
        return 0;
      } else if(!validEmail(archEmail)) {
        System.out.println("Invalid email. Valid example: name@mail.com. Or '0' for main menu.");
      }
    } while(!validEmail(archEmail));
    
    String archAddress;
    do {
      System.out.print("Architect Address: ");
      archAddress = SCAN.nextLine();
      
      if(archAddress.equals("0")) {
        return 0;
      } else if(!isAlphaNumeric(archAddress)) {
        System.out.println("Invalid input. Please re-enter '0' for main menu.");
      }
    } while(!isAlphaNumeric(archAddress));
    
    // Select last entry from table: SELECT * FROM Table ORDER BY ID DESC LIMIT 1
    result = statement.executeQuery("SELECT * FROM architects ORDER BY id DESC LIMIT 1");
    result.next();
    int archId = result.getInt("id");
    archId++;
    
    // Generate project name if one is not entered
    String projName;
    do {
      System.out.print("Does the project have a name? (Y/N)");
      input = SCAN.nextLine();
      
      if(input.equals("Y") || input.equals("y")) {
        do {
          System.out.print("Project Name: ");
          projName = SCAN.nextLine();
          
          if(projName.equals("0")) {
            return 0;
          } else if(!isAlphaNumeric(projName)) {
            System.out.println("Invalid input. Please re-enter or '0' for main menu.");
          }
        } while(!isAlphaNumeric(projName));
        break;
      } else if(input.equals("N") || input.equals("n")) {
        projName = custSurname + " " + type;
        break;
      } else if(input.equals("0")) {
        return 0;
      } else {
        System.out.println("Please enter 'Y', for yes, or 'N', for no, or '0' for main menu.");
      }
    } while(true);
    
    System.out.println("New Client: " + custId + ", " + custName + ", " + custSurname + ", " + custTel + ", " + custEmail + ", " + custAddress);
    statement.executeUpdate("INSERT INTO customers VALUES (" + custId + ", '" + custName + "', '" + custSurname + "', '" + custTel + "', '" + custEmail + "', '" + custAddress + "')");
    
    System.out.println("New Contractor: " + contId + ", " + contName + ", " + contSurname + ", " + contTel + ", " + contEmail + ", " + contAddress);
    statement.executeUpdate("INSERT INTO contractors VALUES (" + contId + ", '" + contName + "', '" + contSurname + "', '" + contTel + "', '" + contEmail + "', '" + contAddress + "')");
    
    System.out.println("New Architect: " + archId + ", " + archName + ", " + archSurname + ", " + archTel + ", " + archEmail + ", " + archAddress);
    statement.executeUpdate("INSERT INTO architects VALUES (" + archId + ", '" + archName + "', '" + archSurname + "', '" + archTel + "', '" + archEmail + "', '" + archAddress + "')");
    
    System.out.println("New entry: " + projNum + ", " + projName + ", " + type + ", " + address + ", " + erf + ", " + fee + ", " + deadline + ", " + archId + ", " + contId + ", " + custId);
    return statement.executeUpdate("INSERT INTO projects VALUES (" + projNum + ", '" + projName + "', '" + type + "', '" + address + "', '" + erf + "', " + fee + ", " + 0 + ", '" + deadline + "', " + archId + ", " + contId + ", " + custId + ")");
  }
  
  /**
  *
  * Method to change a project's deadline.
  * <br>
  * This method is called when menu option 2 is entered.
  *
  * @param statement to connect to database
  * @return integer of rows affected
  */
  // Menu option 2
  private static int changeProjectDeadline(Statement statement) throws SQLException {
    System.out.println("** Change Deadline **");
    
    String input;
    do {
      System.out.println("Project number?");
      input = SCAN.nextLine();
      
      if(input.equals("0")) {
        return 0;
      } else if(!isNumeric(input)) {
        System.out.println("Warning! Invalid ID. Please re-enter or '0' for main menu.");
      }
    } while(!isNumeric(input));
    
    int number = Integer.parseInt(input.trim());
    ResultSet result = statement.executeQuery("SELECT deadline FROM projects WHERE number = " + number);
    result.next();
    System.out.println("Current due date: " + result.getString("deadline"));
    
    Date newDate;
    do {
      System.out.print("New due date? (yyyy-mm-dd): ");
      input = SCAN.nextLine();
      
      if(input.equals("0")) {
        return 0;
      }
      
      try {
        newDate = Date.valueOf(input.trim());
        break;
      } catch(Exception e) {
        System.out.println("Invalid date. Please re-enter or '0' for main menu.");
      }
    } while(true);
    
    return statement.executeUpdate("UPDATE projects SET deadline = '" + newDate + "' WHERE number = " + number);
  }
  
  /**
  *
  * Method to add payment to a project.
  * <br>
  * This method is called when menu option 3 is entered.
  *
  * @param statement to connect to database
  * @return integer of rows affected
  */
  // Menu option 3
  private static int changeAmountPaid(Statement statement) throws SQLException {
    System.out.println("** Add Payment **");
    
    String input;
    do {
      System.out.println("Project Number?");
      input = SCAN.nextLine();
      
      if(input.equals("0")) {
        return 0;
      } else if(!isNumeric(input)) {
        System.out.println("Warning! Invalid ID. Please re-enter or '0' for main menu.");
      }
    } while(!isNumeric(input));
    
    int number = Integer.parseInt(input.trim());
    
    ResultSet result = statement.executeQuery("SELECT totalfee, totalpaid FROM projects WHERE number = " + number);
    result.next();
    
    int fee = result.getInt("totalfee");
    int paid = result.getInt("totalpaid");
    
    System.out.println("Total cost: " + fee);
    System.out.println("Amount due: " + (fee - paid));
    
    do {
      System.out.println("Amount to pay?");
      input = SCAN.nextLine();
      
      if(input.equals("0")) {
        return 0;
      } else if(!isNumeric(input)) {
        System.out.println("Invalid input. Please enter amount to pay or '0' for main menu.");
      }
    } while(!isNumeric(input));
    
    int payment = Integer.parseInt(input.trim());
    int totalPaid = paid + payment;
    
    System.out.println("Payment added.\nAmount due: " + (fee - totalPaid));
    return statement.executeUpdate("UPDATE projects SET totalpaid = '" + totalPaid + "' WHERE number = " + number);
  }
  
  /**
  *
  * Method to update a contractor's details.
  * <br>
  * This method is called when menu option 4 is entered.
  *
  * @param statement to connect to database
  * @return integer of rows affected
  */
  // Menu option 4
  private static int updateContractor(Statement statement) throws SQLException {
    System.out.println("** Add Payment **");
    
    String input;
    do {
      System.out.println("Contractor ID?");
      input = SCAN.nextLine();
      
      if(input.equals("0")) {
        return 0;
      } else if(!isNumeric(input)) {
        System.out.println("Warning! Invalid ID. Please re-enter or '0' for main menu.");
      }
    } while(!isNumeric(input));
    
    int id = Integer.parseInt(input.trim());
    
    ResultSet result = statement.executeQuery("SELECT * FROM contractors WHERE id = " + id);
    result.next();
    
    String fname = result.getString("fname");
    String lname = result.getString("lname");
    String tel = result.getString("telnumber");
    String email = result.getString("email");
    String address = result.getString("address");
    
    System.out.println("Update name? (Y/N)");
    input = SCAN.nextLine();
    
    if(input.equals("y") || input.equals("Y")) {
      System.out.print("New first name: ");
      fname = SCAN.nextLine();
      fname = fname.trim();
      
      System.out.print("New last name: ");
      lname = SCAN.nextLine();
      lname = lname.trim();
    }
    
    System.out.println("Update tel number? (Y/N)");
    input = SCAN.nextLine();
    
    if(input.equals("y") || input.equals("Y")) {
      System.out.print("New tel: ");
      tel = SCAN.nextLine();
      tel = tel.trim();
    }
    
    System.out.println("Update email? (Y/N)");
    input = SCAN.nextLine();
    
    if(input.equals("y") || input.equals("Y")) {
      System.out.print("New email: ");
      email = SCAN.nextLine();
      email = email.trim();
    }
    
    System.out.println("Update address? (Y/N)");
    input = SCAN.nextLine();
    
    if(input.equals("y") || input.equals("Y")) {
      System.out.print("New address: ");
      address = SCAN.nextLine();
      address = address.trim();
    }
    
    System.out.print("Updated details: " + fname + ", " + lname + ", " + tel +", " + email + ", " + address);
    return statement.executeUpdate("UPDATE contractors SET fname = '" + fname + "', lname = '" + lname + "', telnumber = '" + tel + "', email = '" + email + "', address = '" + address + "' WHERE id = " + id);
  }
  
  /**
  *
  * Method to mark a project as complete and to generate and invoice if necessary.
  * <br>
  * This method is called when menu option 5 is entered.
  *
  * @param statement to connect to database
  */
  // Menu option 5
  private static void finaliseProject(Statement statement) throws SQLException {
    System.out.println("** Finalise Project **");
    
    String input;
    do {
      System.out.println("Project number?");
      input = SCAN.nextLine();
      
      if(input.equals("0")) {
        return;
      } else if(!isNumeric(input)){
        System.out.println("Invalid project number. Please re-enter or '0' for main menu.");
      }
    } while(!isNumeric(input));
    int number = Integer.parseInt(input.trim());
    
    ResultSet result = statement.executeQuery("SELECT * FROM projects WHERE number = " + number);
    result.next();
    
    int custId = result.getInt("customer_id");
    int totalFee = result.getInt("totalfee");
    int totalPaid = result.getInt("totalpaid");
    Date completionDate = result.getDate("completion_date");
    
    result = statement.executeQuery("SELECT * FROM customers WHERE id = " + custId);
    
    if(completionDate == null) {
      //Date now = new Date(System.currentTimeMillis());
      LocalDate now = LocalDate.now();
      
      if(totalPaid >= totalFee) {
        System.out.println("No invoice generated. Project paid in full.");
      } else {
        
        System.out.println("INVOICE" +
            "\nClient: " + result.getString("fname") + " " + result.getString("lname") +
            "\nTel: " + result.getString("telnumber") +
            "\nEmail: " + result.getString("email") +
            "\nAddress: " + result.getString("address") +
            "\nTotal Cost: " + totalFee +
            "\nTotal Received: " + totalPaid +
            "\nTotal Due: " + (totalFee - totalPaid)
            );
        
      }
      statement.executeUpdate("UPDATE projects SET completion_date = '" + now + "' WHERE number = " + number);
    } else {
      System.out.println("Project already finalised.");
      return;
    }
    
  }
  
  /**
  *
  * Displays all the projects that are not yet completed.
  * <br>
  * This method is called when menu option 6 is entered.
  *
  * @param statement to connect to database
  */
  // Menu option 6
  private static void incompleteProjects(Statement statement) throws SQLException {
    System.out.println("** Incomplete Projects **");
    
    ResultSet project = statement.executeQuery("SELECT * FROM projects");
    
    while(project.next()) {
      if(project.getDate("completion_date").equals(null)) {
        System.out.println(project.getInt("number") + " - " + project.getString("name") + ", " + project.getString("address") + "\n\tDeadline: " + project.getDate("deadline"));
      }
    }
    System.out.println("");
  }
  
  /**
  *
  * Displays all the projects that are past their deadlines.
  * <br>
  * This method is called when menu option 7 is entered.
  *
  * @param statement to connect to database
  * @return integer of rows affected
  */
  // Menu option 7
  private static void pastDue(Statement statement) throws SQLException {
    System.out.println("** Projects Past Due **");
    
    ResultSet project = statement.executeQuery("SELECT * FROM projects");
    Date now = new Date(System.currentTimeMillis());
    
    while(project.next()) {
      if(project.getDate("deadline").before(now)) {
        System.out.println(project.getInt("number") + " - " + project.getString("name") + ", " + project.getString("address") + "\n\tDeadline: " + project.getDate("deadline"));
      }
    }
    System.out.println("");
  }
  
  // Main Program
  public static void main(String[] args) {
    
    // Connect to database and create a statement object
    Statement statement;
    int rowsAffected;
    try {
      statement = createConnection("poisepms").createStatement();
      //readTable(statement, "architects");
      //readTable(statement, "customers");
      //readTable(statement, "contractors");
      //readTable(statement, "projects");
    } catch (SQLException e) {
      e.printStackTrace();
      statement = null;
    }
    
    // Declare input variable and set to anything except 0
    int menuInput = -1;
    
    while(menuInput != 0) {
      // Show options available in application
      showMenu();
      
      // Try to get a non-string input
      try {
        // Capture menu input from user
        menuInput = SCAN.nextInt();
        SCAN.nextLine(); // Bypass the 'Enter' pressed from user
        
        if(menuInput == 1) {
          rowsAffected = addProject(statement);
          System.out.println("Rows affected " + rowsAffected);
        } else if(menuInput == 2) {
          // Change due date of existing project
          rowsAffected = changeProjectDeadline(statement);
          System.out.println("Deadline changed.\nRows affected " + rowsAffected);
        } else if(menuInput == 3) {
          // Change the total amount paid for a project
          rowsAffected = changeAmountPaid(statement);
          System.out.println("Payment complete.\nRows affected " + rowsAffected);
        } else if(menuInput == 4) {
          // Update a contractor's details
          rowsAffected = updateContractor(statement);
          System.out.println("Update complete.\nRows affected " + rowsAffected);
        } else if(menuInput == 5) {
          // Finalize a project
          finaliseProject(statement);
        } else if(menuInput == 6) {
          // List incomplete projects
          incompleteProjects(statement);
        } else if(menuInput == 7) {
          // List projects that are past their due date
          pastDue(statement);
        } else if(menuInput == 0) {
          // Exit the program
          System.out.println("GOODBYE");
          break;
        } else {
          // Error for number not matching a menu option
          System.out.println("NOTICE: Only menu options accepted.\n");
        }
      } catch (Exception e) {
        // Error if anything except a number is entered
        System.out.println("WARNING! Please input a number.\n" + e);
        SCAN.nextLine(); // Bypass enter that is pressed
      }
    }
    
    // Close the scanner
    SCAN.close();
  }
  
  /**
  *
  * Runs a validator on a contact number.
  * <br>
  * This method is called when the user enters a new contact number for a person.
  *
  * @param tel contact number to be validated
  * @return true or false for tel number
  */
  private static boolean validTel(String tel) {
    String regex = "^(\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4})" + "|^\\d{10}$";
    
    Pattern pattern = Pattern.compile(regex);
    
    Matcher matcher = pattern.matcher(tel);
    
    return matcher.matches();
  }
  
  /**
  *
  * Runs a validator on an email address.
  * <br>
  * This method is called when the user enters a new email for a person.
  *
  * @param email email address to be validated
  * @return true or false for email address
  */
  private static boolean validEmail(String email) {
    String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    
    Pattern pattern = Pattern.compile(regex);
    
    Matcher matcher = pattern.matcher(email);
    
    return matcher.matches();
  }
  
  /**
  *
  * Runs a validator on some string the user inputs.
  * <br>
  * This method is called to validate the input from a user.
  *
  * @param string s to take any string to be tested
  * @return true or false
  */
  private static boolean isAlphaNumeric(String s) {
    String regex = "^[a-zA-Z0-9,]*$";
    
    Pattern pattern = Pattern.compile(regex);
    
    Matcher matcher = pattern.matcher(s);
    
    return matcher.matches();
  }
  
  /**
  *
  * Runs a validator on some string that needs to be converted to a number.
  * <br>
  * This method is called to validate the input from a user.
  *
  * @param string s to take any string
  * @return true or false
  */
  private static boolean isNumeric(String s) {
    String regex = "^[0-9]*$";
    
    Pattern pattern = Pattern.compile(regex);
    
    Matcher matcher = pattern.matcher(s);
    
    return matcher.matches();
  }
  
}
