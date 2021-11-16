package com.example.educationgame;

public class Alphabet {

    private String ID;
    private String title;
    private String imageUrl;
    private String gameImage;
    private int difficulty;

    public Alphabet (){
        this.ID = "";
        this.title = "";
        this.imageUrl = "";
        this.gameImage = "";
        this.difficulty = 0;
    }

    public Alphabet(String ID, String title, String imageUrl, String gameImage, int difficulty) {
        this.ID = ID;
        this.title = title;
        this.imageUrl = imageUrl;
        this.gameImage = gameImage;
        this.difficulty = difficulty;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getGameImage() { return gameImage;}

    public void setGameImage(String gameImage) {this.gameImage = gameImage;}
    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public String toString(){
        return "ID: " + ID + "\nWord: " + title + "\nImage Url: " + imageUrl + "\nImage Url (NT): " + gameImage +"\nDifficulty: " + difficulty;
    }
}
