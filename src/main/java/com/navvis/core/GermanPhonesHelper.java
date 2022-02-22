package com.navvis.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.xml.ws.http.HTTPException;

import com.navvis.config.Constant;
import com.navvis.db.PhoneNumbers;
import com.navvis.model.FailureResponse;
import com.navvis.model.GPPRequest;
import com.navvis.model.GPPResponse;
import com.navvis.model.SuccessResponse;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import com.google.gson.Gson;
import com.navvis.db.PhoneNumberRepository;


@Service
public class GermanPhonesHelper {

	PhoneNumberRepository phoneNumberRepository;
	
	private List<String> phoneNumbers;

	public PhoneNumberRepository getPhoneNumberRepository() {
		return phoneNumberRepository;
	}

	@Autowired
	public void setPhoneNumberRepository(PhoneNumberRepository phoneNumberRepository) {
		this.phoneNumberRepository = phoneNumberRepository;
	}

	public String process(GPPRequest request) throws IOException {
		if (request == null || request.getSourceFile() == null) {
			throw new RuntimeException("File cannot be null");
		}
		if(phoneNumbers== null) {
			phoneNumbers = new LinkedList<>();
		}
		String taskID = getTaskID(request.getSourceFile());
		try (BufferedReader br = new BufferedReader(new FileReader(new File(request.getSourceFile())))) {
			StringBuilder listPhoneNumbers = new StringBuilder();
			for (String phoneNumber; (phoneNumber = br.readLine()) != null;) {
				// process the line.
				phoneNumber = phoneNumber.replaceAll("\\s", "");
				if (isValid(phoneNumber)) {
					phoneNumbers.add(phoneNumber);  
					listPhoneNumbers.append(phoneNumber).append(" ");
				}
			}
			PhoneNumbers pNumber = new PhoneNumbers(taskID, listPhoneNumbers.toString());
			phoneNumberRepository.save(pNumber);
		}

		return taskID;

	}

	private String getTaskID(String sourceFile) throws IOException  {
		try (InputStream is = Files.newInputStream(Paths.get(sourceFile))) {
            return DigestUtils.sha512Hex(is).toString();
        } 
	}

	private boolean isValid(String phoneNumber ) {
		if(!phoneNumber.startsWith("+49") && !phoneNumber.startsWith("0049")) 
			return false;
		if(phoneNumber.startsWith("+49")) {
			if(phoneNumber.length()!=14 || !phoneNumber.matches("\\+[0-9]+"))
				return false;
		}else {
			if(phoneNumber.length()!=15 || !phoneNumber.matches("[0-9]+"))
				return false;
		}
		if(phoneNumbers.stream().filter(s->s.contains(phoneNumber)).findAny().isPresent())
			return false;
		return true;
	}

	public GPPResponse mapResponse(GPPRequest request, boolean isSuccess, Exception e, String taskID) {
		if (isSuccess)
			return new SuccessResponse(taskID);
		else
			return new FailureResponse(e.getMessage(), Constant.HTTP_400);

	}

	public String getPhonesViaTaskID(String taskID) {
		Optional<PhoneNumbers> record = phoneNumberRepository.findById(taskID);
		if (record.isPresent()) {
			return new Gson().toJson(record.get().getPhoneNumbers());
		}
		throw new RuntimeException("No record found in the database");
	}

	public String getAllTaskIDs() {
		List<String> taskIDs = new ArrayList<>(); 
		phoneNumberRepository.findAll().forEach(s->taskIDs.add(s.getTaskID()));
		return new Gson().toJson(taskIDs);
	}

	public String deletePhonesViaTaskID(String taskID) {
		Optional<PhoneNumbers> record = phoneNumberRepository.findById(taskID);
		if(record.isPresent()) {
			
		for(String str:record.get().getPhoneNumbers().split(" ")) {
			if(str.length()>0 && !str.equals(" ")) {
				phoneNumbers.remove(str);
			}
		}
		phoneNumberRepository.deleteById(taskID);
		}
		return record.get().getPhoneNumbers();
	}

}
