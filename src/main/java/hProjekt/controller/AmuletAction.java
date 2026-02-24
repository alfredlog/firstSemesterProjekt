package hProjekt.controller;

/**
 * Represents the different actions that can be performed when using an amulet.
 */
public enum AmuletAction {
    REMOVE_TILE,
    PLAY_HINT,
    EXTRA_DRIVE,
    REDRAW_PATH_CARDS;

    /**
     * Converts the enum constant to a human-readable string.
     *
     * @return a human-readable representation of the enum constant
     */
    public String toHumanReadableString() {
        return toString().substring(0, 1).toUpperCase()
                + toString().substring(1).toLowerCase().replace("_", " ");
    }
}
