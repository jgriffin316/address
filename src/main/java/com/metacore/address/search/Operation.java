package com.metacore.address.search;

import java.util.List;

public class Operation {
  private String property;
  private ParserOperator operator;
  private List<String> value;

  public Operation(String property, ParserOperator operator, List<String> value) {
    this.property = property;
    this.operator = operator;
    this.value = value;
  }

  public String getProperty() {
    return property;
  }

  public void setProperty(String property) {
    this.property = property;
  }

  public ParserOperator getOperator() {
    return operator;
  }

  public void setOperator(ParserOperator operator) {
    this.operator = operator;
  }

  public String getValueAt(int index) {
    return value.get(index);
  }

  public void addValue(String value) {
    this.value.add(value);
  }
}
