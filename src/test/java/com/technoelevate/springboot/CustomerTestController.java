package com.technoelevate.springboot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.technoelevate.springboot.controller.CustomerController;
import com.technoelevate.springboot.entity.Customer;
import com.technoelevate.springboot.response.ResponseMessage;
import com.technoelevate.springboot.service.CustomerService;
@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CustomerTestController {
	@InjectMocks
	private CustomerController customerController;
	@Mock
	private CustomerService customerService;
	private MockMvc mockMvc;
	private ObjectMapper mapper;

	@BeforeEach
	public void setup() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
		this.mapper = new ObjectMapper();
	}

	@SuppressWarnings({ "unchecked"})
	@Test
	public void testDeposite() throws Exception {
		Customer customer = new Customer("Sahid", "sahid@123");
		ResponseMessage message = new ResponseMessage();
		message.setData(customer);
		Mockito.when(customerService.deposite(Mockito.anyDouble())).thenReturn(message);
		String jsonObject = mapper.writeValueAsString(customer);
		String result = mockMvc
				.perform(put("/api/v1/customer/deposite/" + 1000)
						.contentType(MediaType.APPLICATION_JSON).content(jsonObject))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
		System.out.println(result);
		ResponseMessage message2 = mapper.readValue(result, ResponseMessage.class);
		Map<String, String> map = (Map<String, String>) message2.getData();
		for (Map.Entry<String, String> dto1 : map.entrySet()) {
			assertEquals(customer.getUserName(), dto1.getValue());
			break;
		}
	}

	@SuppressWarnings({ "unchecked"})
	@Test
	public void testWidtdraw() throws Exception {
		Customer customer = new Customer("Sahid", "sahid@123");
		ResponseMessage message = new ResponseMessage();
		message.setData(customer);
		Mockito.when(customerService.withdraw(Mockito.anyDouble())).thenReturn(message);
		String jsonObject = mapper.writeValueAsString(customer);
		String result = mockMvc
				.perform(put("/api/v1/customer/withdraw/" + 1000)
						.contentType(MediaType.APPLICATION_JSON).content(jsonObject))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
		ResponseMessage message2 = mapper.readValue(result, ResponseMessage.class);
		Map<String, String> map = (Map<String, String>) message2.getData();
		for (Map.Entry<String, String> dto1 : map.entrySet()) {
			assertEquals(customer.getUserName(), dto1.getValue());
			break;
		}
	}

	@SuppressWarnings({ "unchecked"})
	@Test
	public void testGetBalance() throws Exception {
		Customer customer = new Customer("Sahid", "sahid@123");
		ResponseMessage message1 = new ResponseMessage();
		message1.setData(customer);
		Mockito.when(customerService.getBalance()).thenReturn(message1);
		String jsonObject = mapper.writeValueAsString(customer);
		String result = mockMvc
				.perform(get("/api/v1/customer/balance")
						.contentType(MediaType.APPLICATION_JSON).content(jsonObject))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
		ResponseMessage message2 = mapper.readValue(result, ResponseMessage.class);
		Map<String, String> map = (Map<String, String>) message2.getData();
		for (Map.Entry<String, String> customer1 : map.entrySet()) {
			assertEquals(customer.getUserName(), customer1.getValue());
			break;
		}
	}
}
