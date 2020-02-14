package org.hamroh.hisob.Classes;

public class Savol {
    private int id;
    private String savol, javob;

    public Savol() {
    }

    public Savol(int id, String savol, String javob) {
        this.id = id;
        this.savol = savol;
        this.javob = javob;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSavol() {
        return savol;
    }

    public void setSavol(String savol) {
        this.savol = savol;
    }

    public String getJavob() {
        return javob;
    }

    public void setJavob(String javob) {
        this.javob = javob;
    }
}
