package com.asia_auto.client;

import com.asia_auto.data.Message;
import com.asia_auto.data.MessageType;
import com.asia_auto.data.dao.ElementEntityDao;
import com.asia_auto.data.entity.*;

import java.sql.Date;
import java.util.List;
import java.util.Observable;
import java.util.TreeMap;
import java.util.logging.Logger;

/**
 * Created by Игорь on 07.11.2016.
 */
public class Model extends Observable implements Runnable {
    private Logger logger = Logger.getLogger(Model.class.getName());
    private Message message;

    public Model(Message message) {
        this.message = message;
    }

    @Override
    public void run() {
        switch (message.getType()) {
            case GET_CLIENTS:
                setChanged();
                notifyObservers(getData(ClientElement.class, MessageType.CLIENTS));
                break;
            case GET_MASTERS:
                setChanged();
                notifyObservers(getData(MasterElement.class, MessageType.MASTERS));
                break;
            case GET_TIMES:
                setChanged();
                notifyObservers(getData(TimeElement.class, MessageType.TIMES));
                break;
            case GET_MAIN_FOR_DATE:
                setChanged();
                notifyObservers(getDataForDate(MessageType.MAIN_FOR_DATE, message.getDate()));
                break;
            case GET_TIMES_FOR_DATE:
                setChanged();
                notifyObservers(getDataForDate(MessageType.TIMES_FOR_DATE, message.getDate()));
                break;
            case INPUT_DATA:
                setChanged();
                notifyObservers(inputData(message));
                break;
            case DELETE_DATA:
                setChanged();
                notifyObservers(delData(message));
                break;
        }
    }

    private Message delData(Message message) {
        MessageType type = MessageType.ACCEPTED;
        for (Element e : message.getMap().values()) {
            if (!ElementEntityDao.getInstance().deleteElement(e)) {
                type = MessageType.ERROR;
                break;
            }
        }
        return new Message(type, null, message.getDate(), null);
    }

    private Message inputData(Message message) {
        MessageType type = MessageType.ACCEPTED;
        TreeMap<Long, Element> map = new TreeMap<>();
        for (Element e : message.getMap().values()) {
            Element nElem = ElementEntityDao.getInstance().writeElement(e);
            if (nElem.equals(e)) {
                map.put(nElem.getId(), nElem);
            } else {
                type = MessageType.ERROR;
                map = null;
                break;
            }
        }
        return new Message(type, map, message.getDate(), null);
    }

    private <T extends Element> Message getData(Class<T> clazz, MessageType forResponse) {
        TreeMap<Long, Element> map = new TreeMap<>();
        List<T> list = ElementEntityDao.getInstance().getAllElements(clazz);
        for (T t : list) {
            map.put(t.getId(), t);
        }
        return new Message(forResponse, map, null, null);
    }

    private Message getDataForDate(MessageType forResponse, Date date) {
        TreeMap<Long, Element> map = new TreeMap<>();
        List<MainElement> mainList = ElementEntityDao.getInstance().getMainForDate(date);
        List<TimeElement> timeList = ElementEntityDao.getInstance().getAllElements(TimeElement.class);
        switch (forResponse) {
            case MAIN_FOR_DATE:
                for (MainElement mainElement : mainList) {
                    map.put(mainElement.getId(), mainElement);
                }
                break;
            case TIMES_FOR_DATE:
                int limit = 3;// лимит одновременных клиентов
                for (TimeElement timeElement : timeList) {
                    int count = 0;
                    if (mainList != null && mainList.size() != 0) {
                        for (MainElement mainElement : mainList) {
                            if (timeElement.equals(mainElement.getTime())) count++;
                        }
                    }
                    if (count < limit) map.put(timeElement.getId(), timeElement);
                }
        }
        return new Message(forResponse, map, date, null);
    }
}
