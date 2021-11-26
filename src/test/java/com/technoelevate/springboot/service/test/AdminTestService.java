package com.technoelevate.springboot.service.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.technoelevate.springboot.entity.Admin;
import com.technoelevate.springboot.entity.Customer;
import com.technoelevate.springboot.entity.CustomerDto;
import com.technoelevate.springboot.repository.AdminRepository;
import com.technoelevate.springboot.repository.CustomerRepository;
import com.technoelevate.springboot.response.ResponseMessage;
import com.technoelevate.springboot.service.AdminServiceImpl;









@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class AdminTestService {
	@InjectMocks
	private AdminServiceImpl adminServiceImpl;
	@Mock
	private AdminRepository adminRepository;
	@Mock
	private CustomerRepository customerRepository;

	@SuppressWarnings({ "unchecked", "unused" })
	@Test
	public void testServiceReadAll() {
		Admin admin = new Admin("Rakesh", "rakesh@123");
		List<Customer> list = new ArrayList<Customer>();
		Customer customer = new Customer("Sahid", 8754854754l, "sahid@123", 10000, 0);
		customer.setRoles("USER");
		list.add(customer);
		Mockito.when(customerRepository.findAll()).thenReturn(list);
		ResponseMessage message = adminServiceImpl.getAllCustomer();
		ArrayList<CustomerDto> customer1=(ArrayList<CustomerDto>)message.getData();
		assertEquals(customer.getUserName(),customer1.get(0).getUserName());
	}

}
