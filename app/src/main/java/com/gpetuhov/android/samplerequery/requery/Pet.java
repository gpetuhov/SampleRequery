package com.gpetuhov.android.samplerequery.requery;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.ManyToOne;

@Entity
abstract class AbstractPet {

    @Key @Generated
    int id;

    String type;
    String name;
    int age;

    @ManyToOne
    AbstractPerson owner;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public AbstractPerson getOwner() {
        return owner;
    }

    public void setOwner(AbstractPerson owner) {
        this.owner = owner;
    }
}