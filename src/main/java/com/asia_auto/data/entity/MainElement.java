package com.asia_auto.data.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;


/**
 * Created by Игорь on 15.08.2016.
 */
@Entity
@NamedQuery(name = "MainElement.getAll", query = "SELECT c from MainElement c")
public class MainElement implements Element, Serializable {
    @Id
    @GeneratedValue
    private long id;
    @ManyToOne(cascade = CascadeType.ALL)
    private TimeElement time;
    @OneToOne(cascade = CascadeType.ALL)
    private ClientElement client;
    @ManyToOne(cascade = CascadeType.ALL)
    private MasterElement master;
    private Date date;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MainElement that = (MainElement) o;

        if (time != null ? !time.equals(that.time) : that.time != null) return false;
        if (client != null ? !client.equals(that.client) : that.client != null) return false;
        if (master != null ? !master.equals(that.master) : that.master != null) return false;
        return date != null ? date.equals(that.date) : that.date == null;

    }

    @Override
    public int hashCode() {
        int result = time != null ? time.hashCode() : 0;
        result = 31 * result + (client != null ? client.hashCode() : 0);
        result = 31 * result + (master != null ? master.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }


    public MainElement() {
    }

    public void setId(long id) {
        this.id = id;
    }

    public TimeElement getTime() {
        return time;
    }

    public void setTime(TimeElement time) {
        this.time = time;
    }

    public ClientElement getClient() {
        return client;
    }

    public void setClient(ClientElement client) {
        this.client = client;
    }

    public MasterElement getMaster() {
        return master;
    }

    public void setMaster(MasterElement master) {
        this.master = master;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public MainElement(TimeElement time, ClientElement client, MasterElement master, Date date) {
        this.time = time;
        this.client = client;
        this.master = master;
        this.date = date;
    }

    @Override
    public String toString() {
        return "MainElement{" +
                "id=" + id +
                ", time=" + time +
                ", client=" + client +
                ", master=" + master +
                ", date=" + date +
                '}';
    }

    public long getId() {
        return id;
    }
}
