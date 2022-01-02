package entity;

import java.sql.Date;

public class Customer {
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + passportNum;
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
		Customer other = (Customer) obj;
		if (passportNum != other.passportNum)
			return false;
		return true;
	}
	private int passportNum;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNum;
	private String primaryCitizenship;
	private Date birthDate;
	
	
	public Customer(int passportNum, String firstName, String lastName, String email) {
		super();
		this.passportNum = passportNum;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}
	public int getPassportNum() {
		return passportNum;
	}
	public void setPassportNum(int passportNum) {
		this.passportNum = passportNum;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	@Override
	public String toString() {
		return firstName + " " +  lastName;
	}
}
