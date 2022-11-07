package com.dominikaprusova.urlshortenerapp.repositories;

import com.dominikaprusova.urlshortenerapp.entities.Register;
import org.springframework.data.repository.CrudRepository;

public interface RegisterRepository extends CrudRepository<Register, Integer> {

}
