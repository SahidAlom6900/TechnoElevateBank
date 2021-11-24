package com.technoelevate.springboot.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.technoelevate.springboot.entity.Admin;
import com.technoelevate.springboot.entity.BalanceDetails;
import com.technoelevate.springboot.entity.Customer;
import com.technoelevate.springboot.exception.CustomerException;
import com.technoelevate.springboot.message.Message;
import com.technoelevate.springboot.repository.AdminRepository;
import com.technoelevate.springboot.repository.BalanceDetailsRepo;
import com.technoelevate.springboot.repository.CustomerRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService, UserDetailsService {
	@Autowired
	private CustomerRepository repository;
	@Autowired
	private BalanceDetailsRepo balanceRepo;
	@Autowired
	private AdminRepository adminRepository;
	private static Customer customer;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		if (!username.equals("ADMIN")) {
			customer = repository.findByUserName(username);
			System.out.println(customer);
			if (customer == null) {
				log.error("Please Enter your Correct User Name");
			} else {
				authorities.add(new SimpleGrantedAuthority("USER"));
				return new User(customer.getUserName(), customer.getPassword(), authorities);
			}
		} else {
			Admin admin = adminRepository.findByUserName(username);
			if (admin == null) {
				log.error("Please Enter your Correct User Name");
			} else {
				authorities.add(new SimpleGrantedAuthority("ADMIN"));
				return new User(admin.getUserName(), admin.getPassword(), authorities);
			}
		}
		return null;
	}

	@Value("${deposite.tax}")
	double deposite_tax;

	@Override
	public Message deposite(double amount) {
		if (customer == null || customer.getUserName() == null) {
			throw new CustomerException("Please Login First!!!");
		}
		double avialablewAmount = (double) Math.round((customer.getBalance() + amount * (1 - deposite_tax)) * 1000.0)
				/ 1000.0;
		if (amount % 100 == 0 && amount > 0) {
			customer.setBalance(avialablewAmount);
			this.repository.save(customer);
			this.balanceRepo.save(new BalanceDetails(0,amount, new Date(), avialablewAmount, customer));
			System.out.println(this.repository);
			Customer customer2 = (Customer) this.repository.findByUserName(customer.getUserName());
			return new Message(HttpStatus.OK.value(), new Date(), false, amount + " Amount Successfully Deposited  ",
					customer2);
		}
		log.error("The Amount Should be Multiple of 100");
		throw new CustomerException("The Amount Should be Multiple of 100");
	}

	@Value("${withdraw.tax}")
	double withdraw_tax;

	@Override
	public Message withdraw(double amount) {
		double avialablewAmount = (double) Math.round((customer.getBalance() - amount * (1 + withdraw_tax)) * 1000.0)
				/ 1000.0;
		if (amount % 100 != 0) {
			log.error("The Amount Should be Multiple of 100");
			throw new CustomerException("The Amount Should be Multiple of 100");
		}
		if (avialablewAmount > 0 && customer.getBalance() > 500) {
			if (customer.getCount() < 3) {
				customer.setBalance(avialablewAmount);
				customer.setCount(customer.getCount() + 1);
				repository.save(customer);
				balanceRepo.save(new BalanceDetails(amount,0, new Date(), avialablewAmount, customer));
				Customer customer2 = (Customer) repository.findByUserName(customer.getUserName());
				return new Message(HttpStatus.OK.value(), new Date(), false, amount + " Amount Successfully Withdrawn ",
						customer2);
			}
			log.error(" Only 3 times Can be withdrawn in a month!!!");
			throw new CustomerException(" Only 3 times Can be withdrawn in a month!!!");
		}
		log.error("Insufficient Balance!!!");
		throw new CustomerException("Insufficient Balance!!!");
	}

	@Override
	public Message getBalance() {
		if (customer == null || customer.getUserName() == null) {
			log.error("Please Login First!!!");
			throw new CustomerException("Please Login First!!!");
		}
		Customer customer2 = (Customer) repository.findByUserName(customer.getUserName());
		log.error( "Your Balance is : " + customer2.getBalance());
		return new Message(HttpStatus.OK.value(), new Date(), false, "Your Balance is : " + customer2.getBalance(),
				customer2);
	}

	@Override
	public Customer findByUserName(String userName) {
		customer = (Customer) repository.findByUserName(userName);
		if (customer == null || customer.getUserName() == null) {
			log.error("User Name Does Not Exist!!!");
		}
		log.info("Successfully Logged in " + userName);
		return customer;
	}

	public Customer getCustomer() {
		return customer;
	}

	@Override
	public void fialedAttempt(Customer customer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void accountLock(Customer customer) {
		// TODO Auto-generated method stub
		
	}
}
