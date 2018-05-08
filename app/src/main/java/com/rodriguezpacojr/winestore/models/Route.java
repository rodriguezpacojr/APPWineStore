package com.rodriguezpacojr.winestore.models;

/**
 * Created by francisco on 4/20/18.
 */

public class Route {
    private int keyRoute, keyEmployee;
    private String destination, employee;
    private Integer number;
    private Integer customers;

    public Route(int keyRoute, int keyEmployee, String destination, String employee, Integer number, Integer customers) {
        this.keyRoute = keyRoute;
        this.keyEmployee = keyEmployee;
        this.destination = destination;
        this.employee = employee;
        this.number = number;
        this.customers = customers;
    }

    public Route() {

    }

    public int getKeyRoute() {
        return keyRoute;
    }

    public void setKeyRoute(int keyRoute) {
        this.keyRoute = keyRoute;
    }

    public int getKeyEmployee() {
        return keyEmployee;
    }

    public void setKeyEmployee(int keyEmployee) {
        this.keyEmployee = keyEmployee;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getCustomers() {
        return customers;
    }

    public void setCustomers(Integer customers) {
        this.customers = customers;
    }
}