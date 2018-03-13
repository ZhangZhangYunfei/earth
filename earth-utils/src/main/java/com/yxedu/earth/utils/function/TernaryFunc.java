package com.yxedu.earth.utils.function;

@FunctionalInterface
public interface TernaryFunc<A, B, C, R> {
  R apply(A first, B second, C third);
}
