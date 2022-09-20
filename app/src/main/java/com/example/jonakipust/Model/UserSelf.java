package com.example.jonakipust.Model;

public class UserSelf {
    private final UserModel userModel;
    private static UserSelf userSelf;

    private UserSelf(UserModel userModel){
        this.userModel = userModel;
    }

    public static UserSelf getUserSelf() {
        return userSelf;
    }

    public static UserSelf getUserSelf(UserModel userModel) {
        userSelf = new UserSelf(userModel);
        return userSelf;
    }

    public UserModel getUserModel() {
        return userModel;
    }
}
