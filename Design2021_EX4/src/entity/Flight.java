package entity;

import java.sql.Timestamp;

public class Flight {
	
	private String flightNum;
	private Timestamp departureTime;
	private Timestamp landingTime;
	private String status;
	private String tailNumber;
	private String departureAirport;
	private String landingAirport;
	public Flight(String flightNum, Timestamp departureTime, Timestamp landingTime, String status, String tailNumber,
			String departureAirport, String landingAirport) {
		super();
		this.flightNum = flightNum;
		this.departureTime = departureTime;
		this.landingTime = landingTime;
		this.status = status;
		this.tailNumber = tailNumber;
		this.departureAirport = departureAirport;
		this.landingAirport = landingAirport;
	}
	public String getFlightNum() {
		return flightNum;
	}
	public void setFlightNum(String flightNum) {
		this.flightNum = flightNum;
	}
	public Timestamp getDepartureTime() {
		return departureTime;
	}
	public void setDepartureTime(Timestamp departureTime) {
		this.departureTime = departureTime;
	}
	public Timestamp getLandingTime() {
		return landingTime;
	}
	public void setLandingTime(Timestamp landingTime) {
		this.landingTime = landingTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTailNumber() {
		return tailNumber;
	}
	public void setTailNumber(String tailNumber) {
		this.tailNumber = tailNumber;
	}
	public String getDepartureAirport() {
		return departureAirport;
	}
	public void setDepartureAirport(String departureAirport) {
		this.departureAirport = departureAirport;
	}
	public String getLandingAirport() {
		return landingAirport;
	}
	public void setLandingAirport(String landingAirport) {
		this.landingAirport = landingAirport;
	}
	
	

}
