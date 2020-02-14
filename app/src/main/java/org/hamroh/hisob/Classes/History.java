package org.hamroh.hisob.Classes;

public class History {

    private int id;
    private double money;
    private long time;
    private String info;
    private int type;

    public History() {
    }

    public History(int id, double money, long time, String info, int type) {
        this.id = id;
        this.money = money;
        this.time = time;
        this.info = info;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
