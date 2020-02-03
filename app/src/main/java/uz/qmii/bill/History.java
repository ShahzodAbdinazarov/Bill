package uz.qmii.bill;

class History {

    private int id;
    private double money;
    private long time;
    private String info;
    private int type;

    History() {
    }

    History(int id, double money, long time, String info, int type) {
        this.id = id;
        this.money = money;
        this.time = time;
        this.info = info;
        this.type = type;
    }

    int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    double getMoney() {
        return money;
    }

    void setMoney(double money) {
        this.money = money;
    }

    long getTime() {
        return time;
    }

    void setTime(long time) {
        this.time = time;
    }

    String getInfo() {
        return info;
    }

    void setInfo(String info) {
        this.info = info;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
