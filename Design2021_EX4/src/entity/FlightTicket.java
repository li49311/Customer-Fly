package entity;

public class FlightTicket {
	
	private int orderNum;
	private int ticketNum;
	private Customer customer;
	private Flight flight;
	
	public FlightTicket(int orderNum, int ticketNum, Customer customer, Flight flight) {
		super();
		this.orderNum = orderNum;
		this.ticketNum = ticketNum;
		this.customer = customer;
		this.flight = flight;
	}

	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	public int getTicketNum() {
		return ticketNum;
	}

	public void setTicketNum(int ticketNum) {
		this.ticketNum = ticketNum;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Flight getFlight() {
		return flight;
	}

	public void setFlight(Flight flight) {
		this.flight = flight;
	}
	
	

}
