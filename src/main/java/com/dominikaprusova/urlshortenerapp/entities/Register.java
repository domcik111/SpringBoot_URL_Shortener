package com.dominikaprusova.urlshortenerapp.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter


@Entity
public class Register {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @JsonProperty("url")
    private String url;

    @JsonProperty("redirectType")
    private Integer redirectType;

    @JsonProperty("shortUrl")
    private String shortUrl;

    @JsonProperty("calls")
    private Integer calls;

    @JsonProperty("accountId")
    private String accountId;







}
