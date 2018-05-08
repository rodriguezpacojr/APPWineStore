package com.rodriguezpacojr.winestore;

public class Setup {

    public static String token;
    public static String IP_ADDRESS;
    public static String PORT_NUMBER;
    public static String USER;
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

    public static String getUSER() {
        return USER;
    }

    public static void setUSER(String USER) {
        Setup.USER = USER;
    }
}