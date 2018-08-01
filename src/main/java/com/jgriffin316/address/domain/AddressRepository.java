package com.jgriffin316.address.domain;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends CrudRepository<Address, String>, JpaSpecificationExecutor<Address> {
  @Override
  List<Address> findAll(Specification<Address> spec);
}
