package com.n26.CodeChallenge.model;

import lombok.Data;

@Data
public class Transaction {

  private Double amount;
  private long timestamp;
}
