package entity;

import java.util.ArrayList;
import java.util.HashSet;

public class Airplane {
	
	private String tailNumber;
	private HashSet<Seat> seats = new HashSet<Seat>();

	public Airplane(String tailNumber) {
		super();
		this.tailNumber = tailNumber;
	}	

	public Airplane(String tailNumber, HashSet<Seat> seats) {
		super();
		this.tailNumber = tailNumber;
		this.seats = seats;
	}



	public String getTailNumber() {
		return tailNumber;
	}

	public void setTailNumber(String tailNumber) {
		this.tailNumber = tailNumber;
	}

	public HashSet<Seat> getSeats() {
		return seats;
	}

	public void setSeats(HashSet<Seat> seats) {
		this.seats = seats;
	}
	
	

}
