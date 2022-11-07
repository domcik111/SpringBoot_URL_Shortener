package com.dominikaprusova.urlshortenerapp.controllers;


import com.dominikaprusova.urlshortenerapp.entities.Register;
import com.dominikaprusova.urlshortenerapp.repositories.RegisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping(value = "/redirect")
public class RedirectController {

    @Autowired
    private RegisterRepository registerRepository;

    //method for the redirect short url specified from the function parameter,
    // if the given short url is found in the db url, its long version is found and redirected to it
    @GetMapping("/{shortUrl}")
    public ResponseEntity redirect(@PathVariable("shortUrl") final String shortUrl) {
        final Iterable<Register> registers = registerRepository.findAll();
        String shortUrlDb = null;
        Register register = null;
        for (final Register reg : registers) {
            final String sanitizedUrl = sanitizeUrl(reg.getShortUrl());
            if (sanitizedUrl.equals(shortUrl)) {
                shortUrlDb = shortUrl;
                register = reg;
                break;
            }
        }
        if (shortUrlDb != null) {
            register.setCalls(register.getCalls() + 1);
            registerRepository.save(register);
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(register.getUrl()))
                    .build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .build();
    }

    // profit from short url here last part for '/'
    private String sanitizeUrl(final String url) {
        return url.substring(url.lastIndexOf('/') + 1).trim();
    }

}
