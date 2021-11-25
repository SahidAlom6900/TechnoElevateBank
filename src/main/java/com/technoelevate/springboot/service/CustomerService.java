package com.technoelevate.springboot.service;

import com.technoelevate.springboot.entity.Customer;
import com.technoelevate.springboot.response.ResponseMessage;

public interface CustomerService {

//	Message findByUserName(String userName, String password);

	Customer findByUserName(String userName);

	ResponseMessage deposite(double amount);

	ResponseMessage withdraw(double amount);

	ResponseMessage getBalance();

	void fialedAttempt(Customer customer);
	
	void accountLock(Customer customer);
}
