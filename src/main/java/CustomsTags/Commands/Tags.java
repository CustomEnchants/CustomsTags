package CustomsTags.Commands;

import CustomsTags.CustomsTagsPlugin;
import CustomsTags.Objects.User;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Tags implements CommandExecutor {

    private final CustomsTagsPlugin instance = CustomsTagsPlugin.getInstance();

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String lab, String[] args) {
        if (cmd.getName().equalsIgnoreCase("Tags")) {
            if(!(cs instanceof Player)){
                cs.sendMessage(instance.getUtil().fixColour("&b&l(!) &bCustomsTags &cYou must be a player to open the tags menu."));
                return false;
            }
            if(args.length == 0){
                Player p = (Player) cs;
                editInventory(p);
                return false;
            }
            return false;
        }
        return false;
    }

    private void editInventory(Player player){
        User user = instance.getUsers().get(player.getUniqueId());
        Inventory inventory = Bukkit.createInventory(player, instance.getFileUtil().inventorySize, instance.getFileUtil().inventoryName);
        instance.getTags().forEach(tag -> inventory.setItem(tag.getInventorySlot(), tag.getItemStack(player)));
        ItemStack infoItem = instance.getUtil().getInfoItem(user);
        ItemStack removeTagItem = instance.getUtil().getRemoveTagItem(user);
        ItemStack placeHolderItem = instance.getUtil().getPlaceHolderItem(user);
        instance.getFileUtil().infoItemSlots.forEach(slot -> inventory.setItem(slot, infoItem));
        instance.getFileUtil().removeTagItemSlots.forEach(slot -> inventory.setItem(slot, removeTagItem));
        instance.getFileUtil().placeholderItemSlots.forEach(slot -> inventory.setItem(slot, placeHolderItem));
        player.openInventory(inventory);
    }



}
