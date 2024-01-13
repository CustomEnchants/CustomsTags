package CustomsTags.Listeners;

import CustomsTags.CustomsTagsPlugin;
import CustomsTags.Objects.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerListener implements Listener {

    private final CustomsTagsPlugin instance = CustomsTagsPlugin.getInstance();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        new BukkitRunnable() {
            public void run() {
                User user = new User(event.getPlayer().getUniqueId());
                user.register();
                user.load();
                instance.getUsers().put(event.getPlayer().getUniqueId(), user);
                cancel();
            }
        }.runTaskAsynchronously(instance);
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        if (!instance.getUsers().containsKey(event.getPlayer().getUniqueId())) return;
        instance.getUsers().remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        if (!instance.getUsers().containsKey(event.getPlayer().getUniqueId())) return;
        instance.getUsers().remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (!event.getFormat().contains("[TAG]")) return;
        event.setFormat(event.getFormat().replace("[TAG]", instance.getUsers().get(event.getPlayer().getUniqueId()).getActiveTag()));
    }


}
