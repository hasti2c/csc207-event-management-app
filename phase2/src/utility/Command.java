package utility;

// TODO merge LOGIN and TRIAL_MENU
public enum Command {
    START_UP("Start Up Menu"),
    ADMIN_MENU("Admin Menu"),
    SIGN_UP("Sign Up"),
    // LOGIN is MAIN_MENU
    LOGIN("Main Menu"),
    TRIAL_MENU("Trial Menu"),
    FORGOT_PASSWORD("Forgot Password"),
    EXIT("Exit"),
    CREATE_EVENT("Create Event"),
    BROWSE_EVENTS("Browse Events"),
    EDIT_TEMPLATE("Edit Template"),
    BROWSE_USERS("User List"),
    ACCOUNT_MENU("Account Menu"),
    SAVE("Save"),
    LOG_OUT("Log Out"),
    EXIT_TRIAL("Exit Trial"),
    CHANGE_USERNAME("Change Username"),
    CHANGE_PASSWORD("Change Password"),
    CHANGE_EMAIL("Change Email"),
    CHANGE_TO_ADMIN("Change User Type to Admin"),
    DELETE_ACCOUNT("Delete My Account"),
    GO_BACK("Go Back"),
    ATTEND_EVENT("Attend Event"),
    UNATTEND_EVENT("Unattend Event"),
    CHANGE_EVENT_PRIVACY("Change Privacy Status"),
    EDIT_EVENT("Edit Event"),
    DELETE_EVENT("Delete Event"),
    SUSPEND_EVENT ("Suspend Event"),
    UNSUSPEND_EVENT("Unsuspend Event"),
    FRIEND_USER("Friend User"),
    UNFRIEND_USER("Unfriend User"),
    SUSPEND_USER("Suspend User"),
    UNSUSPEND_USER("Unsuspend User");
    // TODO ability to go from event to user or vice versa

    private final String name;

    Command(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
