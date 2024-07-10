package com.eventmanagement.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eventmanagement.entity.Address;

@Repository
public interface AddressDao extends JpaRepository<Address, Integer> {

}
