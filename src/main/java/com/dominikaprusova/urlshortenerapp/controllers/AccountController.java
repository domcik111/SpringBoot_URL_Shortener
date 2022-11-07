package com.dominikaprusova.urlshortenerapp.controllers;


import com.dominikaprusova.urlshortenerapp.entities.Account;
import com.dominikaprusova.urlshortenerapp.repositories.AccountRepository;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping(value = "/account")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;


    //method for obtaining all accounts that have been created
    @GetMapping
    public Iterable<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    // method for adding an account, the parameter contains the account we want to add,
    // if the account is null, we do not continue
    //there is also a check to see if the account with the given ID already exists,
    // if not, a password is created, the account is assigned and the account is added to the db
    @PostMapping
    public ResponseEntity addAccount(@RequestBody final Account account) {
        if (account != null) {
            final Iterable<Account> accountsFromDb = accountRepository.findAll();
            for (final Account ac : accountsFromDb) {
                if (Objects.equals(ac.getAccountId(), account.getAccountId())) {
                    return new ResponseEntity<>(createResponse(false, null).toString(), HttpStatus.BAD_REQUEST);
                }
            }
            final String password = createHash(8);
            account.setPassword(password);
            accountRepository.save(account);
            return new ResponseEntity<>(createResponse(true, password).toString(), HttpStatus.CREATED);
        }
        return ResponseEntity.badRequest().build();
    }

    // creating a json response for adding account
    private JSONObject createResponse(boolean status, String password) {
        JSONObject resp = new JSONObject();
        resp.put("success", status);
        if (status) {
            resp.put("description", "Account created succesfully");
            resp.put("password", password);
        } else {
            resp.put("description", "Account with that ID already exist, account was not created");
        }
        return resp;
    }

    //method for creating a password or a shortened url (password n ​​= 8, hash n = 6)
    static String createHash(int n) {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());

            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

}
