package app;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		try {
			//Vars
			Service service = new Service();
			boolean running = true;
			Scanner scanner = new Scanner(System.in);
			//Configuration Phase
			System.out.println("--- Configuration phase\n" +
					"Insert < TagName AverageTime > or 0 to end phase");
			//TODO Config phase

			//Start program
			int input;
			while(running) {
				System.out.println("--- Welcome to the office! Choose an option:\n" +
						"1 - Get a ticket\n" +
						"2 - Employee actions\n" +
						"3 - Manager actions\n" +
						"4 - Exit ( JUST FOR DEMO )");
				input = scanner.nextInt(); //Get input from console
				//Menu handler
				switch (input){
					case 1:
						//TODO: Handle getting ticket
						break;
					case 2:
						//TODO: Employee actions
						break;
					case 3:
						//Manager actions
						System.out.println("Type query option followed by time filter (d,w,m)\n" +
								"Example: 1 d\n" +
								"1 - How many customers have been served for each request type\n" +
								"2 - The number of customers each counter has served divided by request type");
						//Getting input
						input = scanner.nextInt();
						char filter = scanner.next().charAt(0);
						if (input == 1) service.printAllStats(filter);
						else if(input == 2) service.printAllStatsByCounter(filter);
						else System.err.println("Error on input");
						break;
					case 4:
						running = false;
						System.out.println("Shutting down ...");
						break;
				}
			}

			service.officeDAO.close();
		} catch (ClassNotFoundException | SQLException |InputMismatchException exc){
			exc.printStackTrace();
		}

	}

}
