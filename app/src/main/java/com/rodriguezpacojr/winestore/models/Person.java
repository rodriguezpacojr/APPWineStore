package com.rodriguezpacojr.winestore.models;

/**
 * Created by francisco on 4/21/18.
 */

public class Person {
    private Integer keyPerson;
    private String name;
    private String lastName;
    private String bornDate;
    private String email;
    private String phone;
    private String rfc;
    private String photo;
    private String entryDate;
    private Double latitude;
    private Double longitude;
    private int keyRoute;
    private int keyUser;

    public Person() {

    }


    public int getKeyRoute() {
        return keyRoute;
    }

    public void setKeyRoute(int keyRoute) {
        this.keyRoute = keyRoute;
    }

    public int getKeyUser() {
        return keyUser;
    }

    public void setKeyUser(int keyUser) {
        this.keyUser = keyUser;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getKeyPerson() {
        return keyPerson;
    }

    public void setKeyPerson(Integer keyPerson) {
        this.keyPerson = keyPerson;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBornDate() {
        return bornDate;
    }

    public void setBornDate(String bornDate) {
        this.bornDate = bornDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entrybornDate) {
        this.entryDate = entrybornDate;
    }
}