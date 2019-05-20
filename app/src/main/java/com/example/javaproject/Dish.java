package com.example.javaproject;

public class Dish
{
    private String name;
    private int imageId;
    public Dish(String name, int imageId)
    {
        this.name = name;
        this.imageId = imageId;
    }
    public String getName()
    {
        return this.name;
    }
    public int getImageId()
    {
        return this.imageId;
    }
}
