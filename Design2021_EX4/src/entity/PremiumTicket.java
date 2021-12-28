package entity;

public class PremiumTicket extends FlightTicket {
	
	double luggageWeight;
	String Request1;
	String Request2;
	String Request3;


	public PremiumTicket(int orderNum, int ticketNum, Customer customer, Flight flight, double luggageWeight,
			String request1, String request2, String request3) {
		super(orderNum, ticketNum, customer.getPassportNum(), flight.getFlightNum());
		this.luggageWeight = luggageWeight;
		Request1 = request1;
		Request2 = request2;
		Request3 = request3;
	}
	
	public double getLuggageWeight() {
		return luggageWeight;
	}
	public void setLuggageWeight(double luggageWeight) {
		this.luggageWeight = luggageWeight;
	}
	public String getRequest1() {
		return Request1;
	}
	public void setRequest1(String request1) {
		Request1 = request1;
	}
	public String getRequest2() {
		return Request2;
	}
	public void setRequest2(String request2) {
		Request2 = request2;
	}
	public String getRequest3() {
		return Request3;
	}
	public void setRequest3(String request3) {
		Request3 = request3;
	}
}
