package com.youssef.yasmine.musy.Util;

public class ServerConnection {


    private static ServerConnection instance;
    private String server;
    private ServerConnection(){}

    public String getServer() {
        return "http://192.168.1.16:3003";
    }

    public void setServer(String server) {
        this.server = server;
    }

    public static synchronized ServerConnection getInstance(){
        if(instance==null){
            instance=new ServerConnection();
        }
        return instance;
    }
}
