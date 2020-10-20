package app;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

//class that validate (or not) a date inserted by the manager
class DateValidator {

	public boolean isThisDateValid(String dateToValidate, String dateFromat){

		if(dateToValidate == null){
			return false;
		}

		SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
		sdf.setLenient(false);

		try {

			//if not valid, it will throw ParseException
			Date date = sdf.parse(dateToValidate);
			System.out.println(date);

		} catch (ParseException e) {

			e.printStackTrace();
			return false;
		}

		return true;
	}

}

public class Main {

	public static void main(String[] args) {
		try {
			//Vars
			Service service = new Service();
			boolean running = true;
			boolean running_date=true;
			Scanner scanner = new Scanner(System.in);
			String date = null; //insert date of day of work
			String datebefore = null;
			int day,month,year;
			DateValidator dv=new DateValidator();
			//Configuration Phase
			
			System.out.println("--- Configuration phase\n");
			try {
				while (true) {
					System.out.print("Insert a tag name and an average time separated by a space in order to add a request type; enter a digit to skip:");
					if (scanner.hasNextInt()) {
						scanner.nextInt(); // skip it
						break;
					}
					service.createDefinition(scanner.next("[A-Za-z_][A-Za-z_0-9]*"), scanner.nextFloat());
				}
			} catch (InputMismatchException e) {
				System.out.println("The last definition contained errors and so it hasn't been added.\n\n");
				scanner.nextLine(); // get rid of the wrong input
			}
			
			while (true) {
				System.out.print("Do you want to add a counter? (Yes/No) ");
				if (scanner.next().equals("Yes")) {
					service.createCounter();
				} else {
					break;
				}
			}

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
						if(date==null)
							System.out.println("Call the manager to insert a day\n");


						break;
					case 2:
						//TODO: Employee actions
						if(date==null) {
							System.out.println("Call the manager to insert a day\n");
							break;
						}
						
						System.out.println("1 - Call next customer\n");
						System.out.println("2 - Return to Main Menu\n");
						input = scanner.nextInt();
						switch (input){
							case 1:
								System.out.println("What's your counter number?");
								service.callNextCustomer(scanner.nextInt());
								break;
								
							case 2:
								break;
						}
						
					case 3:
						//Manager actions
						System.out.println("1 - Insert the date of day\n");
						System.out.println("2 - Show Statistics\n");
						System.out.println("3 - Return to Main Menu\n");
						input = scanner.nextInt();
						switch (input){
							case 1:
								while(running_date){
									System.out.println("Select day (dd): \n");
									day = scanner.nextInt();
									System.out.println("Select month (MM): \n");
									month = scanner.nextInt();
									System.out.println("Select year (yyyy): \n");
									year = scanner.nextInt();
									date= day +"/"+ month +"/"+ year;
									if(dv.isThisDateValid(date,"dd/MM/yyyy")){
										running_date=false;

									}else{
										System.out.println("Invalid Format Date inserted. Please try again...\n");
									}
								}
								if (!date.equals(datebefore)) {
									service.resetQueues();
									datebefore = date;
								}
								break;
							case 2:
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
							case 3:
								//maybe adding code...
								break;
						}


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
