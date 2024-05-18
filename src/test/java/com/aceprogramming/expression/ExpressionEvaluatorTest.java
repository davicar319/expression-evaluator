package com.aceprogramming.expression;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.aceprogramming.expression.Expression.*;
import static com.aceprogramming.expression.TestOutcome.*;

class ExpressionEvaluatorTest {
  private final ExpressionEvaluator evaluator = new ExpressionEvaluator();

  static Stream<Arguments> testCasesForEvaluate() {
    return Stream.of(
        Arguments.of("Evaluating a constant", new Constant(5), 5),
        Arguments.of("Adding two constants", new Addition(new Constant(3),
            new Constant(2)), 5),
        Arguments.of("Subtracting two constants", new Subtraction(new Constant(10),
            new Constant(4)), 6),
        Arguments.of("Multiplying by zero", new Multiplication(new Constant(6),
            new Constant(0)), 0),
        Arguments.of("Dividing by a non-zero constant", new Division(new Constant(8),
            new Constant(2)), 4),
        Arguments.of("Dividing by zero - Expecting ArithmeticException",
            new Division(new Constant(5), new Constant(0)), ArithmeticException.class)
    );
  }

  @ParameterizedTest(name = "[{index}] {0}")
  @MethodSource("testCasesForEvaluate")
  void testEvaluate(String description, Expression expression, Object expectedOutcome) { // Changes here
    if (expectedOutcome instanceof Class<?>) {
      @SuppressWarnings("unchecked")
      Class<? extends Exception> expectedException = (Class<? extends Exception>) expectedOutcome;
      Assertions.assertThrows(expectedException, () -> evaluator.evaluate(expression));
    } else {
      int expectedResult = (int) expectedOutcome;
      Assertions.assertEquals(expectedResult, evaluator.evaluate(expression));
    }
  }

  private static Stream<TestCase> provideExpressionsForEvaluate() {
    return Stream.of(
        new TestCase("Valid constant", new Constant(5), new Success(5)),
        new TestCase("Valid addition", new Addition(new Constant(2), new Constant(3)), new Success(5)),
        new TestCase("Division by zero", new Division(new Constant(6), new Constant(0)), new Failure(ArithmeticException::new)),
        new TestCase("Valid subtraction", new Subtraction(new Constant(10), new Constant(3)), new Success(7)),
        new TestCase("Valid multiplication", new Multiplication(new Constant(3), new Constant(4)), new Success(12)),
        new TestCase("Negative result", new Subtraction(new Constant(5), new Constant(8)), new Success(-3)),
        new TestCase("Combined operations",
            new Addition(new Constant(3), new Multiplication(new Constant(2), new Constant(5))),
            new Success(13)),

        new TestCase("Nested division by zero",
            new Division(new Constant(12), new Division(new Constant(6), new Constant(0))),
            new Failure(ArithmeticException::new))
    );
  }

  @ParameterizedTest
  @MethodSource("provideExpressionsForEvaluate")
  void testEvaluateExpression(TestCase testCase) {
    ExpressionEvaluator evaluator = new ExpressionEvaluator();

    switch (testCase.expectedOutcome()) {
      case Success(int expectedResult) ->
          Assertions.assertEquals(expectedResult, evaluator.evaluate(testCase.expression()),
              "Incorrect evaluation for: " + testCase.description());
      case Failure(Supplier<? extends RuntimeException> exceptionSupplier) ->
          Assertions.assertThrows(exceptionSupplier.get().getClass(),
              () -> evaluator.evaluate(testCase.expression()),
              "Incorrect error handling for: " + testCase.description());
    }
  }
}