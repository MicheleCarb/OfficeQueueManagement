package app;

import java.util.List;

public class Counter {
	private Integer id;
	List<Integer> requestTypeServable;
	
	private static int countId = 0;
	
	Counter(List<Integer> requestTypeServable) {
		this.id = countId++;
		this.requestTypeServable = requestTypeServable;
	}
	
	void nextCustomer(List<RequestType> requestTypes) {
		
	}
	
	Boolean canServeRequestType(Integer idRequestType) {
		return requestTypeServable.stream().findAny().isPresent();
	}
}
