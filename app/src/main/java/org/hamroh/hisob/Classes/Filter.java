package org.hamroh.hisob.Classes;

public class Filter {

    private double up, down, borrow, borrowBack, lend, lendBack;

    public Filter() {
    }

    public Filter(double up, double down, double borrow, double borrowBack, double lend, double lendBack) {
        this.up = up;
        this.down = down;
        this.borrow = borrow;
        this.borrowBack = borrowBack;
        this.lend = lend;
        this.lendBack = lendBack;
    }

    public double getUp() {
        return up;
    }

    public void setUp(double up) {
        this.up = up;
    }

    public double getDown() {
        return down;
    }

    public void setDown(double down) {
        this.down = down;
    }

    public double getBorrow() {
        return borrow;
    }

    public void setBorrow(double borrow) {
        this.borrow = borrow;
    }

    public double getBorrowBack() {
        return borrowBack;
    }

    public void setBorrowBack(double borrowBack) {
        this.borrowBack = borrowBack;
    }

    public double getLend() {
        return lend;
    }

    public void setLend(double lend) {
        this.lend = lend;
    }

    public double getLendBack() {
        return lendBack;
    }

    public void setLendBack(double lendBack) {
        this.lendBack = lendBack;
    }
}
