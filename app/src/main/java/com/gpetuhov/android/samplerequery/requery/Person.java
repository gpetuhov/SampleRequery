package com.gpetuhov.android.samplerequery.requery;

import java.util.List;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.OneToMany;

@Entity
abstract class AbstractPerson {

    @Key @Generated
    int id;

    String name;

    @OneToMany
    List<AbstractPet> pets;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AbstractPet> getPets() {
        return pets;
    }

    public void setPets(List<AbstractPet> pets) {
        this.pets = pets;
    }
}
