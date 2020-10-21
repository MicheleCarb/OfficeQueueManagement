package app;

import java.io.Console;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Service {
	private LinkedList<RequestType> requestTypes = new LinkedList<>();
	private List<Counter> counters = new LinkedList<>();
	private DAO officeDAO;

	public Service() throws SQLException, ClassNotFoundException {
		this.officeDAO = new DAO();
	}
	

	protected void finalize() throws SQLException {
		officeDAO.close();
	}

	public void printAllStats(Character DayWeekMonth) throws SQLException {
		//Preparing for formatted output
		String typeDate;
		switch (DayWeekMonth) {
			case 'w' : typeDate = "Week";
			case 'm' : typeDate = "Month";
			default : typeDate = "Date";
		}
		ResultSet rs = officeDAO.getAllStats(DayWeekMonth);
		System.out.printf("%20s %20s %20s\n","Request Type", "Customers", typeDate);
		while(rs.next()){
			System.out.printf("%20s %20d %20s\n",rs.getString(1),rs.getInt(2),rs.getString(3));
		}
	}

	public void printAllStatsByCounter(Character DayWeekMonth) throws SQLException {
		//Preparing for formatted output
		String typeDate;
		switch (DayWeekMonth) {
			case 'w' : typeDate = "Week";
			case 'm' : typeDate = "Month";
			default : typeDate = "Date";
		}
		ResultSet rs = officeDAO.getAllStatsByCounter(DayWeekMonth);
		System.out.printf("%20s %20s %20s %20s\n","Request Type", "Counter", "Customers", typeDate);
		while(rs.next()){
			System.out.printf("%20s %20d %20s %20s\n",
					rs.getString(1),rs.getInt(2),
					rs.getInt(3),rs.getString(4));
		}
	}

	void createDefinition(String tagName, Float averageTime) {
		requestTypes.add(new RequestType(tagName, averageTime));
	}
	
	void createCounter() {
		System.out.println("Available request types:");
		requestTypes.forEach(requestType -> System.out.print(requestType.getId() + " - " + requestType.getTagName() + "\n"));
		
		System.out.print("Enter which request types the new counter can serve, through a comma-separated list of ids: ");
		Scanner scanner = new Scanner(System.in);
		counters.add(new Counter(
				Arrays.asList(scanner.nextLine().split("[,]"))
				.stream().map(numberAsString -> Integer.parseUnsignedInt(numberAsString))
				.collect(Collectors.toList()))
		);
	}
	
	Ticket getTicket() {
     //receive from the menu the id of the selected type of request

		System.out.println("Available request types:");
		requestTypes.forEach(requestType -> System.out.println(requestType.getId() + " - " + requestType.getTagName() + "\n"));
		
		
		float Tr;
		
		System.out.println("Enter which request type you want: ");
		Scanner scanner = new Scanner(System.in);
		Integer idRequestType = scanner.nextInt();

		float tr = requestTypes.stream()
				.filter(requestType -> requestType.getId().equals(idRequestType))
				.collect(Collectors.toList()).get(0)
				.getAverageTime();

		Integer nr = requestTypes.stream()
				.filter(requestType -> requestType.getId().equals(idRequestType))
				.collect(Collectors.toList()).get(0)
				.count();

		float sum = 0;
		for (Counter c: counters) {
			sum += (((float)1/c.count()) * (c.canServeRequestType(idRequestType) ? 1:0));
		}

		Tr = tr * ((nr / sum) + (float)1/2);

		Ticket t = new Ticket(idRequestType, Tr);

		requestTypes.stream()
				.filter( requestType -> requestType.getId().equals(idRequestType) )
				.collect(Collectors.toList()).get(0)
				.addTicket(t);

		return t;
	}
	
	//Show the waiting tickets in the digital display board
	void watchTicketsInQueues(Ticket[] ticsToShow) {
		System.out.println("Ticket number\t" + "RequestType\t" + "Waiting time");
		for (Ticket tic: ticsToShow) {
			System.out.println(tic.getId() + "\t" + tic.getIdRequestType() + "\t" + tic.getTr());
		}
	}
	
	void callNextCustomer(Integer counterId) {
		Counter counter = counters.get(counterId);
		
		// Sorting requires n*log(n) steps, while the following approach is faster.
		
		
//		List<RequestType> servableRequests = requestTypes.stream()
//			.filter(requestType -> counter.canServeRequestType(requestType.getId()))
//			.collect(Collectors.toList());
//		
//		Integer currentMaxSize = servableRequests.stream()
//			.max((requestType1, requestType2) -> requestType1.count() - requestType2.count())
//			.get()
//			.count();
//			;
//			
//		servableRequests = servableRequests.stream()
//			.filter(requestType -> requestType.count().equals(currentMaxSize))
//			.collect(Collectors.toList());
//			;
//		
//		servableRequests.stream()
//			.max((requestType1, requestType2) -> requestType1.getAverageTime().compareTo(requestType2.getAverageTime()))
//			.get()
//			;


		RequestType queue = requestTypes.stream()
				.filter(requestType -> {
					//System.out.println(counter.canServeRequestType(requestType.getId()));
					return counter.canServeRequestType(requestType.getId());
				})
				.sorted(((requestType1, requestType2) -> {
					if (!requestType1.count().equals(requestType2.count()))
						return requestType2.count().compareTo(requestType1.count());
					else
						return requestType2.getAverageTime().compareTo(requestType1.getAverageTime());
				}))
				.filter(requestType -> {
					//System.out.println(requestType);
					return true;
				})
				.findFirst()
				.get()
				;
		
		Integer ticketId;
		try {
			ticketId = queue.removeTicket();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return;
		}
		printTurn(queue.getId(), ticketId, queue.count());

	}

	void printTurn(Integer id, Integer ticketId, Integer peopleWaiting) {
		System.out.printf("%50s\n","Counters");
		System.out.printf("%10s - %10s - %10s\n", "Counter", "Ticket #", "People in queue");
		System.out.printf("%10s - %10s - %10s\n", id, ticketId, peopleWaiting);
	}

	//reset all queues every morning
	void resetQueues(){
		for(RequestType rt:requestTypes){
			rt.reset();
		}
		System.out.println("All queues are empty");
	}
}
