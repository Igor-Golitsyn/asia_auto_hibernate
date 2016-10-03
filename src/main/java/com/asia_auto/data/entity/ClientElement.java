package com.asia_auto.data.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import java.io.Serializable;

/**
 * Created by Игорь on 15.08.2016.
 */
@Entity
@NamedQuery(name = "ClientElement.getAll", query = "SELECT c from ClientElement c")
public class ClientElement implements Element, Serializable {
    @Id
    @GeneratedValue
    private long id;
    private String name;
    private String secondName;
    private String family;
    private String phone;
    private String email;
    private String town;
    private String street;
    private String house;
    private String flat;
    private String comment;

    public ClientElement() {
    }

    public ClientElement( String name, String secondName, String family, String phone, String email, String town, String street, String house, String flat, String comment) {
        this.name = name;
        this.secondName = secondName;
        this.family = family;
        this.phone = phone;
        this.email = email;
        this.town = town;
        this.street = street;
        this.house = house;
        this.flat = flat;
        this.comment = comment;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "ClientElement{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", secondName='" + secondName + '\'' +
                ", family='" + family + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", town='" + town + '\'' +
                ", street='" + street + '\'' +
                ", house='" + house + '\'' +
                ", flat='" + flat + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientElement that = (ClientElement) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (secondName != null ? !secondName.equals(that.secondName) : that.secondName != null) return false;
        if (family != null ? !family.equals(that.family) : that.family != null) return false;
        if (phone != null ? !phone.equals(that.phone) : that.phone != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (town != null ? !town.equals(that.town) : that.town != null) return false;
        if (street != null ? !street.equals(that.street) : that.street != null) return false;
        if (house != null ? !house.equals(that.house) : that.house != null) return false;
        if (flat != null ? !flat.equals(that.flat) : that.flat != null) return false;
        return comment != null ? comment.equals(that.comment) : that.comment == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (secondName != null ? secondName.hashCode() : 0);
        result = 31 * result + (family != null ? family.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (town != null ? town.hashCode() : 0);
        result = 31 * result + (street != null ? street.hashCode() : 0);
        result = 31 * result + (house != null ? house.hashCode() : 0);
        result = 31 * result + (flat != null ? flat.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        return result;
    }
}
