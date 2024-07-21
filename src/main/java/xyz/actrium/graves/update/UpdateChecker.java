package xyz.actrium.graves.update;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import xyz.actrium.graves.ActriumGraves;
import xyz.actrium.graves.util.CC;
import xyz.actrium.graves.util.Logger;
import xyz.actrium.graves.workers.UpdateWorker;

public class UpdateChecker {
    private boolean outdated = false;
    private boolean failed = false;
    private UpdateWorker worker;
    private String latestVersion;
    private String currentVersion;

    public UpdateChecker(String version) {
        if (ActriumGraves.get().getConfigHandler().checkForUpdates) {
            this.currentVersion = version;
            this.latestVersion = retrieveLatestVersion();
            if (!this.failed) {
                this.outdated = isVersionOutDate();
                this.worker = new UpdateWorker(this);
                this.worker.runTaskTimerAsynchronously(ActriumGraves.get(), 300L, 2400L);
            }
        }
    }

    public void refresh() {
        String version = retrieveLatestVersion();
        if (this.failed) {
            this.latestVersion = this.currentVersion;
            this.outdated = false;
            this.worker.cancel();
            return;
        }

        this.latestVersion = version;
        this.outdated = isVersionOutDate();
        if (this.outdated) {
            this.worker.cancel();
        }
    }

    public void sendConsoleStatus(Logger logger) {
        logger.noPrefix("&r ");
        if (this.outdated) {
            logger.info("&cYou are running a outdated version of ActriumGraves!");
            logger.info("&cLatest version: " + this.latestVersion);
            logger.info("&cYour version: " + this.currentVersion);
        } else {
            logger.info(CC.translate("You are running the latest version (" + this.currentVersion + ") of ActriumGraves!"));
        }
    }

    public String retrieveLatestVersion() {
        try {
            URL url = new URL("https://api.spiget.org/v2/resources/114963/versions/latest?" + System.currentTimeMillis());
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder jsonString = new StringBuilder();

            String line;
            while((line = reader.readLine()) != null) {
                jsonString.append(line);
            }

            reader.close();
            JsonObject infoJson = new Gson().fromJson(jsonString.toString(), JsonObject.class);
            return infoJson.get("name").getAsString();
        } catch (IOException e) {
            this.failed = true;
            ActriumGraves.get().logger.error("Failed to check for a update! Check your internet connection.");
            return null;
        }
    }

    public boolean isVersionOutDate() {
        String[] currentArgs = this.currentVersion.split(".");
        String[] latestArgs = this.latestVersion.split(".");
        boolean returnValue = false;

        for(int i = 0; i < currentArgs.length; ++i) {
            int c = Integer.parseInt(currentArgs[i]);
            int l = Integer.parseInt(latestArgs[i]);
            if (c < l) {
                returnValue = true;
                break;
            }
        }

        return returnValue;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public boolean isOutdated() {
        return outdated;
    }

    public boolean isFailed() {
        return failed;
    }

    public UpdateWorker getWorker() {
        return worker;
    }
}
