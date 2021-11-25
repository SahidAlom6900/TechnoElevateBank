package com.technoelevate.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.technoelevate.springboot.response.ResponseMessage;
import com.technoelevate.springboot.service.AdminService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/admin")
public class AdminController {
	@Autowired
	private AdminService adminService;

	@GetMapping(path = "/customers")
	@ApiOperation(value = "All Customers ", notes = "All Customers ", tags = "Bank Application")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Fetch Successfully"),
			@ApiResponse(code = 404, message = "No Customer Found"),
			@ApiResponse(code = 403, message = "Access Denied") })
	public ResponseEntity<ResponseMessage> getAllCustomer() {
		log.info("Fatch Successfully");
		return new ResponseEntity<ResponseMessage>(adminService.getAllCustomer(), HttpStatus.OK);
	}
}
