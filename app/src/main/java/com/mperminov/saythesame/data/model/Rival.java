package com.mperminov.saythesame.data.model;

public class Rival extends User {
    private String gameId;

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
