package com.jgriffin316.address.search;

public enum ParserOperator {
  /*
   * =  -> equal to > -> greater than 
   * >= -> greater than or equal to 
   * <  -> less than 
   * <= -> less than or equal to 
   * !  -> null value
   * :  -> in 
   * ,  -> in value separator 
   * ;  -> argument separator 
   * \  -> escape next character
   */
  EQ("="), GT(">"), GE(">="), LT("<"), LE("<="), IN(":"), NL("!");

  private String operator;

  ParserOperator(String op) {
    operator = op;
  }

  public String getOperator() {
    return operator;
  }
}
