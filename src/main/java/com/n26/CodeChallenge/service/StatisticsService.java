package com.n26.CodeChallenge.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.n26.CodeChallenge.model.Transaction;
import com.n26.CodeChallenge.util.Statistics;
import com.n26.CodeChallenge.util.TransactionProcessor;

@Service
public class StatisticsService {
  private static final Logger log = LoggerFactory.getLogger(StatisticsService.class);

  @Autowired
  TransactionProcessor transactionProcessor;

  public void addTransaction(Transaction transaction) {
    log.debug("addTransaction started!");
    transactionProcessor.addTransaction(transaction);
    log.debug("addTransaction ended!");
  }

  public Statistics getStatistics() {
    log.debug("getStatistics started!");
    return transactionProcessor.getStatistics();
  }

}
