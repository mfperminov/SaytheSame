package com.mperminov.saythesame.data.model;

public class Rival extends User {
    private String gameId;

    public Rival(String uid, String username, String email, String provider, String photo_url,
        String name){
        super(uid,username,email,provider,photo_url,name);
    }

    public Rival(String name, String photo_url) {
        this.name = name;
        this.photo_url = photo_url;
    }
    public Rival(String uid){
        super(uid);
    }
    public Rival(){
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getGameId() {
        return gameId;
    }
}
