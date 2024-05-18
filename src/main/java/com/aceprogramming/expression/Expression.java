package com.aceprogramming.expression;

public sealed interface Expression {
    record Constant(int value) implements Expression {}
    record Addition(Expression left, Expression right) implements Expression {  }
    record Multiplication(Expression left, Expression right) implements Expression {
    }
    record Subtraction(Expression left, Expression right) implements Expression {}
    record Division(Expression left, Expression right) implements Expression {}

}
