package com.mysampleapp.model;

/**
 * Created by User on 2017/5/30.
 */

public class ProductItem {
    private String name;
    private String price;
    private String img_url;
    private String location;
    private String month_consume;
    private String repurchase_rate;

    public ProductItem(String name){
        this.name = name;
        this.price = "";
        this.img_url = "";
        this.month_consume="";
        this.repurchase_rate="";
        this.location = "";
    }

    public ProductItem(String name, String location, String price) {
        this.name = name;
        this.price = price;
        this.img_url = "";
        this.month_consume="";
        this.repurchase_rate="";
        this.location = location;
    }

    public ProductItem(String name, String location, String price, String img_url, String month_consume, String repurchase_rate) {
        this.name = name;
        this.price = price;
        this.img_url = img_url;
        this.month_consume=month_consume;
        this.repurchase_rate=repurchase_rate;
        this.location = location;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setLocation(String location){
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setPrice(String price){
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setImgUrl(String img_url){
        this.img_url = img_url;
    }

    public String getImgUrl() {
        return img_url;
    }

    public void setMonthConsume(String month_consume){
        this.month_consume = month_consume;
    }

    public String getMonthConsume() {
        return month_consume;
    }

    public void setRepurchaseRate(String repurchase_rate){
        this.repurchase_rate = repurchase_rate;
    }

    public String getRepurchaseRate() {
        return repurchase_rate;
    }
}
