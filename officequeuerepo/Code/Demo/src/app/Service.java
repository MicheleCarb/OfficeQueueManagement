package app;

import java.io.Console;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class Service {
	List<RequestType> requestTypes;
	List<Counter> counters;
	DAO officeDAO;

	public Service() throws SQLException, ClassNotFoundException {
		this.officeDAO = new DAO();
	}

	public void printAllStats(Character DayWeekMonth) throws SQLException {
		//Preparing for formatted output
		String typeDate;
		switch (DayWeekMonth) {
			case 'w' -> typeDate = "Week";
			case 'm' -> typeDate = "Month";
			default -> typeDate = "Date";
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
			case 'w' -> typeDate = "Week";
			case 'm' -> typeDate = "Month";
			default -> typeDate = "Date";
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
	
	Ticket getTicket() {
		return null;
	}
	
	void watchTicketsInQueues() {
		
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
				.filter(requestType -> counter.canServeRequestType(requestType.getId()))
				.sorted((requestType1, requestType2) -> {
					if (requestType1.count() != requestType2.count())
						return requestType1.count() - requestType2.count();
					else
						return requestType1.getAverageTime().compareTo(requestType2.getAverageTime());
				})
				.findFirst()
				.get()
				;
		
		queue.removeTicket();
		
	}
	
	void watchStatistics() {
		
	}

	void printAllStats(){

	}

	//reset all queues every morning
	void resetQueues(){
		for(RequestType rt:requestTypes){
			rt.reset();
		}
		System.out.println("All queues are empty");
	}
}
