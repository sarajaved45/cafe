package com.example.cafedesign;

public class Creation {

    private float scaleX;
    private float scaleY;
    private float rotation;

    private String imageUrl;

    // Constructor, getters, and setters
    public Creation() {
        // Default constructor required for Firestore
    }

    public Creation(float scaleX, float scaleY, float rotation, long timestamp, String imageUrl, float x, float y) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.rotation = rotation;

        this.imageUrl = imageUrl;

    }

    public float getScaleX() {
        return scaleX;
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }



    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


}
