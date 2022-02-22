package com.navvis.db;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;

@Entity
public class PhoneNumbers {
	@Id
	String taskID;
	@Column(name = "PHONE_NUMBER", nullable = false, length=99999)
	String phoneNumbers;

	public PhoneNumbers(String taskID, String phoneNumbers) {
		super();
		this.taskID = taskID;
		this.phoneNumbers = phoneNumbers;
	}

	public String getPhoneNumbers() {
		return phoneNumbers;
	}

	public void setPhoneNumbers(String phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

	public PhoneNumbers() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getTaskID() {
		return taskID;
	}

	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}

}
