package xyz.actrium.graves.workers;

import org.bukkit.scheduler.BukkitRunnable;
import xyz.actrium.graves.update.UpdateChecker;

public class UpdateWorker extends BukkitRunnable {
    private final UpdateChecker updateChecker;

    public UpdateWorker(UpdateChecker updateChecker) {
        this.updateChecker = updateChecker;
    }

    @Override
    public void run() {
        this.updateChecker.refresh();
    }
}
