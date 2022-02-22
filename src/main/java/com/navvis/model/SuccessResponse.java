package com.navvis.model;

public class SuccessResponse implements GPPResponse{
	public SuccessResponse(String taskID) {
		super();
		this.taskID = taskID;
	}

	private String taskID;

	public String getTaskID() {
		return taskID;
	}

	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}
}