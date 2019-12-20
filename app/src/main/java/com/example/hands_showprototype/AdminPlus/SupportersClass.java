package com.example.hands_showprototype.AdminPlus;

public class SupportersClass {
    private String UID;
    private String Name;

    public SupportersClass(){}
    public SupportersClass(String UID, String Name){
        this.Name=Name.toString();
        this.UID=UID.toString();
    }

    public String GetUID(){
        return UID;
    }

    public String GetName(){
        return Name;
    }
}
