package org.openjfx.models;

public class Order {
    private int id;
    private Offer offer;
    private int quantity;

    public Order() {
    }

    public Order(int id, Offer offer, int quantity) {
        this.id = id;
        this.offer = offer;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", offer=" + offer +
                ", quantity=" + quantity +
                '}';
    }
}
