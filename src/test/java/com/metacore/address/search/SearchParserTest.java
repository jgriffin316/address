package com.metacore.address.search;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class SearchParserTest {

  private SearchParser parser;

  @Before
  public void setup() {
    parser = new SearchParser();
    parser.setPropertyList(Arrays.asList("a", "b", "c", "aLongVariable23"));
  }

  @Test(expected = RuntimeException.class)
  public void parse_emptyString_expectException() throws ParseException {
    parser.parse("");
  }

  @Test(expected = RuntimeException.class)
  public void parse_invalidPropertyName_expectException() throws ParseException {
    parser.parse("x=5");
  }

  @Test(expected = RuntimeException.class)
  public void parse_invalidOperator_expectException() throws ParseException {
    parser.parse("a@5");
  }

  @Test(expected = RuntimeException.class)
  public void parse_missingOperator_expectException() throws ParseException {
    parser.parse("a5");
  }

  @Test(expected = RuntimeException.class)
  public void parse_missingValue_expectException() throws ParseException {
    parser.parse("a=");
  }

  @Test
  public void parse_criteriaWithSemicolon_expectException() throws ParseException {
    parser.parse("a=5;");
  }

  @Test
  public void parse_validSimpleExpression_expectParsList() throws ParseException {
    List<Operation> opList = parser.parse("a=5");
    assertEquals(1, opList.size());
    assertEquals("a", opList.get(0).getProperty());
    assertEquals(ParserOperator.EQ, opList.get(0).getOperator());
    assertEquals("5", opList.get(0).getValueAt(0));
  }

  @Test
  public void parse_validLongExpression_expectParsList() throws ParseException {
    List<Operation> opList = parser.parse("aLongVariable23>=2018-10-22:08:00:32.000");
    assertEquals(1, opList.size());
    assertEquals("aLongVariable23", opList.get(0).getProperty());
    assertEquals(ParserOperator.GE, opList.get(0).getOperator());
    assertEquals("2018-10-22:08:00:32.000", opList.get(0).getValueAt(0));
  }

  @Test
  public void parse_validMultipleExpression_expectParsList() throws ParseException {
    List<Operation> opList = parser.parse("a=5;b<3");
    assertEquals(2, opList.size());
    assertEquals("a", opList.get(0).getProperty());
    assertEquals(ParserOperator.EQ, opList.get(0).getOperator());
    assertEquals("5", opList.get(0).getValueAt(0));
    assertEquals("b", opList.get(1).getProperty());
    assertEquals(ParserOperator.LT, opList.get(1).getOperator());
    assertEquals("3", opList.get(1).getValueAt(0));
  }

  @Test
  public void parse_validComplexValue_expectParsList() throws ParseException {
    List<Operation> opList = parser.parse("a=5&\\t");
    assertEquals(1, opList.size());
    assertEquals("a", opList.get(0).getProperty());
    assertEquals(ParserOperator.EQ, opList.get(0).getOperator());
    assertEquals("5&\\t", opList.get(0).getValueAt(0));
  }

  @Test
  public void parse_validValueWithEscapeAtEnd_expectParsList() throws ParseException {
    List<Operation> opList = parser.parse("a=5&\\");
    assertEquals(1, opList.size());
    assertEquals("a", opList.get(0).getProperty());
    assertEquals(ParserOperator.EQ, opList.get(0).getOperator());
    assertEquals("5&\\", opList.get(0).getValueAt(0));
  }

  @Test
  public void parse_validInValue_expectParseList() throws ParseException {
    List<Operation> opList = parser.parse("a:1,2,3,4,5");
    assertEquals(1, opList.size());
    assertEquals("a", opList.get(0).getProperty());
    assertEquals(ParserOperator.IN, opList.get(0).getOperator());
    assertEquals("1", opList.get(0).getValueAt(0));
    assertEquals("2", opList.get(0).getValueAt(1));
    assertEquals("3", opList.get(0).getValueAt(2));
    assertEquals("4", opList.get(0).getValueAt(3));
    assertEquals("5", opList.get(0).getValueAt(4));
  }

  @Test
  public void parse_validInValueWithEscape_expectParseList() throws ParseException {
    List<Operation> opList = parser.parse("a:1,2,\\,3,4,5");
    assertEquals(1, opList.size());
    assertEquals("a", opList.get(0).getProperty());
    assertEquals(ParserOperator.IN, opList.get(0).getOperator());
    assertEquals("1", opList.get(0).getValueAt(0));
    assertEquals("2", opList.get(0).getValueAt(1));
    assertEquals("\\,3", opList.get(0).getValueAt(2));
    assertEquals("4", opList.get(0).getValueAt(3));
    assertEquals("5", opList.get(0).getValueAt(4));
  }

  @Test
  public void parse_validInValueWithEscapeAtEnd_expectParseList() throws ParseException {
    List<Operation> opList = parser.parse("a:1,2,3,4,5\\");
    assertEquals(1, opList.size());
    assertEquals("a", opList.get(0).getProperty());
    assertEquals(ParserOperator.IN, opList.get(0).getOperator());
    assertEquals("1", opList.get(0).getValueAt(0));
    assertEquals("2", opList.get(0).getValueAt(1));
    assertEquals("3", opList.get(0).getValueAt(2));
    assertEquals("4", opList.get(0).getValueAt(3));
    assertEquals("5\\", opList.get(0).getValueAt(4));
  }
}
