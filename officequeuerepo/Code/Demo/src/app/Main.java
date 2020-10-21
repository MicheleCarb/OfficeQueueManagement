package app;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Main {

    public static void main(String[] args) {
        try {
            //Vars
            Service service = new Service();
            boolean running = true;
            Scanner scanner = new Scanner(System.in);
            Integer command;

            //Configuration Phase
            System.out.print("--- Configuration phase ---\n\n");
            
            command = 1;
            System.out.print("Add the first request type:\n");
            while (command.equals(1)) {
            	System.out.print("Insert a tag name and an average time separated by a space: ");
            	try {
                    service.createDefinition(scanner.next("[A-Za-z_][A-Za-z_0-9]*"), scanner.nextFloat());
                } catch (InputMismatchException e) {
                    System.out.println("\nThe last definition contained errors and so it hasn't been added.\n\n");
                    scanner.nextLine(); // get rid of the wrong input
                }
            	
            	System.out.print("\nDo you want to add further request types?\n" +
                        "\t1 - Yes\n" +
                        "\t2 - No\n" +
                        "Command: ");
            	command = scanner.nextInt();
            	System.out.print("\n");
            }
            
//            while (true) {
//                System.out.print("\nInsert a tag name and an average time separated by a space in order to add a request type; enter a digit to skip:");
//                try {
//                    if (scanner.hasNextInt()) {
//                        scanner.nextInt(); // skip it
//                        break;
//                    }
//                    service.createDefinition(scanner.next("[A-Za-z_][A-Za-z_0-9]*"), scanner.nextFloat());
//                } catch (InputMismatchException e) {
//                    System.out.println("The last definition contained errors and so it hasn't been added.\n\n");
//                    scanner.nextLine(); // get rid of the wrong input
//                }
//            }

            command = 1;
            System.out.print("Add the first counter:\n");
            while (command.equals(1)) {
                service.createCounter();
                
                System.out.print("\nDo you want to add further counters?\n" +
                        "\t1 - Yes\n" +
                        "\t2 - No\n" +
                        "Command: ");
                command = scanner.nextInt();
                System.out.print("\n");
            }

            //Retrieve a date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //Use calendar
            Calendar cal = Calendar.getInstance();
            String date = sdf.format(cal.getTime()); //date in string
            System.out.print("Today is " + date + "\n");
            service.resetQueues();
            
            System.out.print("--- End of configuration phase ---\n\n");
            
            while (running) {
                service.watchTicketsInQueues();
                System.out.print("\n\nChoose your set of actions:\n" +
                        "\t1 - Customer actions\n" +
                        "\t2 - Employee actions\n" +
                        "\t3 - Manager actions\n" +
                        "\t4 - Exit\n" +
                        "Command: ");
                command = scanner.nextInt();
                System.out.print("\n\n");
                //Menu handler
                switch (command) {
                
                    case 1:
                    	System.out.print("As a customer I want to:\n" +
                    					"\t1 - Get ticket on a request type\n" +
                        				"\t2 - Return to Main Menu\n" +
                        				"Command: ");
                        command = scanner.nextInt();
                        System.out.print("\n\n");
                        switch (command) {
                        
                            case 1:
                            	Ticket t = service.getTicket();
                                System.out.println(t);
                                break;
                                
                            default:
                                break;
                        }
                        
                        break;

                    case 2:
                    	System.out.print("As an employee I want to:\n" +
                    					"\t1 - Call next customer\n" +
                        				"\t2 - Return to Main Menu\n" +
                						"Command: ");
                        command = scanner.nextInt();
                        System.out.print("\n\n");
                        switch (command) {
                            case 1:
                                System.out.println("Enter your counter id: ");
                                service.callNextCustomer(scanner.nextInt());
                                System.out.print("\n\n");
                                break;
                            default:
                                break;
                        }
                        break;

                    case 3:
                        //Manager actions
                    	System.out.print("As a manager I want to:\n" +
                    			"\t1 - Know how many customers have been served for each request type\n" +
                                "\t2 - Know the number of customers each counter has served divided by request type\n" +
                        		"\t3 - Return to Main Menu\n" +
        						"Command: ");
                        command = scanner.nextInt();
                        System.out.print("\n");
                        
                        // The user should go back to the main menu without telling the time frame
                        if (command == 3) break;
                        
                        System.out.print("Data should be grouped by:\n" +
                                "\t1 - Day\n" +
                                "\t2 - Week\n" +
                                "\t3 - Month\n" +
        						"Command: ");
                        Integer timeFrame = scanner.nextInt();
                        System.out.print("\n");
                        
                        Character filter;
                        if (timeFrame.equals(1)) filter = 'd';
                        else if (timeFrame.equals(2)) filter = 'w';
                        else filter = 'm';
                        
                        if (command == 1) service.printAllStats(filter);
                        else if (command == 2) service.printAllStatsByCounter(filter);
                        else System.err.print("Command not recognized\n\n");
                        break;
                        
                    case 4:
                        running = false;
                        System.out.println("Shutting down ...");
                        break;
                    default:
                        System.out.println("Error input inserted. Try again\n");
                        break;
                }
            }

        } catch (ClassNotFoundException | SQLException | InputMismatchException exc) {
            exc.printStackTrace();
        }

    }

}
