package cn.edu.swpu.cins.weike.async;

public enum EventType {

    MAIL(0);

    private int value;

    EventType() {
    }

    EventType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
