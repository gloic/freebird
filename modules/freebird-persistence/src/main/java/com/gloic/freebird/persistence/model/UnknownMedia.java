package com.gloic.freebird.persistence.model;

import lombok.Data;

import javax.persistence.Entity;

/**
 * @author gloic
 */
@Entity
@Data
public class UnknownMedia extends AbstractMedia {

    private boolean ignored;

    public Link getLink() {
        return links.iterator().next();
    }
}
