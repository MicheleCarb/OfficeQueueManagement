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

	@Override
	public String toString() { return "ticket id:" + this.id + "waiting time:" + this.Tr ;}

	public Integer getId() { return this.id; }
	public Integer getIdRequestType() { return idRequestType; }
	public float getTr() { return Tr; }
}
