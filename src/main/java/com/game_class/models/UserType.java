package com.game_class.models;

public enum UserType {

    TEACHER("teacher"),
    STUDENT("student");

    private final String userType;

    UserType(String userType) {
        this.userType = userType;
    }

    public String getUserType() {
        return userType;
    }
}
