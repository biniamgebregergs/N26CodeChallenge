package com.n26.CodeChallenge.util;

import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import com.n26.CodeChallenge.model.Transaction;


@Component
public class TransactionProcessor {

  Statistics[] transactionInSecond = new Statistics[60];

  @PostConstruct
  public void init() {
    for (int i = 0; i < transactionInSecond.length; i++) {
      transactionInSecond[i] = new Statistics();
    }
  }

  public Statistics getStatistics() {
    Statistics newTransactionBucket = new Statistics();
    for (int i = 0; i < transactionInSecond.length; i++) {
      Statistics bucketStatistics = transactionInSecond[i];
      try {
        bucketStatistics.lock();
        newTransactionBucket.add(bucketStatistics);

      } finally {
        bucketStatistics.unlock();
      }

    }
    return newTransactionBucket;
  }


  public void addTransaction(Transaction transactionInfo) {
    //second part of the timestamp
    int secondOfTimeStamp = new Long((transactionInfo.getTimestamp() / 1000) % 60).intValue(); 
    Statistics Statistics = transactionInSecond[secondOfTimeStamp];

    try {
      Statistics.lock();
      Statistics.addTransactionToStatistics(transactionInfo);
    } finally {
      Statistics.unlock();
    }
  }

}
