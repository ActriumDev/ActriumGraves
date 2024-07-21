package xyz.actrium.graves.storage;

import java.io.IOException;
import java.util.List;
import xyz.actrium.graves.ActriumGraves;
import xyz.actrium.graves.death.Death;

public interface GraveStorage {
    void init(ActriumGraves plugin);

    default void onDisable() {
    }

    void saveGrave(Death death) throws IOException;

    void removeGrave(Death death);

    List<Death> getGraves() throws IOException, ClassNotFoundException;
}
