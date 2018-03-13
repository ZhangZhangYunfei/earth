package com.yxedu.earth.utils.function;

@FunctionalInterface
public interface QuaternaryFunc<A, B, C, D, R> {
  R apply(A first, B second, C third, D fourth);
}
