package com.example.xcifers;

import com.google.gson.Gson;

import java.io.Serializable;

public class MainModel implements Serializable {
    String name, image, description, owner, HighestBidPlacer;
    int highestBidder;
    MainModel(){

    }

    public int getHighestBidder() {
        return highestBidder;
    }

    public void setHighestBidder(int highestBidder) {
        this.highestBidder = highestBidder;
    }

    public MainModel(String name, String image, String description, String owner, String HighestBidPlacer, int highestBidder) {
        this.name = name;
        this.image = image;
        this.description = description;
        this.owner = owner;
        this.highestBidder= highestBidder;
        this.HighestBidPlacer= HighestBidPlacer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner() {
        return owner;
    }

    public String getHighestBidPlacer() {
        return HighestBidPlacer;
    }

    public void setHighestBidPlacer(String highestBidPlacer) {
        this.HighestBidPlacer = highestBidPlacer;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    // Deserialize JSON to object
    public static MainModel fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, MainModel.class);
    }
}
