package com.microservice.example.stock.dbservice.controller;

import com.microservice.example.stock.dbservice.model.Quote;
import com.microservice.example.stock.dbservice.model.Quotes;
import com.microservice.example.stock.dbservice.repository.QuotesRepository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/db")
public class DbServiceController {

  private QuotesRepository quotesRepository;

  public DbServiceController(QuotesRepository quotesRepository) {
    this.quotesRepository = quotesRepository;
  }

  @GetMapping("/{username}")
  public List<String> getQuotes(@PathVariable("username") final String username) {
    return getQuotesByUserName(username);
  }

  @PostMapping("/add")
  public List<String> addQuote(@RequestBody final Quotes quotes) {
    quotes.getQuotes()
        .stream()
        .map(quote -> new Quote(quotes.getUserName(), quote))
        .forEach(quote -> quotesRepository.save(quote));

    return getQuotesByUserName(quotes.getUserName());
  }

  @DeleteMapping("/delete/{username}")
  public List<String> deleteQuotes(@PathVariable("username") final String username) {
    List<Quote> quotes = quotesRepository.findByUserName(username);
    quotesRepository.deleteAll(quotes);

    return getQuotesByUserName(username);
  }

  private List<String> getQuotesByUserName(String username) {
    return quotesRepository.findByUserName(username)
        .stream()
        .map(Quote::getQuote)
        .collect(Collectors.toList());
  }
}
