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
	
	void removeTicket() {
		queue.removeFirst();
	}
	
	@Override
	public String toString() {
		return tagName;
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
	
	Float getAverageTime() {
		return this.averageTime;
	}

	String getInfoQueues(){
		return "Numbers of people in queue"+this.toString()+" : "+this.count()+"\n";
	}
	
}
