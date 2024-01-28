package CustomsTags;

import CustomsTags.Commands.CustomsTagsCommand;
import CustomsTags.Commands.Tags;
import CustomsTags.Hooks.PlaceHolderAPIPlaceHolders;
import CustomsTags.Listeners.InventoryListener;
import CustomsTags.Listeners.PlayerListener;
import CustomsTags.Objects.Tag;
import CustomsTags.Objects.User;
import CustomsTags.Utils.FileUtil;
import CustomsTags.Utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class CustomsTagsPlugin extends JavaPlugin {

    private static CustomsTagsPlugin instance;
    private final ArrayList<Tag> tags = new ArrayList<>();
    private FileUtil fileutil;
    private Util util;

    private final HashMap<UUID, User> users = new HashMap<>();

    public static CustomsTagsPlugin getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;
        fileutil = new FileUtil();
        util = new Util();
        getFileUtil().setup(getDataFolder());
        Bukkit.getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getCommand("CustomsTags").setExecutor(new CustomsTagsCommand());
        getCommand("CustomsTags").setTabCompleter(new CustomsTagsCommand());
        getCommand("tags").setExecutor(new Tags());
        new BukkitRunnable() {
            public void run() {
                Bukkit.getServer().getOnlinePlayers().forEach(player -> {
                    User user = new User(player.getUniqueId());
                    user.register();
                    user.load();
                    getUsers().put(player.getUniqueId(), user);
                });
                cancel();
            }
        }.runTaskAsynchronously(this);
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PlaceHolderAPIPlaceHolders().register();
        }
    }

    public void onDisable() {
        instance = null;
        getTags().clear();
        getUsers().clear();
    }

    public FileUtil getFileUtil() {
        return fileutil;
    }

    public HashMap<UUID, User> getUsers() {
        return users;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public Util getUtil() {
        return util;
    }


}
