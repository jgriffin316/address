package com.metacore.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.metacore.demo.domain.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {

}
