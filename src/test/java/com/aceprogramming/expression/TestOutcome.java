package com.aceprogramming.expression;

import java.util.function.Supplier;

sealed interface TestOutcome {
  record Success(int value) implements TestOutcome {}
  record Failure(Supplier<? extends RuntimeException> exceptionSupplier) implements TestOutcome {}
}
