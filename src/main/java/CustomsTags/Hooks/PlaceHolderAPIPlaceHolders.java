package CustomsTags.Hooks;

import CustomsTags.CustomsTagsPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class PlaceHolderAPIPlaceHolders extends PlaceholderExpansion {

    private final CustomsTagsPlugin instance = CustomsTagsPlugin.getInstance();

    // This is the most important method when registering an expansion from your plugin
    public boolean persist() {
// This tells PlaceholderAPI to not unregister this hook when /papi reload is executed
        return true;
    }

    // The identifier, shouldn't contain any _ or %
    public String getIdentifier() {
        return "customstags";
    }

    @SuppressWarnings("deprecation")
    public String getPlugin() {
        return "CustomsTags";
    }

    // The author of the placeholder. This cannot be null
    public String getAuthor() {
        return "CustomEnchants";
    }

    // Same with #getAuthor() but for version
    public String getVersion() {
        return "3.0";
    }

    // Use this method to set up placeholders. This is somewhat similar to EZPlaceholderHook
    public String onPlaceholderRequest(Player p, String identifier) {
        return "tag".equals(identifier) ? instance.getUsers().get(p.getUniqueId()).getActiveTag() : "";
    }


}