package com.rodriguezpacojr.winestore.models;

/**
 * Created by francisco on 4/20/18.
 */

public class Product {

    private Integer keyProduct;
    private Integer keyTypeProduct;
    private String name;
    private double ml;
    private String color;
    private String taste;
    private Integer stock;
    private double salesPrice;
    private String photo;
    private String TypeProduct;
    private Integer availables;
    private Integer quantity;

    public Product(){
    }

    public Integer getKeyProduct() {
        return keyProduct;
    }

    public void setKeyProduct(Integer keyProduct) {
        this.keyProduct = keyProduct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMl() {
        return ml;
    }

    public void setMl(double ml) {
        this.ml = ml;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTaste() {
        return taste;
    }

    public void setTaste(String taste) {
        this.taste = taste;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public double getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(double salesPrice) {
        this.salesPrice = salesPrice;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTypeProduct() {
        return TypeProduct;
    }

    public void setTypeProduct(String TypeProduct) {
        this.TypeProduct = TypeProduct;
    }

    public Integer getAvailables() {
        return availables;
    }

    public void setAvailables(Integer availables) {
        this.availables = availables;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getKeyTypeProduct() {
        return keyTypeProduct;
    }

    public void setKeyTypeProduct(Integer keyTypeProduct) {
        this.keyTypeProduct = keyTypeProduct;
    }
}