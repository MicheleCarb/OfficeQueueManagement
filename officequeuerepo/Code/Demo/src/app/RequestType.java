package app;

import java.util.List;

public class RequestType {
	private Integer id;
	private String tagName;
	private Float averageTime;
	List<Ticket> queue;
	
	void removeTicket() {
		
	}
	
	@Override
	public String toString() {
		return tagName;
	}
	
	void reset() {
		
	}
}
