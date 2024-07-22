package xyz.actrium.graves.storage;

import xyz.actrium.graves.ActriumGraves;
import xyz.actrium.graves.config.ConfigHandler;
import xyz.actrium.graves.storage.GraveStorage;
import xyz.actrium.graves.storage.impl.GraveMongoStorage;
import xyz.actrium.graves.storage.impl.GraveSQLStorage;
import xyz.actrium.graves.storage.impl.GraveYamlStorage;

public class StorageManager {
    private GraveStorage graveStorage;

    public StorageManager(ActriumGraves plugin) {
        switch (ConfigHandler.get().getStorageConfig().type) {
            case YML:
                this.graveStorage = new GraveYamlStorage();
                break;
            case MYSQL:
                this.graveStorage = new GraveSQLStorage();
                break;
            case MONGODB:
                this.graveStorage = new GraveMongoStorage();
        }

        this.graveStorage.init(plugin);
        ActriumGraves.get().logger.info("Storage has been loaded");
    }

    public GraveStorage getGraveStorage() {
        return this.graveStorage;
    }
}
