package org.openjfx;

import org.openjfx.models.User;

public class AccountManager {
    private static AccountManager instance;
    public static User user;

    private AccountManager(User user) {
        this.user = user;
    }

    public static AccountManager logIn(User user) {
        if (instance == null) {
            instance = new AccountManager(user);
        }
        return instance;
    }

    public static void logOut() {
        instance = null;
    }
}
