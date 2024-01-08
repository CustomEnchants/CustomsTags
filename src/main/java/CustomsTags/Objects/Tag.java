package CustomsTags.Objects;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Tag {

    public final String name;
    public final String chat;
    public final ItemStack locked;
    public final ItemStack unlocked;
    private final String permission;

    private final int inventorySlot;
    private final int page;

    public Tag(String name, String chat, String permission, ItemStack locked, ItemStack unlocked, int inventorySlot,int page) {
        this.name = name;
        this.chat = chat;
        this.permission = permission;
        this.locked = locked;
        this.unlocked = unlocked;
        this.inventorySlot = inventorySlot;
        this.page = page;
    }

    public ItemStack getItemStack(Player p) {
        return p.hasPermission(getPermission()) ? getUnlockedItemStack() : getLockedItemStack();
    }

    public ItemStack getUnlockedItemStack(){
        return unlocked;
    }

    public ItemStack getLockedItemStack(){
        return locked;
    }

    public String getPermission() {
        return permission;
    }

    public int getInventorySlot(){
        return inventorySlot;
    }

    public int getPage(){
        return page;
    }

}
