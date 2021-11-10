package com.example.android_application.ultilities;

public class Constants {
    // Set up the constant values to cycle between the application and the database

    // Fire store collection
    public static final String COLLECTION_USERS = "users";

    //User info
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String USER_ID = "userId";
    public static final String IMAGE= "image";
    public static final String USER = "user";

    //Chat app preference
    public static final String PREFERENCE_NAME = "chatAppPreference";

    //Signed in state
    public static final String IS_SIGNED_IN = "isSignedIn";
    //Token
    public static final String FCM_TOKEN = "fcmtoken";

    //Realtime chat implementation
    public static final String COLLECTION_CHAT = "chat";
    public static final String SENDER_ID = "senderId";
    public static final String RECEIVER_ID= "receiverId";
    public static final String MESSAGE = "message";
    public static final String TIMESTAMP = "timestamp";
}