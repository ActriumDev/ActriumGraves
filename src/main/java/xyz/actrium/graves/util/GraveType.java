package xyz.actrium.graves.util;

public enum GraveType {
    SKULL;

    public static boolean valid(String context) {
        boolean valid = false;
        GraveType[] type = values();
        for (GraveType value : type) {
            if (value.toString().equalsIgnoreCase(context)) {
                valid = true;
                break;
            }
        }

        return valid;
    }
}
