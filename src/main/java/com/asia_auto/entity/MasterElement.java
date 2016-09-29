package com.asia_auto.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Игорь on 12.08.2016.
 */
@Entity
@NamedQuery(name = "MasterElement.getAll", query = "SELECT c from MasterElement c")
public class MasterElement implements Element, Serializable {
    @Id
    @GeneratedValue
    private long id;
    private String name;
    private String secondName;
    private String family;
    private String smena;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] foto;

    public MasterElement() {
    }

    public MasterElement(String name, String secondName, String family, String smena, byte[] foto) {
        this.name = name;
        this.secondName = secondName;
        this.family = family;
        this.foto = foto;
        this.smena = smena;
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

    public String getSmena() {
        return smena;
    }

    public void setSmena(String smena) {
        this.smena = smena;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    @Override
    public String toString() {
        return "MasterElement{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", secondName='" + secondName + '\'' +
                ", family='" + family + '\'' +
                ", smena='" + smena + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MasterElement that = (MasterElement) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (secondName != null ? !secondName.equals(that.secondName) : that.secondName != null) return false;
        return family != null ? family.equals(that.family) : that.family == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (secondName != null ? secondName.hashCode() : 0);
        result = 31 * result + (family != null ? family.hashCode() : 0);
        return result;
    }
}
