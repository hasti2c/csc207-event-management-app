package utility;

/**
 * Enum of Types of Users.
 */
public enum UserType {
    /**
     * Normal user.
     */
    REGULAR,

    /**
     * User with admin abilities.
     */
    ADMIN,

    /**
     * User whose data is not saved.
     */
    TRIAL,

    /**
     * User whose account gets suspended after a certain amount of time.
     */
    TEMPORARY
}
