package com.mperminov.saythesame.data.model;



public class Rival extends User {


    public Rival(String uid) {
        super(uid);
    }
    public Rival(String name, String photo_url){
        this.name = name;
        this.photo_url = photo_url;
    }
}
