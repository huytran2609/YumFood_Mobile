package com.example.yumfood.models;

public class Home_CategoriesModel {
    String img_url;
    String name;
    String type;

    public Home_CategoriesModel(){ }
    public Home_CategoriesModel(String img_url, String name, String type){
        this.img_url = img_url;
        this.name = name;
        this.type = type;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
