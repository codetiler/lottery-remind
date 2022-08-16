package com.codetiler.lotterycrawler.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Lottery {

    @Id
    @GeneratedValue(generator = "faceset_generator")
    @GenericGenerator(name = "faceset_generator",strategy = "uuid")
    private String id;

    private String issue;

    private String blue;

    private String reds;



}
