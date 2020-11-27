package org.openjfx.models;

public class Offer {
    private int id;
    private String description;
    private String unit;
    private float price;

    public Offer(int id, String description, String unit, float price) {
        this.id = id;
        this.description = description;
        this.unit = unit;
        this.price = price;
    }

    public Offer() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return description+ " cena : "+ price +" zl za "+ unit;
    }
}
