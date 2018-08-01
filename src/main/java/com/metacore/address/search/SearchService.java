package com.metacore.address.search;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.stream.Stream;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.metacore.address.domain.Address;
import com.metacore.address.domain.AddressRepository;

@Component
public class SearchService {
  @Autowired
  AddressRepository addressRepository;

  public Stream<Address> search(List<Operation> operations) {
    return addressRepository.findAll(toSpecification(toCriteriaList(operations))).stream();
  }

  List<Specification<Address>> toCriteriaList(List<Operation> operations) {
    return operations.stream().map(it -> filter(it)).collect(toList());
  }

  Specification<Address> toSpecification(List<Specification<Address>> criteriaList) {
    return toSpecification(criteriaList, criteriaList.size() - 1);
  }

  Specification<Address> toSpecification(List<Specification<Address>> criteriaList, int index) {
    return index == 0 ? criteriaList.get(index) : criteriaList.get(index).and(toSpecification(criteriaList, index--));
  }

  @SuppressWarnings("serial")
  public static Specification<Address> filter(Operation op) {
    return new Specification<Address>() {

      @Override
      public Predicate toPredicate(Root<Address> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        switch (op.getOperator()) {
        case GT:
          return builder.greaterThan(root.get(op.getProperty()), op.getValueAt(0));
        case GE:
          return builder.greaterThanOrEqualTo(root.get(op.getProperty()), op.getValueAt(0));
        case LT:
          return builder.lessThan(root.get(op.getProperty()), op.getValueAt(0));
        case LE:
          return builder.lessThanOrEqualTo(root.get(op.getProperty()), op.getValueAt(0));
        case NL:
          return builder.isNotNull(root.get(op.getProperty()));
        case IN:
          return root.get(op.getProperty()).in(op.getValue());
        case EQ:
        default:
          // TODO: Make allowance for LIKE if value contains a wild card
          return builder.equal(root.get(op.getProperty()), op.getValueAt(0));
        }
      }
    };
  }

  // TODO: also need: start, limit, order by
}
