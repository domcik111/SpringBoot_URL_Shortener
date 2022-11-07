package com.dominikaprusova.urlshortenerapp.repositories;

import com.dominikaprusova.urlshortenerapp.entities.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, String> {

}
