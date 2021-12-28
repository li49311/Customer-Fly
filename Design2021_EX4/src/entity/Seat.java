package entity;

public class Seat {
	
	private int rowNum;
	private String seatNum;
	private String seatClass;
	private String tailNumber;
	public Seat(int rowNum, String seatNum, String seatClass, String tailNumber) {
		super();
		this.rowNum = rowNum;
		this.seatNum = seatNum;
		this.seatClass = seatClass;
		this.tailNumber = tailNumber;
	}
	public Seat(int rowNum, String seatNum, String tailNumber) {
		super();
		this.rowNum = rowNum;
		this.seatNum = seatNum;
		this.tailNumber = tailNumber;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + rowNum;
		result = prime * result + ((seatNum == null) ? 0 : seatNum.hashCode());
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
		Seat other = (Seat) obj;
		if (rowNum != other.rowNum)
			return false;
		if (seatNum == null) {
			if (other.seatNum != null)
				return false;
		} else if (!seatNum.equals(other.seatNum))
			return false;
		return true;
	}
	public int getRowNum() {
		return rowNum;
	}
	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}
	public String getSeatNum() {
		return seatNum;
	}
	public void setSeatNum(String seatNum) {
		this.seatNum = seatNum;
	}
	public String getSeatClass() {
		return seatClass;
	}
	public void setSeatClass(String seatClass) {
		this.seatClass = seatClass;
	}
	public String getTailNumber() {
		return tailNumber;
	}
	public void setTailNumber(String tailNumber) {
		this.tailNumber = tailNumber;
	}
	@Override
	public String toString() {
		return "Seat [rowNum=" + rowNum + ", seatNum=" + seatNum + ", seatClass=" + seatClass + ", tailNumber="
				+ tailNumber + "]";
	}
	
	
	
	
}
