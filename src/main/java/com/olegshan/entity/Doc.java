package com.olegshan.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by olegshan on 02.10.2016.
 */
@Entity
public class Doc {

    @Id
    String name;
//    @Column(length = Integer.MAX_VALUE)
    @Column(length = 10485760)
    String doc;

    public Doc() {
    }

    public Doc(String name, String doc) {
        this.name = name;
        this.doc = doc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }
}
