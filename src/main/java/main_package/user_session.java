package main_package;

public class user_session {
    private static String username;

    public static void set_user(String name) {
        username = name;
    }

    public static String get_user() {
        return username;
    }
}
