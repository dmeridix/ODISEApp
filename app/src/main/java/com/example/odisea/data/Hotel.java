package com.example.odisea.data;

import com.example.odisea.ListItem;

public class Hotel implements ListItem {
    private String id;
    private String type = "hotel";
    private String title;
    private String imageUrl;
    private String description;
    private String location;
    private float rating;

    public Hotel(String id, String title, String imageUrl, String description, String location, float rating) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.description = description;
        this.location = location;
        this.rating = rating;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public float getRating() {
        return rating;
    }
}
