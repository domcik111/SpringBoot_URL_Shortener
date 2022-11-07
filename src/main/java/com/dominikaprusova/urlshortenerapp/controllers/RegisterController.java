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

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

@RestController
@RequestMapping(value = "/register")
public class RegisterController {


    @Autowired
    private RegisterRepository registerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping
    public Iterable<Register> getAllUrls() {
        return registerRepository.findAll();
    }

    // method for url registration, if the user is authenticated,
    // a short version of the url is created and added to the db for the given url
    @PostMapping
    public ResponseEntity registerURL(@RequestHeader(HttpHeaders.AUTHORIZATION) String authentication,
                                      @RequestBody final Register register) {
        if (register != null) {
            final boolean isAuthenticated = performAuthentication(authentication);
            if (isAuthenticated) {
                if (register.getRedirectType() == null || register.getRedirectType() == 0) {
                    register.setRedirectType(302);
                }
                final String shortUrl = shortenUrl();
                register.setShortUrl(shortUrl);
                register.setCalls(0);
                register.setAccountId(getAccountId(authentication));
                registerRepository.save(register);
                return new ResponseEntity(createResponse(register.getShortUrl()), HttpStatus.CREATED);
            }
        }
        return ResponseEntity.badRequest().build();
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

    private JSONObject createResponse(final String url) {
        JSONObject resp = new JSONObject();
        resp.put("shortUrl", url);
        return resp;
    }

    private String shortenUrl() {
        return String.format("%s%s", "http://short.com/", AccountController.createHash(6));
    }

    private String getAccountId(final String authentication) {
        final String base64Credentials = authentication.substring("Basic".length()).trim();
        final byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
        final String credentials = new String(credDecoded, StandardCharsets.UTF_8);
        final String[] values = credentials.split(":", 2);
        return values[0];
    }
}
