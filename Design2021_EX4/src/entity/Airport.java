package entity;

public class Airport {
	private String airportCode;
	private String city;
	private String country;
	public Airport(String airportCode, String city, String country) {
		super();
		this.airportCode = airportCode;
		this.city = city;
		this.country = country;
	}
	public String getAirportCode() {
		return airportCode;
	}
	public void setAirportCode(String airportCode) {
		this.airportCode = airportCode;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
	
}
