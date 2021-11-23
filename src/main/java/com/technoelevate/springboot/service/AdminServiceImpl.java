package com.technoelevate.springboot.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.technoelevate.springboot.entity.Customer;
import com.technoelevate.springboot.entity.CustomerDto;
import com.technoelevate.springboot.message.Message;
import com.technoelevate.springboot.repository.CustomerRepository;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class AdminServiceImpl implements AdminService {
	@Autowired
	private CustomerRepository repository;

	@Override
	public Message getAllCustomer() {
		List<Customer> customers = repository.findAll();
		List<CustomerDto> dto = new ArrayList<>();
		for (int i = 0; i < customers.size(); i++) {
			dto.add(new CustomerDto(customers.get(i).getUserName(),customers.get(i).getAccountNo()));
		}
		log.info("Successfully Fatched  ");
		return new Message(HttpStatus.OK.value(),new Date(),false, "Successfully Fatched  ", dto);
	}

}
