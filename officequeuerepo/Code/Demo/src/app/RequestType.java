package app;

import java.util.LinkedList;
import java.util.List;

public class RequestType {
	private Integer id;
	private String tagName;
	private Float averageTime;
	LinkedList<Ticket> queue = new LinkedList<>();
	
	private static int idCount = 0;
	
	RequestType(String tagName, Float averageTime) {
		this.id = idCount++;
		this.tagName = tagName;
		this.averageTime = averageTime;
	}
	
	void addTicket(Ticket t) {
		queue.addLast(t);
	}
	
	Integer removeTicket() { // Return the first ticket in the queue
		return queue.removeFirst().getId();
	}
	
	@Override
	public String toString() {
		return this.tagName;
	}
	
	void reset() {
		queue.clear();
	}
	
	Integer count() {
		return queue.size();
	}
	
	Integer getId() {
		return this.id;
	}
	
	String getTagName() {
		return this.tagName;
	}
	
	Float getAverageTime() {
		return this.averageTime;
	}

	String getInfoQueues(){
		return "Numbers of people in queue"+this.toString()+" : "+this.count()+"\n";
	}

	Integer countTicketAhead(Ticket t) { // AKA count()
		Integer count = 0;
		for (Ticket tk: queue) {
			if(tk.equals(t))
				return count;
			count++;
		}
		return -1; // Ticket not found in the queue
	}
	
}
