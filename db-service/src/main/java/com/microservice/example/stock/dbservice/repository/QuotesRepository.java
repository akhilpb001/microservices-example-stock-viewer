package com.microservice.example.stock.dbservice.repository;

import com.microservice.example.stock.dbservice.model.Quote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuotesRepository extends JpaRepository<Quote, Integer> {
  List<Quote> findByUserName(String username);
}
