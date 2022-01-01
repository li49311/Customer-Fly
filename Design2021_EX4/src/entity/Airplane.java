package entity;

import java.util.ArrayList;
import java.util.HashSet;

public class Airplane {
	
	private String tailNumber;
	private HashSet<Seat> seats = new HashSet<Seat>();
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tailNumber == null) ? 0 : tailNumber.hashCode());
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
		Airplane other = (Airplane) obj;
		if (tailNumber == null) {
			if (other.tailNumber != null)
				return false;
		} else if (!tailNumber.equals(other.tailNumber))
			return false;
		return true;
	}



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
