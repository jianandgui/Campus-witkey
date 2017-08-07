package cn.edu.swpu.cins.weike.async;

import java.util.HashMap;
import java.util.Map;

public class EventModel {

    private EventType eventType;
    private Map<String, String> exts = new HashMap<String, String>();

    public EventModel(EventType eventType) {
        this.eventType = eventType;
    }

    public EventModel() {
    }

    public EventType getEventType() {
        return eventType;
    }

    public EventModel setEventType(EventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public Map<String, String> getExts() {
        return exts;
    }

    public EventModel setExts(String key,String value) {
        exts.put(key, value);
        return this;
    }
}
