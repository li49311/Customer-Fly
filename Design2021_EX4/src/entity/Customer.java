package entity;

import java.sql.Date;

public class Customer {
	
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
		return "firstName=" + firstName + ", lastName=" + lastName;
	}
}
