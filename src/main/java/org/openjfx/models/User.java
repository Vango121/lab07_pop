package org.openjfx.models;

public class User {
    private int id;
    private String username;
    private int accType;

    public User(int id, String username, int accType) {
        this.id = id;
        this.username = username;
        this.accType = accType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAccType() {
        return accType;
    }


    public void setAccType(int accType) {
        this.accType = accType;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", accType=" + accType +
                '}';
    }
}
