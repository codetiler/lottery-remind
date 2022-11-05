package com.codetiler.lotterycrawler.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@Entity
public class LottoryHistory implements Serializable {

    @Id
    @GeneratedValue(generator = "faceset_generator")
    @GenericGenerator(name = "faceset_generator",strategy = "uuid")
    private String id;

    private String issue;

    private String blue;

    private String reds;
}
