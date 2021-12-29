package entity;

import java.sql.Timestamp;
import java.util.Date;

import util.FlightStatus;

public class Flight {
	
	private String flightNum;
	private Timestamp departureTime;
	private Timestamp landingTime;
	private FlightStatus status;
	private Airplane airplane;
	private Airport departureAirport;
	private Airport landingAirport;
	private Date updateDate;
	
	
	public Flight(String flightNum, Timestamp departureTime, Timestamp landingTime, FlightStatus status,
			Airplane airplane, Airport departureAirport, Airport landingAirport, Date updateDate) {
		super();
		this.flightNum = flightNum;
		this.departureTime = departureTime;
		this.landingTime = landingTime;
		this.status = status;
		this.airplane = airplane;
		this.departureAirport = departureAirport;
		this.landingAirport = landingAirport;
		this.updateDate = updateDate;
	}
	
	public Flight(String flightNum, Timestamp departureTime, Timestamp landingTime, FlightStatus status,
			Airplane airplane, Airport departureAirport, Airport landingAirport) {
		super();
		this.flightNum = flightNum;
		this.departureTime = departureTime;
		this.landingTime = landingTime;
		this.status = status;
		this.airplane = airplane;
		this.departureAirport = departureAirport;
		this.landingAirport = landingAirport;
	}
	
	
	
	public Flight(String flightNum, String airplane, String departureAirport, String landingAirport) {
		super();
		this.flightNum = flightNum;
		this.airplane = new Airplane(airplane);
		this.departureAirport = new Airport(departureAirport);
		this.landingAirport = new Airport(landingAirport);
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
	public FlightStatus getStatus() {
		return status;
	}
	public void setStatus(FlightStatus status) {
		this.status = status;
	}
	public Airplane getAirplane() {
		return airplane;
	}
	public void setAirplane(Airplane airplane) {
		this.airplane = airplane;
	}
	public Airport getDepartureAirport() {
		return departureAirport;
	}
	public void setDepartureAirport(Airport departureAirport) {
		this.departureAirport = departureAirport;
	}
	public Airport getLandingAirport() {
		return landingAirport;
	}
	public void setLandingAirport(Airport landingAirport) {
		this.landingAirport = landingAirport;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Override
	public String toString() {
		return "flightNum=" + flightNum + ", departureTime=" + departureTime + ", landingTime=" + landingTime;
	}
	
	
	
	

}
