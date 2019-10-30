package uz.qmii.bill;

public class History {

    private int id;
    private double money;
    private String time, info;
    boolean isIncome;

    History() {
    }

    History(int id, int money, String time, String info, boolean isIncome) {
        this.id = id;
        this.money = money;
        this.time = time;
        this.info = info;
        this.isIncome = isIncome;
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

    void setMoney(Double money) {
        this.money = money;
    }

    String getTime() {
        return time;
    }

    void setTime(String time) {
        this.time = time;
    }

    String getInfo() {
        return info;
    }

    void setInfo(String info) {
        this.info = info;
    }

    boolean isIncome() {
        return isIncome;
    }

    void setIncome(boolean income) {
        isIncome = income;
    }

}
