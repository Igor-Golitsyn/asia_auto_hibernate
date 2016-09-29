package com.asia_auto.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import java.io.Serializable;
import java.sql.Time;

/**
 * Created by Игорь on 12.08.2016.
 */
@Entity
@NamedQuery(name = "TimeElement.getAll", query = "SELECT c from TimeElement c")
public class TimeElement implements Element, Serializable {
    @Id
    @GeneratedValue
    private long id;
    private Time time;

    public TimeElement() {
    }

    public TimeElement(Time time) {
        this.time = time;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "TimeElement{" +
                "id=" + id +
                ", time=" + time +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimeElement that = (TimeElement) o;

        return time.equals(that.time);

    }

    @Override
    public int hashCode() {
        return time.hashCode();
    }
}
