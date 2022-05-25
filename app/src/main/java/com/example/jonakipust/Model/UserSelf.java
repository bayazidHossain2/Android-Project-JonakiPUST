package com.example.jonakipust.Model;

public class UserSelf {
    private final UserModel userModel;
    private final UserAdditionalInfo userAdditionalInfo;
    private static UserSelf userSelf;

    private UserSelf(UserModel userModel,UserAdditionalInfo userAdditionalInfo){
        this.userModel = userModel;
        this.userAdditionalInfo = userAdditionalInfo;
    }

    public static UserSelf getUserSelf() {
        return userSelf;
    }

    public static UserSelf getUserSelf(UserModel userModel,UserAdditionalInfo userAdditionalInfo) {
        userSelf = new UserSelf(userModel,userAdditionalInfo);
        return userSelf;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public UserAdditionalInfo getUserAdditionalInfo() {
        return userAdditionalInfo;
    }
}
