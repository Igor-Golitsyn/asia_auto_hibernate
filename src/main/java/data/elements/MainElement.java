package data.elements;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

/**
 * Created by Игорь on 15.08.2016.
 */
@Entity
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
