package com.navvis.core;

import java.io.IOException;

import com.navvis.model.GPPRequest;
import com.navvis.model.GPPResponse;
import com.navvis.model.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.annotations.Operation;
import com.navvis.core.GermanPhonesHelper;


@RequestMapping("/api")
@Slf4j
@RestController

public class GermanPhonesParser {

	private GermanPhonesHelper germanPhoneshelper;

	@Autowired
	public void setGermanPhoneshelper(GermanPhonesHelper germanPhoneshelper) {
		this.germanPhoneshelper = germanPhoneshelper;
	}

	@Operation(summary = "Post mobile numbers")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "found success", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SuccessResponse.class))) }),
			@ApiResponse(responseCode = "404", description = "No Phones found", content = @Content) })
	@PostMapping(path = "/process/file", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public GPPResponse savePhoneNumber(@RequestBody GPPRequest request) {
		String taskID = "";
		try {
			taskID = germanPhoneshelper.process(request);
		} catch (IOException e) {
			log.error("{} Exception occured {}", GermanPhonesHelper.class.getName(), e.getMessage());
			return germanPhoneshelper.mapResponse(request, false, e, taskID);
		}
		return germanPhoneshelper.mapResponse(request, true, null, taskID);

	}

	@Operation(summary = "Get mobile numbers")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "found success", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SuccessResponse.class))) }),
			@ApiResponse(responseCode = "404", description = "No Record found", content = @Content) })
	@GetMapping(path = "/process/file/{taskID}", produces = MediaType.APPLICATION_JSON_VALUE)
	
	public @ResponseBody ResponseEntity<String> getPhoneNumbers(@PathVariable String taskID) {
		try {
			return ResponseEntity.status(HttpStatus.OK)
					.body(germanPhoneshelper.getPhonesViaTaskID(taskID));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("No record Exists"+e.getMessage());
		}

	}
	

	@Operation(summary = "Delete mobile numbers")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "record delete success", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SuccessResponse.class))) }),
			@ApiResponse(responseCode = "404", description = "No Record found", content = @Content) })
	@DeleteMapping(path = "/process/file/{taskID}", produces = MediaType.APPLICATION_JSON_VALUE)
	
	public @ResponseBody ResponseEntity<String> deletePhoneNumbers(@PathVariable String taskID) {
		try {
			return ResponseEntity.status(HttpStatus.OK)
					.body(germanPhoneshelper.deletePhonesViaTaskID(taskID));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("No record Exists"+e.getMessage());
		}

	}
	@Operation(summary = "Get All Task IDs")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "found success", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SuccessResponse.class))) }),
			@ApiResponse(responseCode = "404", description = "No Record found", content = @Content) })
	@GetMapping(path = "/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
	
	public @ResponseBody ResponseEntity<String> getAllTaskIDs() {
		try {
			return ResponseEntity.status(HttpStatus.OK)
					.body(germanPhoneshelper.getAllTaskIDs());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("No TaskIDs found"+e.getMessage());
		}

	}
}
