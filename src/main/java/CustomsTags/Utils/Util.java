package CustomsTags.Utils;

import CustomsTags.CustomsTagsPlugin;
import CustomsTags.Objects.User;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Util {

    public String fixColour(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    private final CustomsTagsPlugin instance = CustomsTagsPlugin.getInstance();


    public ItemStack getPlaceHolderItem(User user) {
        return getItemStack(user, instance.getFileUtil().placeHolderItem.clone());
    }

    private ItemStack getItemStack(User user, ItemStack itemStack) {
        ArrayList<String> lore = itemStack.getItemMeta().hasLore() ? (ArrayList<String>) itemStack.getItemMeta().getLore() : new ArrayList<>();
        ArrayList<String> newLore = new ArrayList<>();
        for (String string : lore) {
            if (string.contains("%customstags_tag%")) {
                string = string.replace("%customstags_tag%", user.getActiveTag());
            }
            newLore.add(string);
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(itemMeta.getDisplayName().replace("%customstags_tag%", user.getActiveTag()));
        itemMeta.setLore(newLore);
        return itemStack;
    }

    public ItemStack getRemoveTagItem(User user) {
        return getItemStack(user, instance.getFileUtil().removeTagItem.clone());
    }

    public ItemStack getInfoItem(User user) {
        return getItemStack(user, instance.getFileUtil().infoItem.clone());
    }

}
