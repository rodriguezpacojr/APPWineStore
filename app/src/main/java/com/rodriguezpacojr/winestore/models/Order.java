package com.rodriguezpacojr.winestore.models;

/**
 * Created by francisco on 4/20/18.
 */

public class Order {

    private Integer keyOrder;
    private Integer keyEmployee;
    private Integer keyCustomer;
    private Integer keyProduct;
    private Integer quantity;

    public Order(){
    }

    public Integer getKeyOrder() {
        return keyOrder;
    }

    public void setKeyOrder(Integer keyOrder) {
        this.keyOrder = keyOrder;
    }

    public Integer getKeyEmployee() {
        return keyEmployee;
    }

    public void setKeyEmployee(Integer keyEmployee) {
        this.keyEmployee = keyEmployee;
    }

    public Integer getKeyCustomer() {
        return keyCustomer;
    }

    public void setKeyCustomer(Integer keyCustomer) {
        this.keyCustomer = keyCustomer;
    }

    public Integer getKeyProduct() {
        return keyProduct;
    }

    public void setKeyProduct(Integer keyProduct) {
        this.keyProduct = keyProduct;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}