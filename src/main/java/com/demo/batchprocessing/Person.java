package com.demo.batchprocessing;

public class Person {

	private String firstName;
	private String lastName;

	public Person() {
		// TODO Auto-generated constructor stub
	}

	public Person(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
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

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "firstName <" + firstName + "> " + "lastName <" + lastName + ">";
	}
}
