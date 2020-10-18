package app;

import java.util.List;
import java.util.stream.Collectors;

public class Service {
	List<RequestType> requestTypes;
	List<Counter> counters;
	
	
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
}
