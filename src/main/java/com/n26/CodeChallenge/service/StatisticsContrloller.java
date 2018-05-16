package com.n26.CodeChallenge.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.n26.CodeChallenge.model.Transaction;
import com.n26.CodeChallenge.util.Statistics;

@RestController
public class StatisticsContrloller {
  @Autowired
  StatisticsService statisticsService;

  @PostMapping(value = "/transactions")
  @ResponseStatus(HttpStatus.CREATED)
  public @ResponseBody void createTransaction(@RequestBody Transaction transaction) {
    statisticsService.addTransaction(transaction);
  }

  @GetMapping(value = "/statistics")
  public Statistics getStatistic() {
    return statisticsService.getStatistics();
  }

}
