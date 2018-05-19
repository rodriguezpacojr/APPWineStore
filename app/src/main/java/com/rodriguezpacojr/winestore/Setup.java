package com.rodriguezpacojr.winestore;

public class Setup {

    public static String token = "8a1cde295d303ac40368abc57342482f";
    public static String IP_ADDRESS="192.168.0.5";
    public static String PORT_NUMBER="8080";
    public static String userName;
    public static int keyCustomer;

    //========================================================================================
    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        Setup.token = token;
    }

    public static String getIpAddress() {
        return IP_ADDRESS;
    }

    public static void setIpAddress(String ipAddress) {
        IP_ADDRESS = ipAddress;
    }

    public static String getPortNumber() {
        return PORT_NUMBER;
    }

    public static void setPortNumber(String portNumber) {
        PORT_NUMBER = portNumber;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        Setup.userName = userName;
    }

    public static int getKeyCustomer() {
        return keyCustomer;
    }

    public static void setKeyCustomer(int keyCustomer) {
        Setup.keyCustomer = keyCustomer;
    }
}