package CustomsTags.Listeners;

import CustomsTags.CustomsTagsPlugin;
import CustomsTags.Objects.Tag;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Optional;

public class InventoryListener implements Listener {

    private final CustomsTagsPlugin instance = CustomsTagsPlugin.getInstance();

    @EventHandler
    public void onClick(final InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (event.getClick() == null) return;
        if (event.getWhoClicked() == null) return;
        if (!event.getView().getTitle().equalsIgnoreCase(instance.getFileUtil().inventoryName)) return;
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        if (event.getCurrentItem().isSimilar(instance.getFileUtil().removeTagItem)) {
            instance.getUsers().get(player.getUniqueId()).setActiveTag("");
            instance.getFileUtil().database.update(player.getUniqueId().toString(), "");
            instance.getFileUtil().deactivated_tag.forEach(string -> player.spigot().sendMessage(TextComponent.fromLegacyText(string)));
            player.closeInventory();
            return;
        }
        Optional<Tag> tagOptional = instance.getTags().stream().filter(tag -> event.getCurrentItem().isSimilar(tag.getUnlockedItemStack()) || event.getCurrentItem().isSimilar(tag.getLockedItemStack())).findFirst();
        if (!tagOptional.isPresent()) {
            return;
        }
        if (!player.hasPermission(tagOptional.get().getPermission())) return;
        instance.getUsers().get(player.getUniqueId()).setActiveTag(tagOptional.get().chat);
        instance.getFileUtil().database.update(player.getUniqueId().toString(), tagOptional.get().chat);
        instance.getFileUtil().activated_tag.forEach(string -> player.spigot().sendMessage(TextComponent.fromLegacyText(string.replace("%chat%", tagOptional.get().chat))));
        player.closeInventory();
    }


}
