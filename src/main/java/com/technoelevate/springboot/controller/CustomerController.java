package com.technoelevate.springboot.controller;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.technoelevate.springboot.entity.Customer;
import com.technoelevate.springboot.response.ResponseMessage;
import com.technoelevate.springboot.security.JWTConfig;
import com.technoelevate.springboot.service.CustomerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/customer")
@Api(value = "/api/v1/customer", tags = "Bank Application")
public class CustomerController {
	@Autowired
	private CustomerService customerService;
	@Autowired
	private JWTConfig config;

	@PutMapping(path = "/withdraw/{amount}")
	@ApiOperation(value = "Withdraw Money ", notes = "Withdraw Money ", tags = "Bank Application")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Withdraw Successfully"),
			@ApiResponse(code = 404, message = "Invalid Customer Id"),
			@ApiResponse(code = 403, message = "Access Denied") })
	public ResponseEntity<ResponseMessage> withdraw(@PathVariable("amount") double amount) {
		log.info(amount + "Amount Successfully Withdraw  ");
		return new ResponseEntity<ResponseMessage>(customerService.withdraw(amount), HttpStatus.OK);
	}

	@PutMapping(path = "/deposite/{amount}")
	@ApiOperation(value = "Deposite Money ", notes = "Deposite Money ", tags = "Bank Application")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Deposite Successfully"),
			@ApiResponse(code = 404, message = "Invalid Customer Id"),
			@ApiResponse(code = 403, message = "Access Denied") })
	public ResponseEntity<ResponseMessage> deposite(@PathVariable("amount") double amount) {
		log.info(amount + "Amount Successfully Deposited  ");
		return new ResponseEntity<ResponseMessage>(customerService.deposite(amount), HttpStatus.OK);
	}

	@GetMapping("/balance")
	@ApiOperation(value = "Total Money ", notes = "Total Money ", tags = "Bank Application")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Deposite Successfully"),
			@ApiResponse(code = 404, message = "Invalid Customer Id"),
			@ApiResponse(code = 403, message = "Access Denied") })   
	public ResponseEntity<ResponseMessage> getBalance() {
		return new ResponseEntity<ResponseMessage>(customerService.getBalance(), HttpStatus.OK);
	}

	@GetMapping("/token/refresh")
	@ApiOperation(value = "Generate new Access token ", notes = "Generate new Access token ", tags = "Bank Application")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Generate new Access token Successfully"),
			@ApiResponse(code = 404, message = "Refresh token is missing"),
			@ApiResponse(code = 403, message = "Access Denied") })
	public void refreashToken(HttpServletRequest request, HttpServletResponse response)
			throws JsonGenerationException, JsonMappingException, IOException {
		String header = request.getHeader(AUTHORIZATION);
		if (header != null && header.startsWith("Bearer ")) {
			try {
				String refresh_token = header.substring("Bearer ".length());
				Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
				JWTVerifier verifier = JWT.require(algorithm).build();
				DecodedJWT decodedJWT = verifier.verify(refresh_token);
				String username = decodedJWT.getSubject();
				Customer customer = customerService.findByUserName(username);
				Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
				authorities.add(new SimpleGrantedAuthority(customer.getRoles()));
				String access_token = JWT.create().withSubject(customer.getUserName())
						.withExpiresAt(new Date(System.currentTimeMillis() + config.getAccess_token()))
						.withIssuer(request.getRequestURI().toString())
						.withClaim("roles",
								authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
						.sign(algorithm);
				LinkedHashMap<String, Object> infomation = new LinkedHashMap<>();
				LinkedHashMap<String, Object> tokens = new LinkedHashMap<>();
				infomation.put("error", false);
				infomation.put("message", "Successfully Generate Access token");
				tokens.put("access_token", access_token);
				tokens.put("refresh_token", refresh_token);
				infomation.put("data", tokens);
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				new ObjectMapper().writeValue(response.getOutputStream(), infomation);
			} catch (Exception exception) {
				response.setHeader("error", exception.getMessage());
				response.setStatus(FORBIDDEN.value());
				HashMap<String, String> error = new HashMap<>();
				error.put("error", exception.getMessage());
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				new ObjectMapper().writeValue(response.getOutputStream(), error);
			}
		} else
			throw new RuntimeException("Refresh token is missing");

	}

}
