package com.n26.CodeChallenge.util;

import java.util.concurrent.locks.ReentrantLock;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.n26.CodeChallenge.model.Transaction;
import lombok.Data;

@JsonIgnoreProperties({"holdCount", "heldByCurrentThread", "locked", "fair", "queueLength"})
@JsonPropertyOrder({"sum", "avg", "max", "min", "count"})
@Data
public class Statistics extends ReentrantLock {

  double sum;
  double avg;
  double min = Double.MAX_VALUE;
  double max = Double.MIN_VALUE;
  long count;
  @JsonIgnore
  long indexOfBucketInSecond;

  public Statistics() {}

  /*@Override
  public String toString() {
    return "Statistics [sum=" + sum + ", min=" + min + ", max=" + max + ", count=" + count
        + ", indexOfBucketInSecond=" + indexOfBucketInSecond + "]";
  }
*/
  public void addTransactionToStatistics(Transaction transaction) {
    long transactionExecutionInSecond = transaction.getTimestamp() / 1000;
    if (Util.transactionHappenedInLast60Seconds(transactionExecutionInSecond)) {
      if (indexOfBucketInSecond != transactionExecutionInSecond) {
        clearPacketData(transactionExecutionInSecond);
      }
      Double amount = transaction.getAmount();
      sum += amount;
      if (amount < min)
        min = amount;
      if (amount > max)
        max = amount;
      count++;
      avg = sum / count;
    }
  }

  /*
   * Clear statistic parameters
   */
  private void clearPacketData(long transactionExecutionInSecond) {
    sum = 0;
    min = Double.MAX_VALUE;
    max = Double.MIN_VALUE;
    count = 0;
    avg = 0;
    indexOfBucketInSecond = transactionExecutionInSecond;
  }

  /*
   * For in time transactions populate the parameters
   */
  public void add(Statistics statistics) {
    if (Util.transactionHappenedInLast60Seconds(statistics.getIndexOfBucketInSecond())) {
      sum += statistics.getSum();
      min = (min > statistics.getMin() ? statistics.getMin() : min);
      max = (max < statistics.getMax() ? statistics.getMax() : max);
      count += statistics.getCount();
      avg = sum / count;
    }
  }


}
