package com.metacore.address.search;

import static java.util.Arrays.asList;
import static java.util.Map.entry;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchParser {
  private List<String> properties;
  private static final Map<String, ParserOperator> operators = Map.ofEntries(
      entry(ParserOperator.EQ.getOperator(), ParserOperator.EQ),
      entry(ParserOperator.GT.getOperator(), ParserOperator.GT),
      entry(ParserOperator.GE.getOperator(), ParserOperator.GE),
      entry(ParserOperator.LT.getOperator(), ParserOperator.LT),
      entry(ParserOperator.LE.getOperator(), ParserOperator.LE),
      entry(ParserOperator.IN.getOperator(), ParserOperator.IN),
      entry(ParserOperator.NL.getOperator(), ParserOperator.NL));
  private static final String lineSeparator = "(?<!\\\\)" + Pattern.quote(";");
  private static final String valueSeparator = "(?<!\\\\)" + Pattern.quote(",");
  private static final Pattern singleLinePattern = Pattern.compile("(^[_0-9A-Za-z]*)(>=|<=|=|<|>|!|:)(.*)$");

  public void setPropertyList(List<String> properties) {
    this.properties = properties;
  }

  public List<Operation> parse(String input) throws ParseException {
    return Arrays.stream(input.split(lineSeparator)).map(it -> getOperation(it)).collect(toList());
  }

  private Operation getOperation(String input) throws RuntimeException {
    Matcher matcher = getMatcher(input);
    return new Operation(getName(matcher), getOperator(matcher), getValue(matcher));
  }

  private Matcher getMatcher(String input) {
    Matcher matcher = singleLinePattern.matcher(input);
    throwRuntimeExceptionIfMatchingFails(matcher.find(), input);
    return matcher;
  }

  private static void throwRuntimeExceptionIfMatchingFails(boolean hasPassed, String input) {
    if (!hasPassed) {
      throw new RuntimeException("Invalid search query: " + input);
    }
  }

  private String getName(Matcher matcher) {
    String name = matcher.group(1);
    throwExceptionIfInvalidPropertyName(name);
    return name;
  }

  private void throwExceptionIfInvalidPropertyName(String name) throws RuntimeException {
    if (!properties.contains(name)) {
      throw new RuntimeException("Unknown property: '" + name + "'");
    }
  }

  private ParserOperator getOperator(Matcher matcher) throws RuntimeException {
    String key = matcher.group(2);
    if (operators.containsKey(key)) {
      return operators.get(key);
    } else {
      throw new RuntimeException("Unknown operator: '" + key + "'");
    }
  }

  private List<String> getValue(Matcher matcher) {
    String[] valueArray = matcher.group(3).split(valueSeparator);
    throwExceptionIfValueMissing(valueArray);
    return asList(valueArray);
  }

  private void throwExceptionIfValueMissing(String[] valueArray) throws RuntimeException {
    if (isBlank(valueArray[0])) {
      throw new RuntimeException("Missing value");
    }
  }
}
