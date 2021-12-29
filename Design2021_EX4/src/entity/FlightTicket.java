package entity;

public class FlightTicket {
	
	private int orderNum;
	private int ticketNum;
	private String seatClass;
	private Customer customer;
	private Flight flight;
	
	
	public FlightTicket(int orderNum, int ticketNum, String seatClass, Customer customer, Flight flight) {
		super();
		this.orderNum = orderNum;
		this.ticketNum = ticketNum;
		this.seatClass = seatClass;
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

	public void setCustomerPassportNum(Customer customer) {
		this.customer = customer;
	}

	public Flight getFlightNum() {
		return flight;
	}

	public void setFlightNum(Flight flight) {
		this.flight = flight;
	}
	
	

	public String getSeatClass() {
		return seatClass;
	}

	public void setSeatClass(String seatClass) {
		this.seatClass = seatClass;
	}

	@Override
	public String toString() {
		return "orderNum=" + orderNum + ", ticketNum=" + ticketNum + ", seatClass=" + seatClass
				+ ", customer=" + customer + ", flight=" + flight + "]";
	}
	
	

	
	


	

}
