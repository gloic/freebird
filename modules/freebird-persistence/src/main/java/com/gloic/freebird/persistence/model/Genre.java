package com.gloic.freebird.persistence.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author gloic
 */
@Entity
@Data
public class Genre {
    @Id
    private Integer id;
    private String name;

    public Genre() {
    }
}
