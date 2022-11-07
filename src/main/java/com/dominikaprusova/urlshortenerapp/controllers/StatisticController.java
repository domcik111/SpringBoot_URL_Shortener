package com.dominikaprusova.urlshortenerapp.controllers;


import com.dominikaprusova.urlshortenerapp.entities.Account;
import com.dominikaprusova.urlshortenerapp.entities.Register;
import com.dominikaprusova.urlshortenerapp.repositories.AccountRepository;
import com.dominikaprusova.urlshortenerapp.repositories.RegisterRepository;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/statistics")
public class StatisticController {

    @Autowired
    private RegisterRepository registerRepository;

    @Autowired
    private AccountRepository accountRepository;


    // a method for obtaining statistics for a given account,
    // and there is also authentication
    @GetMapping("/{id}")
    public ResponseEntity getStatById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authentication,
                                      @PathVariable("id") final String id) {
        final boolean isAuthenticated = performAuthentication(authentication);
        if (isAuthenticated) {
            final Iterable<Register> registers = registerRepository.findAll();
            final List<Register> accountUrls = new ArrayList<>();
            for (final Register reg : registers) {
                if (reg.getAccountId().equals(id)) {
                    accountUrls.add(reg);
                }
            }
            return new ResponseEntity(createResponse(accountUrls), HttpStatus.ACCEPTED);
        }
        return ResponseEntity.badRequest().build();
    }

    private JSONObject createResponse(final List<Register> registers) {
        JSONObject resp = new JSONObject();
        for (final Register register : registers) {
            resp.put(register.getUrl(), register.getCalls());
        }
        return resp;
    }

    // method for user authentication
    private boolean performAuthentication(String authentication) {
        final Iterable<Account> accounts = accountRepository.findAll();
        for (final Account acc : accounts) {
            final HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(acc.getAccountId(), acc.getPassword());
            final HttpEntity request = new HttpEntity(headers);
            String header = Objects.requireNonNull(request.getHeaders().get(HttpHeaders.AUTHORIZATION)).get(0);
            if (authentication.equals(header)) {
                System.out.println("Credentials are valid!");
                return true;

            }
        }
        System.out.println("Credentials are invalid!");
        return false;
    }

}
