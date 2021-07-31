package controllers;

import java.lang.reflect.Method;

public enum Command {
    SIGN_UP("Sign Up", "signUp"),
    LOGIN("Login", "login"),
    TRIAL("Trial", "runTrialMenu"),
    EXIT("Exit", "exit"),
    CREATE_EVENT("Create Event", "createEvent"),
    VIEW_ATTENDED("View Attended Events", "viewAttended"),
    VIEW_UNATTENDED("View Not Attended Events", "viewUnattended"),
    VIEW_OWNED("View My Events", "viewOwned"),
    VIEW_PUBLISHED("View Published Events", "viewPublished"),
    EDIT_TEMPLATE("Edit Template", "editTemplate"),
    ACCOUNT_MENU("Account Menu", "runAccountMenu"),
    SAVE("Save", "saveAll"),
    LOG_OUT("Log Out", "logOut"),
    CHANGE_USERNAME("Change Username", "changeUsername"),
    CHANGE_PASSWORD("Change Password", "changePassword"),
    CHANGE_EMAIL("Change Email", "changeEmail"),
    CHANGE_TO_ADMIN("Change User Type to Admin", "changeToAdmin"),
    DELETE_ACCOUNT("Delete My Account", "deleteAccount"),
    GO_BACK("Go Back", "goBack");

    private String name;
    private Method method;

    Command(String name, String methodName) {
        this.name = name;
        try {
            method = SystemController.class.getDeclaredMethod(methodName);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public Method getMethod() {
        return method;
    }
}
