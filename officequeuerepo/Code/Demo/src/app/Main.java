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

            //Configuration Phase
            System.out.println("--- Configuration phase\n");
            while (true) {
                System.out.println("Insert a tag name and an average time separated by a space in order to add a request type; enter a digit to skip:");
                try {
                    if (scanner.hasNextInt()) {
                        scanner.nextInt(); // skip it
                        break;
                    }
                    service.createDefinition(scanner.next("[A-Za-z_][A-Za-z_0-9]*"), scanner.nextFloat());
                } catch (InputMismatchException e) {
                    System.out.println("The last definition contained errors and so it hasn't been added.\n\n");
                    scanner.nextLine(); // get rid of the wrong input
                }
            }

            //Counter setter
            while (true) {
                System.out.println("Do you want to add a counter?\n" +
                        "1 - Yes\n" +
                        "2 - No");
                if (scanner.nextInt() == 1) service.createCounter();
                else break;
            }

            //Start program
            int input;
            //Retrieve a date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //Use calendar
            Calendar cal = Calendar.getInstance();
            String date = sdf.format(cal.getTime()); //date in string
            service.resetQueues();
            while (running) {
                service.watchTicketsInQueues();
                System.out.println("Day: " + date + "\n");
                System.out.println("--- Welcome to the office! Choose an option:\n" +
                        "1 - Get a ticket\n" +
                        "2 - Employee actions\n" +
                        "3 - Manager actions\n" +
                        "4 - Exit ( JUST FOR DEMO )");
                input = scanner.nextInt(); //Get input from console
                //Menu handler
                switch (input) {
                    case 1:
                        Ticket t = service.getTicket();
                        System.out.println(t);
                        break;

                    case 2:
                        System.out.println("1 - Call next customer\n");
                        System.out.println("2 - Return to Main Menu\n");
                        input = scanner.nextInt();
                        switch (input) {
                            case 1:
                                System.out.println("What's your counter number?");
                                service.callNextCustomer(scanner.nextInt());
                                break;
                            default:
                                break;
                        }
                        break;

                    case 3:
                        //Manager actions
                        System.out.println("Type query option followed by time filter (d,w,m)\n" +
                                "Example: 1 d\n" +
                                "1 - How many customers have been served for each request type\n" +
                                "2 - The number of customers each counter has served divided by request type");
                        //Getting input
                        char filter = 0;
                        try {
                            input = scanner.nextInt();
                            filter = scanner.next().charAt(0);
                        }catch ( InputMismatchException exc ){
                            System.err.println("Error on input!");
                        }
                        if (input == 1) service.printAllStats(filter);
                        else if (input == 2) service.printAllStatsByCounter(filter);
                        else System.err.println("Error on input");
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

            service.officeDAO.close();
        } catch (ClassNotFoundException | SQLException | InputMismatchException exc) {
            exc.printStackTrace();
        }

    }

}
