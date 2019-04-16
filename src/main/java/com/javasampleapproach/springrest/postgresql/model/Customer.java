package com.javasampleapproach.springrest.postgresql.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "customer")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "lastname")
	private String lastname;

	@Column(name = "firstname")
	private String firstname;

	@Column(name = "age")
	private int age;

	@Column(name = "email")
	private String email;

	@Column(name = "active")
	private boolean active;

	private String activationCode;



	public Customer() {
	}

	public Customer(String lastname, String firstname, int age, String email) {
		this.lastname = lastname;
		this.firstname = firstname;
		this.age = age;
		this.email = email;
		this.active = false;
	}

	public long getId() {
		return id;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getAge() {
		return this.age;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getActivationCode() {
		return activationCode;
	}

	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", lastname=" + lastname + ", age=" + age + ", email=" + email + ", active=" + active + "]";
	}
}
