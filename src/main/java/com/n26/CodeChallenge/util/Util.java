package com.n26.CodeChallenge.util;

public class Util {

  public static boolean transactionHappenedInLast60Seconds(long timeInSecond) {
    return (System.currentTimeMillis() / 1000) < (timeInSecond) + 60;
  }
}
