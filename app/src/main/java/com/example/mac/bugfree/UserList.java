package com.example.mac.bugfree;

import java.util.ArrayList;

/**
 * @author Zhi Li
 */

public class UserList {
    // A public arrayList, shared by all users, all users created are stored in this list.
    private static ArrayList<User> UserList= new ArrayList<User>();
    public UserList() {
    }

    public Integer getUserListSize() {
        return this.UserList.size();
    }
    public void addUser(User user) {
        UserList.add(user);
    }

    public User getUser(Integer userID) {
        return UserList.get(userID);
    }
    public void cleanList(){UserList.clear();}
}