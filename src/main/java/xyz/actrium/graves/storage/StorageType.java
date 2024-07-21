package xyz.actrium.graves.storage;

public enum StorageType {
    YML,
    MONGODB,
    MYSQL;

    public static boolean valid(String context) {
        boolean valid = false;
        StorageType[] type = values();

        for (StorageType value : type) {
            if (value.toString().equalsIgnoreCase(context)) {
                valid = true;
                break;
            }
        }

        return valid;
    }
}
