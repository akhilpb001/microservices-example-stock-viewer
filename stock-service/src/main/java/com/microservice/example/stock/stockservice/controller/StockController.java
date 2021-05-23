package com.microservice.example.stock.stockservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/stock")
public class StockController {

  @Autowired
  RestTemplate restTemplate;

  @GetMapping("/{username}")
  public List<Quote> getStocks(@PathVariable("username") final String username) {
    final String dbServiceUrl = "http://db-service:8081/api/v1/db/" + username;

    ResponseEntity<List<String>> quoteResponse = restTemplate.exchange(dbServiceUrl,
        HttpMethod.GET,
        null,
        new ParameterizedTypeReference<List<String>>() {
        });

    List<String> quotes = quoteResponse.getBody();

    return quotes.stream()
        .map(quote -> {
          Stock stock = getStockFromYahooFinance(quote);
          return new Quote(quote, stock.getQuote().getPrice());
        })
        .collect(Collectors.toList());
  }

  private Stock getStockFromYahooFinance(String quote) {
    try {
      return YahooFinance.get(quote);
    } catch (IOException e) {
      e.printStackTrace();
      return new Stock(quote);
    }
  }

  private class Quote {
    private String quote;
    private BigDecimal price;

    public Quote(String quote, BigDecimal price) {
      this.quote = quote;
      this.price = price;
    }

    public String getQuote() {
      return quote;
    }

    public void setQuote(String quote) {
      this.quote = quote;
    }

    public BigDecimal getPrice() {
      return price;
    }

    public void setPrice(BigDecimal price) {
      this.price = price;
    }
  }
}
