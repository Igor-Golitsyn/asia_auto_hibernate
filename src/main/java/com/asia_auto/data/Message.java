package com.asia_auto.data;


import com.asia_auto.data.entity.Element;

import java.io.Serializable;
import java.sql.Date;
import java.util.TreeMap;

/**
 * Класс описывает передаваемое сообщение.
 */
public class Message implements Serializable {
    private final MessageType type;
    private final TreeMap<Long, Element> map;
    private final Date date;
    private final String dopInfo;

    public Message(MessageType type, TreeMap<Long, Element> map, Date date, String dopInfo) {
        this.type = type;
        this.map = map;
        this.date = date;
        this.dopInfo = dopInfo;
    }

    public Message(MessageType type, Date date) {
        this.type = type;
        this.date = date;
        map = null;
        dopInfo = null;
    }

    public MessageType getType() {
        return type;
    }

    public TreeMap<Long, Element> getMap() {
        return map;
    }

    public Date getDate() {
        return date;
    }

    public String getDopInfo() {
        return dopInfo;
    }

    @Override
    public String toString() {
        return "Message{" +
                "type=" + type +
                ", map=" + map +
                ", date=" + date +
                ", dopInfo='" + dopInfo + '\'' +
                '}';
    }
}
