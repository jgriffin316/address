package com.metacore.address.search;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;

import com.metacore.address.domain.Address;

public class SearchService {
  @Autowired
  SessionFactory sessionFactory;

  public List<Address> search(List<Operation> criteria) {
    return new ArrayList<>();
  }

  // also need: start, limit, order by

  List<Address> buildCriteria(List<Operation> criteria) throws Exception {
    Session session = sessionFactory.openSession();

    CriteriaBuilder builder = session.getCriteriaBuilder();

    for (Operation op : criteria) {
      switch (op.getOperator()) {
      case EQ:
      case GT:
      case GE:
      case LT:
      case LE:
      case NL:
      case IN:
      default:
        throw new Exception("Undefined ParserOperator");
      }
    }
    // builder.greaterThan(x, y)
    CriteriaQuery<Address> query = builder.createQuery(Address.class);
    Root<Address> root = query.from(Address.class);

    query.select(root).where(builder.equal(root.get("id"), ""));
    Query<Address> q = session.createQuery(query);
    return q.getResultList();
  }
}
