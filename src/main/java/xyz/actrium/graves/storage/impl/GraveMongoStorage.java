package xyz.actrium.graves.storage.impl;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bson.Document;
import xyz.actrium.graves.ActriumGraves;
import xyz.actrium.graves.config.impl.StorageConfig;
import xyz.actrium.graves.death.Death;
import xyz.actrium.graves.storage.GraveStorage;
import xyz.actrium.graves.util.ItemUtils;
import xyz.actrium.graves.util.LocationUtils;

public class GraveMongoStorage implements GraveStorage {
    private MongoClient client;
    private MongoDatabase database;
    private MongoCollection<Document> gravesCollection;

    @Override
    public void init(ActriumGraves plugin) {
        StorageConfig storageConfig = plugin.getConfigHandler().getStorageConfig();

        String uri = "mongodb://";
        if (storageConfig.authed) {
            uri = uri + storageConfig.username + ":" + storageConfig.password + "@";
        }

        uri = uri + storageConfig.host + ":" + storageConfig.port;

        this.client = new MongoClient(new MongoClientURI(uri));
        this.database = this.client.getDatabase(storageConfig.dbName);
        this.gravesCollection = this.database.getCollection("graves");
    }

    @Override
    public void onDisable() {
        try {
            if (this.client != null) {
                this.client.close();
            }
        } catch (Exception e) {
            ActriumGraves.get().logger.error("Failed to properly close the Mongo database. ");
            e.printStackTrace();
        }

    }

    @Override
    public List<Death> getGraves() throws IOException, ClassNotFoundException {
        ArrayList<Death> toReturn = new ArrayList();

        for (Document document : this.gravesCollection.find()) {
            String locString = document.getString("location");

            if (!LocationUtils.worldExists(locString)) {
                this.gravesCollection.deleteOne(document);
                continue;
            }

            Death data = new Death(
                    LocationUtils.locationFromString(locString),
                    LocationUtils.locationFromString(document.getString("actualDeathLocation")),
                    UUID.fromString(document.getString("owner")), document.getLong("created"),
                    ItemUtils.convertStringToitems(document.getString("contents"))
            );

            toReturn.add(data);
        }

        return toReturn;
    }

    @Override
    public void saveGrave(Death data) throws IOException {
        Document document = new Document();

        document.append("owner", data.getPlayerId());
        document.append("location", LocationUtils.locationToString(data.getGraveLocation()));
        document.append("actualDeathLocation", LocationUtils.locationToString(data.getDeathLocation()));
        document.append("created", data.getTimeOfDeath());
        document.append("contents", ItemUtils.convertItemsToString(data.getInventoryContents()));

        this.gravesCollection.insertOne(document);
    }

    @Override
    public void removeGrave(Death data) {
        Document search = new Document("location", LocationUtils.locationToString(data.getGraveLocation()));
        this.gravesCollection.findOneAndDelete(search);
    }
}