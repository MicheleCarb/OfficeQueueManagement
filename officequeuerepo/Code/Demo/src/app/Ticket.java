package app;

public class Ticket {
	private Integer id;
	private Integer idRequestType;

	private static int counter = 0;

	Ticket(int idRequestType){
		this.id = counter++;
		this.idRequestType = idRequestType;
	}
	
	Integer getId() {
		return this.id;
	}
}
