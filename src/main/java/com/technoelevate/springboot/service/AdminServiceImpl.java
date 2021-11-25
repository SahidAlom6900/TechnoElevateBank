package com.technoelevate.springboot.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.technoelevate.springboot.entity.Customer;
import com.technoelevate.springboot.entity.CustomerDto;
import com.technoelevate.springboot.repository.CustomerRepository;
import com.technoelevate.springboot.response.ResponseMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {
	@Autowired
	private CustomerRepository repository;

	@Override
	public ResponseMessage getAllCustomer() {
		List<Customer> customers = repository.findAll();
		List<CustomerDto> dto = new ArrayList<>();
		for (int i = 0; i < customers.size(); i++) {
			Customer customer = customers.get(i);
			if (customer.getRoles().equals("USER"))
				dto.add(new CustomerDto(customer.getUserName(), customer.getAccountNo()));
		}
		log.info("Successfully Fatched  ");
		return new ResponseMessage(HttpStatus.OK.value(), new Date(), false, "Successfully Fatched  ", dto);
	}

}
