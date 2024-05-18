package com.aceprogramming.expression;

public class ExpressionEvaluator {

    public int evaluate(Expression expression) {
        return switch (expression) {
            case Expression.Constant(int value) -> value;
            case Expression.Addition(var left, var right) -> evaluate(left) + evaluate(right);
            case Expression.Multiplication(var left, var right) -> evaluate(left) * evaluate(right);
            case Expression.Division(var left, var right) -> evaluate(left) / evaluate(right);
            case Expression.Subtraction(var left, var right) -> evaluate(left) - evaluate(right);
        };
    }
}
