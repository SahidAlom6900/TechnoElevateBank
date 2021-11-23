package com.technoelevate.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.technoelevate.springboot.entity.Customer;
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	Customer findByUserName(String userName);
	List<Customer> findAll();
	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Query(value = "update Customer c set c.failedAttempt = :failedAttempt where c.user_name = :userName" , nativeQuery = true)
	boolean fialedAttempt(@Param("failedAttempt") int failedAttempt,@Param("userName") String userName);
}
