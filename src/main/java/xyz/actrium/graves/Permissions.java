package xyz.actrium.graves;

public enum Permissions {
    UPDATE_MESSAGE("updatemessage", "Allows players to see the new update message when they join the server."),
    ADMIN_DELETE("admindelete", "Allows players to run the \"/grave admindelete\" command."),
    INFO_COMMAND("info", "Allows players to run the \"/grave info\" command."),
    DELETE("delete", "Allows players to run the \"/grave delete\" command.");

    private final String string;
    private final String description;
    private final String prefix = "actriumgraves:";

    Permissions(String string, String description) {
        this.string = string;
        this.description = description;
    }

    public String asString() {
        return "actriumgraves:" + this.string;
    }

    public String getDescription() {
        return this.description;
    }
}
