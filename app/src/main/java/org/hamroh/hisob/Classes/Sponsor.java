package org.hamroh.hisob.Classes;

public class Sponsor {

    private int id;
    private String image, name, desc, link;
    private int imageResource;

    public Sponsor() {
    }

    public Sponsor(int id, String image, String name, String desc, String link) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.desc = desc;
        this.link = link;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }
}
