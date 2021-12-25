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
	public Customer(int passportNum, String firstName, String lastName, String email, String phoneNum) {
		super();
		this.passportNum = passportNum;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNum = phoneNum;
	}
	
	
	
	

}
