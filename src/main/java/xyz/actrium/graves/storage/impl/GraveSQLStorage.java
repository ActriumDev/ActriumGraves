package xyz.actrium.graves.storage.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import xyz.actrium.graves.ActriumGraves;
import xyz.actrium.graves.config.impl.StorageConfig;
import xyz.actrium.graves.death.Death;
import xyz.actrium.graves.storage.GraveStorage;
import xyz.actrium.graves.util.ItemUtils;
import xyz.actrium.graves.util.LocationUtils;

public class GraveSQLStorage implements GraveStorage {
    private Connection con;

    @Override
    public void init(ActriumGraves plugin) {
        StorageConfig storageConfig = plugin.getConfigHandler().getStorageConfig();
        String uri = "jdbc:mysql://" + storageConfig.host + ":" + storageConfig.port + "/" + storageConfig.dbName;

        try {
            if (storageConfig.authed) {
                this.con = DriverManager.getConnection(uri, storageConfig.username, storageConfig.password);
            } else {
                this.con = DriverManager.getConnection(uri);
            }

            String createTable = "CREATE TABLE IF NOT EXISTS graves (location VARCHAR(255) PRIMARY KEY,actualDeathLocation VARCHAR(255),uuid VARCHAR(255),created BIGINT,contents TEXT)";
            Statement statement = this.con.createStatement();
            statement.executeUpdate(createTable);
        } catch (SQLException e) {
            ActriumGraves.get().logger.error("Failed to connect to SQL database. " + e.getErrorCode());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        try {
            if (this.con != null && !this.con.isClosed()) {
                this.con.close();
            }
        } catch (SQLException e) {
            ActriumGraves.get().logger.error("Failed to properly close the SQL database. " + e.getErrorCode());
        }

    }

    @Override
    public List<Death> getGraves() {
        ArrayList<Death> toReturn = new ArrayList<>();

        try {
            Statement statement = this.con.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT location, actualDeathLocation, uuid, created, contents FROM graves");
            while(resultSet.next()) {
                String locString = resultSet.getString("location");
                if (!LocationUtils.worldExists(locString)) {
                    this.remove(locString);
                } else {
                    toReturn.add(new Death(LocationUtils.locationFromString(locString), LocationUtils.locationFromString(resultSet.getString("actualDeathLocation")), UUID.fromString(resultSet.getString("uuid")), resultSet.getLong("created"), ItemUtils.convertStringToitems(resultSet.getString("contents"))));
                }
            }

            statement.close();

            return toReturn;
        } catch (IOException | ClassNotFoundException | SQLException e) {
            ActriumGraves.get().logger.error("Failed to handle to SELECT Sql. ");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void saveGrave(Death data) throws IOException {
        String insertSQL = "INSERT INTO graves (location, actualDeathLocation, uuid, created, contents) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement statement = this.con.prepareStatement(insertSQL);

            statement.setString(1, LocationUtils.locationToString(data.getGraveLocation()));
            statement.setString(2, LocationUtils.locationToString(data.getDeathLocation()));
            statement.setString(3, data.getPlayerId().toString());
            statement.setLong(4, data.getTimeOfDeath());
            statement.setString(5, ItemUtils.convertItemsToString(data.getInventoryContents()));

            int rowAffect = statement.executeUpdate();
            if (rowAffect == 0) {
                ActriumGraves.get().logger.error("Failed to save grave at location " + LocationUtils.locationToString(data.getGraveLocation()));
            }

            statement.close();

        } catch (SQLException e) {
            ActriumGraves.get().logger.error("Failed to handle to INSERT Sql. ");
            e.printStackTrace();
        }
    }

    @Override
    public void removeGrave(Death data) {
        try {
            PreparedStatement statement = this.con.prepareStatement("DELETE FROM graves WHERE location = ?");
            statement.setString(1, LocationUtils.locationToString(data.getGraveLocation()));

            int rowAffect = statement.executeUpdate();
            if (rowAffect == 0) {
                ActriumGraves.get().logger.error("Failed to delete grave at location " + LocationUtils.locationToString(data.getGraveLocation()));
            }

            statement.close();

        } catch (SQLException e) {
            ActriumGraves.get().logger.error("Failed to handle to DELETE Sql. ");
            e.printStackTrace();
        }
    }

    private void remove(String locString) {
        try {
            PreparedStatement statement = this.con.prepareStatement("DELETE FROM graves WHERE location = ?");
            statement.setString(1, locString);

            int rowAffect = statement.executeUpdate();
            if (rowAffect == 0) {
                ActriumGraves.get().logger.error("Failed to delete grave at location " + locString);
            }

            statement.close();

        } catch (SQLException e) {
            ActriumGraves.get().logger.error("Failed to handle to DELETE Sql. ");
            e.printStackTrace();
        }
    }
}
