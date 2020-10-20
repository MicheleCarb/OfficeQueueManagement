package app;

public class Ticket {
	private Integer id;
	private Integer idRequestType;
	private float Tr; // Waiting time

	private static int counter = 0;

	Ticket(int idRequestType, float Tr){
		this.id = counter++;
		this.idRequestType = idRequestType;
		this.Tr = Tr;
	}
	
	Integer getId() {
		return this.id;
	}
}
