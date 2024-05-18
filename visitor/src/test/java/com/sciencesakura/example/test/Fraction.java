package com.sciencesakura.example.test;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

@Immutable
public final class Fraction {

  private final int numerator;

  private final int denominator;

  public Fraction(int numerator, int denominator) {
    if (denominator == 0) {
      throw new IllegalArgumentException("denominator must not be zero");
    }
    this.numerator = numerator;
    this.denominator = denominator;
  }

  @Nonnull
  public Fraction plus(@Nonnull Fraction other) {
    return new Fraction(
        numerator * other.denominator + other.numerator * denominator,
        denominator * other.denominator
    );
  }
}
