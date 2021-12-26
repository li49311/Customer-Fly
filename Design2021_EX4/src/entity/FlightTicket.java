package entity;

public class FlightTicket {
	
	private int orderNum;
	private int ticketNum;
	private int customerPassportNum;
	private String flightNum;
	
	
	public FlightTicket(int orderNum, int ticketNum, int customerPassportNum, String flightNum) {
		super();
		this.orderNum = orderNum;
		this.ticketNum = ticketNum;
		this.customerPassportNum = customerPassportNum;
		this.flightNum = flightNum;
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

	public int getCustomerPassportNum() {
		return customerPassportNum;
	}

	public void setCustomerPassportNum(int customerPassportNum) {
		this.customerPassportNum = customerPassportNum;
	}

	public String getFlightNum() {
		return flightNum;
	}

	public void setFlightNum(String flightNum) {
		this.flightNum = flightNum;
	}

	
	


	

}
