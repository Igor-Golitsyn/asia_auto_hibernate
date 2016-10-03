package com.asia_auto.data.entity;

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
    private String comment;

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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimeElement that = (TimeElement) o;

        if (time != null ? !time.equals(that.time) : that.time != null) return false;
        return comment != null ? comment.equals(that.comment) : that.comment == null;

    }

    @Override
    public int hashCode() {
        int result = time != null ? time.hashCode() : 0;
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TimeElement{" +
                "id=" + id +
                ", time=" + time +
                ", comment='" + comment + '\'' +
                '}';
    }
}
