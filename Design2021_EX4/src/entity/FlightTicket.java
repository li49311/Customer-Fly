package entity;

import util.SeatClass;

public class FlightTicket {
	
	private int orderNum;
	private int ticketNum;
	private SeatClass seatClass;
	private Customer customer;
	private Flight flight;
	
	
	
	public FlightTicket(int orderNum, int ticketNum, SeatClass seatClass, Customer customer, Flight flight) {
		super();
		this.orderNum = orderNum;
		this.ticketNum = ticketNum;
		this.seatClass = seatClass;
		this.customer = customer;
		this.flight = flight;
	}
	
	
	@Override
	public String toString() {
		return "FlightTicket [orderNum=" + orderNum + ", ticketNum=" + ticketNum + ", seatClass=" + seatClass
				+ ", customer=" + customer + ", flight=" + flight + "]";
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
	public SeatClass getSeatClass() {
		return seatClass;
	}
	public void setSeatClass(SeatClass seatClass) {
		this.seatClass = seatClass;
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


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + orderNum;
		result = prime * result + ticketNum;
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FlightTicket other = (FlightTicket) obj;
		if (orderNum != other.orderNum)
			return false;
		if (ticketNum != other.ticketNum)
			return false;
		return true;
	}
	

	
	
	

	
	


	

}
