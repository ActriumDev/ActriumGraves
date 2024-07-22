package xyz.actrium.graves;

import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.actrium.graves.bstats.Metrics;
import xyz.actrium.graves.commands.ActriumGravesCommand;
import xyz.actrium.graves.commands.GraveCommand;
import xyz.actrium.graves.commands.subcommands.AdminDeleteGraveSubCommand;
import xyz.actrium.graves.commands.subcommands.GraveInfoSubCommand;
import xyz.actrium.graves.config.ConfigHandler;
import xyz.actrium.graves.listeners.GraveInteractListener;
import xyz.actrium.graves.listeners.ItemUseListener;
import xyz.actrium.graves.listeners.PlayerDeathListener;
import xyz.actrium.graves.listeners.PlayerJoinListener;
import xyz.actrium.graves.listeners.PlayerRespawnListener;
import xyz.actrium.graves.listeners.block.BlockBreakListeners;
import xyz.actrium.graves.item.ItemManager;
import xyz.actrium.graves.menu.MenuManager;
import xyz.actrium.graves.storage.StorageManager;
import xyz.actrium.graves.update.UpdateChecker;
import xyz.actrium.graves.util.Logger;
import xyz.actrium.graves.workers.GraveWorker;

public class ActriumGraves extends JavaPlugin {

    private static ActriumGraves instance;

    public Logger logger;

    private Metrics metrics;

    private StorageManager storageManager;
    private ConfigHandler configHandler;
    public GraveManager graveManager;
    public ItemManager itemManager;

    public UpdateChecker updateChecker;

    public String version;

    private GraveCommand graveCommand;

    private MenuManager menuManager;

    public static ActriumGraves get() {
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;
        this.logger = new Logger(this);

        this.saveDefaultConfig();
        this.configHandler = new ConfigHandler(this);
        this.configHandler.init();

        this.version = this.getServer().getPluginManager().getPlugin(this.getName()).getDescription().getVersion();
    }

    @Override
    public void onEnable() {
        this.metrics = new Metrics(this, 22721);

        this.configHandler.load(this.logger);
        this.registerManagers();

        PluginManager pman = this.getServer().getPluginManager();
        this.registerPermissions(pman);
        this.registerListeners(pman);
        this.registerCommands();

        new GraveWorker().runTaskTimer(this, 0L, 2L);

        if (!this.updateChecker.isFailed()) {
            this.updateChecker.sendConsoleStatus(this.logger);
        }

        this.metrics.addCustomChart(new Metrics.SingleLineChart("graves", () -> getStorageHandler().getGraveStorage().getGraves().size()));
    }

    @Override
    public void onDisable() {
        this.metrics.shutdown();
        this.storageManager.getGraveStorage().onDisable();
    }

    private void registerManagers() {
        this.storageManager = new StorageManager(this);
        this.updateChecker = new UpdateChecker(this.version);
        this.graveManager = new GraveManager(this);
        this.itemManager = new ItemManager();
        this.menuManager = new MenuManager();
    }

    private void registerPermissions(PluginManager pluginManager) {
        Permissions[] perms = Permissions.values();
        for (Permissions value : perms) {
            pluginManager.addPermission(new Permission(value.asString(), value.getDescription()));
        }

    }

    private void registerListeners(PluginManager pman) {
        pman.registerEvents(new PlayerDeathListener(), this);
        pman.registerEvents(new PlayerRespawnListener(), this);
        pman.registerEvents(new GraveInteractListener(), this);
        pman.registerEvents(new BlockBreakListeners(), this);
        pman.registerEvents(new PlayerJoinListener(this.updateChecker), this);
        pman.registerEvents(new ItemUseListener(), this);
    }

    private void registerCommands() {
        this.getCommand("ActriumGraves").setExecutor(new ActriumGravesCommand());
        this.graveCommand = new GraveCommand();
        this.graveCommand.registerSubCommand(new AdminDeleteGraveSubCommand());
        this.graveCommand.registerSubCommand(new GraveInfoSubCommand());
        this.getCommand("grave").setExecutor(this.graveCommand);
    }

    public ConfigHandler getConfigHandler() {
        return this.configHandler;
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        this.configHandler.load(this.logger);
    }

    @Override
    public void saveConfig() {
        super.saveConfig();
        this.configHandler.load(this.logger);
    }

    public StorageManager getStorageHandler() {
        return storageManager;
    }

    public ItemManager getItemHandler() {
        return itemManager;
    }

    public MenuManager getMenuManager() {
        return menuManager;
    }

    public void disable() {
        this.getServer().getPluginManager().disablePlugin(this);
    }
}
