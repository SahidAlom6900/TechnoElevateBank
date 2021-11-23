package com.technoelevate.springboot.service;

import com.technoelevate.springboot.entity.Customer;
import com.technoelevate.springboot.message.Message;

public interface CustomerService {

//	Message findByUserName(String userName, String password);

	Customer findByUserName(String userName);

	Message deposite(double amount);

	Message withdraw(double amount);

	Message getBalance();

	void fialedAttempt(Customer customer);
	
	void accountLock(Customer customer);
}
