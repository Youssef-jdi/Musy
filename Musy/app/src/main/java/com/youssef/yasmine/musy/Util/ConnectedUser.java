package com.youssef.yasmine.musy.Util;

public class ConnectedUser {
    private static ConnectedUser instance;
    private String connected_user;
    private ConnectedUser(){}

    public String getConnected_user() {
        return connected_user;
    }

    public void setConnected_user(String connected_user) {
        this.connected_user = connected_user;
    }

    public static synchronized ConnectedUser getInstance(){
        if(instance==null){
            instance=new ConnectedUser();
        }
        return instance;
    }
}
