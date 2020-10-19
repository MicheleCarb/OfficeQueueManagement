package app;

import java.util.List;

public class Counter {
	private Integer id;
	List<Integer> requestTypeServable;

	Integer count() {
		return requestTypeServable.size();
	}


	
	Boolean canServeRequestType(Integer idRequestType) {
		return requestTypeServable.stream().findAny().isPresent();
	}
}
