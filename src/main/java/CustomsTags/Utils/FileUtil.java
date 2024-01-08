package CustomsTags.Utils;

import CustomsTags.CustomsTagsPlugin;
import CustomsTags.MySQL.SQLite;
import CustomsTags.Objects.Tag;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FileUtil {


    public SQLite database;
    public File conf;
    public File language;
    public File tags;

    public String inventoryName;
    public int inventorySize;

    public ArrayList<String> activated_tag = new ArrayList<>();
    public ArrayList<String> deactivated_tag = new ArrayList<>();

    public ArrayList<String> reload_tags_reloading = new ArrayList<>();
    public ArrayList<String> reload_tags_reloaded = new ArrayList<>();

    public ArrayList<Integer> removeTagItemSlots = new ArrayList<>();
    public ArrayList<Integer> placeholderItemSlots = new ArrayList<>();
    public ArrayList<Integer> infoItemSlots = new ArrayList<>();

    public ItemStack infoItem;
    public ItemStack placeHolderItem;
    public ItemStack removeTagItem;

    private final CustomsTagsPlugin instance = CustomsTagsPlugin.getInstance();

    public String fixColour(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public ArrayList<String> fixColours(List<String> input) {
        if (input.size() == 0) return new ArrayList<>();
        ArrayList<String> result = new ArrayList<>();
        input.forEach(string -> result.add(fixColour(string)));
        return result;
    }

    private void loadDataBase() {
        File db = new File(instance.getDataFolder() + File.separator + "Database.db");
        if (!db.exists()) {
            try {
                db.createNewFile();
            } catch (IOException e) {
                System.out.println(e.getLocalizedMessage());
                return;
            }
        }
        try {
            database = new SQLite(db);
            database.openConnection();
            database.setupDataTable();
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getLocalizedMessage());
            return;
        }
        System.out.println("Successfully loaded SQLite database");
    }

    public void saveTag(Tag tag) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(tags);
        int size = config.getKeys(false).size() + 1;
        config.set(size + ".name", tag.name);
        config.set(size + ".chat", tag.chat);
        config.set(size + ".permission", tag.getPermission());
        config.set(size + ".unlocked",tag.getUnlockedItemStack());
        config.set(size + ".locked",tag.getLockedItemStack());
        config.set(size + ".inventorySlot", tag.getInventorySlot());
        config.set(size +".page",tag.getPage());
        try {
            config.save(tags);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadTags() {
        FileConfiguration config = YamlConfiguration.loadConfiguration(tags);
        for (String string : config.getKeys(false)) {
            ConfigurationSection cs = config.getConfigurationSection(string);
            String name = cs.getString("name");
            String chat = fixColour(cs.getString("chat"));
            String permission = cs.getString("permission");
            ItemStack unlocked = cs.getItemStack("unlocked");
            ItemStack locked = cs.getItemStack("locked");
            int inventorySlot = cs.getInt("inventorySlot");
            int page = cs.getInt("page");
            Tag tag = new Tag(name, chat, permission, unlocked, locked, inventorySlot,page);
            instance.getTags().add(tag);
        }
    }

    public void loadValues() {
        FileConfiguration config = YamlConfiguration.loadConfiguration(conf);
        inventoryName = fixColour(config.getString("inventory.name"));
        inventorySize = config.getInt("inventory.size");

        placeHolderItem = config.getItemStack("placeHolderItem.item");
        infoItem = config.getItemStack("infoItem.item");
        removeTagItem = config.getItemStack("removeTagItem.item");

        placeholderItemSlots = (ArrayList<Integer>) config.getIntegerList("placeHolderItem.itemSlots");
        infoItemSlots = (ArrayList<Integer>) config.getIntegerList("infoItem.itemSlots");
        removeTagItemSlots = (ArrayList<Integer>) config.getIntegerList("removeTagItem.itemSlots");


        FileConfiguration config2 = YamlConfiguration.loadConfiguration(language);
        activated_tag = fixColours(config2.getStringList("Tag.Activated"));
        deactivated_tag = fixColours(config2.getStringList("Tag.Deactivated"));
        reload_tags_reloading = fixColours(config2.getStringList("Reload-Tags.Reloading"));
        reload_tags_reloaded = fixColours(config2.getStringList("Reload-Tags.Reloaded"));
    }

    public void setup(File dir) {
        if (!dir.exists()) {
            dir.mkdirs();
        }
        conf = new File(dir + File.separator + "Config.yml");
        if (!conf.exists()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(conf);
            config.set("Author", "CustomEnchants");
            config.set("inventory.name", "&l» &rSelect a tag..");
            config.set("inventory.size", 54);

            config.set("placeHolderItem.item", getDefaultPlaceHolderItem());
            config.set("placeHolderItem.itemSlots", Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 46, 47, 48, 50, 51, 52));

            config.set("infoItem.itemSlots", Collections.singletonList(49));
            config.set("infoItem.item",getDefaultInfoItem());

            config.set("removeTagItem.item", getRemoveTagItem());
            config.set("removeTagItem.itemSlots", Arrays.asList(45,53));
            try {
                config.save(conf);
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
        tags = new File(dir + File.separator + "Tags.yml");
        if (!tags.exists()) {
            try {
                tags.createNewFile();
                saveTag(new Tag("1738", "&r &7[&b&l1738&7]", "tagsgui.tags.1738", createTagItem("1738","&r &7[&b&l1738&7]",false), createTagItem("1738","&r &7[&b&l1738&7]",true), 10,1));
                saveTag(new Tag("Netflix", "&r &7[&f&k||&c&lNetflix&f&k||&7]", "tagsgui.tags.netflix", createTagItem("Netflix","&r &7[&f&k||&c&lNetflix&f&k||&7]",false), createTagItem("Netflix","&r &7[&f&k||&c&lNetflix&f&k||&7]",true), 11,1));
                saveTag(new Tag("Salty", "&r &7[&f&k||&b&lSalty&f&k||&7]", "tagsgui.tags.salty", createTagItem("Salty","&r &7[&f&k||&b&lSalty&f&k||&7]",false), createTagItem("Salty","&r &7[&f&k||&b&lSalty&f&k||&7]",true), 12,1));
                saveTag(new Tag("Trashy", "&r &7[&a&k||&2&lTRASHY&a&k||&7]", "tagsgui.tags.trashy", createTagItem("Trashy","&r &7[&a&k||&2&lTRASHY&a&k||&7]",false), createTagItem("Trashy","&r &7[&a&k||&2&lTRASHY&a&k||&7]",true), 13,1));
                saveTag(new Tag("1v1", "&r &7[&c&k||&4&l1v1&c&k||&7]", "tagsgui.tags.1v1", createTagItem("1v1","&r &7[&c&k||&4&l1v1&c&k||&7]",false), createTagItem("1v1","&r &7[&c&k||&4&l1v1&c&k||&7]",true), 14,1));
                saveTag(new Tag("Launch", "&r &c[&4&k||&e&lLAUNCH&4&k||&c]", "tagsgui.tags.launch", createTagItem("Launch","&r &c[&4&k||&e&lLAUNCH&4&k||&c]",false), createTagItem("Launch","&r &c[&4&k||&e&lLAUNCH&4&k||&c]",true), 15,1));
                saveTag(new Tag("Fam", "&r &c[&4&k||&2Fam&4&k||&c]", "tagsgui.tags.fam", createTagItem("Fam","&r &c[&4&k||&2Fam&4&k||&c]",false), createTagItem("Fam","&r &c[&4&k||&2Fam&4&k||&c]",true), 16,1));
                saveTag(new Tag("MVP", "&r &c[&4&k||&b&lMVP&4&k||&c]", "tagsgui.tags.mvp", createTagItem("MVP","&r &c[&4&k||&b&lMVP&4&k||&c]",false), createTagItem("MVP","&r &c[&4&k||&b&lMVP&4&k||&c]",true), 19,1));
                saveTag(new Tag("Raider", "&r &c[&4&k||&b&lRaider&4&k||&c]", "tagsgui.tags.raider", createTagItem("Raider","&r &c[&4&k||&b&lRaider&4&k||&c]",false), createTagItem("Raider","&r &c[&4&k||&b&lRaider&4&k||&c]",true), 20,1));
                saveTag(new Tag("RipGolems", "&r &9[&5&k||&c&lRip&e&lGolems&5&k||&9]", "tagsgui.tags.ripgolems", createTagItem("RipGolems","&r &9[&5&k||&c&lRip&e&lGolems&5&k||&9]",false), createTagItem("RipGolems","&r &9[&5&k||&c&lRip&e&lGolems&5&k||&9]",true), 21,1));
                saveTag(new Tag("Legendary", "&r &0[&c&l&k1&e&lLegendary&c&l&k1&0]&r", "tagsgui.tags.legendary", createTagItem("Legendary","&r &0[&c&l&k1&e&lLegendary&c&l&k1&0]&r",false), createTagItem("Legendary","&r &0[&c&l&k1&e&lLegendary&c&l&k1&0]&r",true), 22,1));
                saveTag(new Tag("MoneyMan", "&r &8&l[&b&lMoney &e&lMan&8&l]&r", "tagsgui.tags.moneyman", createTagItem("MoneyMan","&r &8&l[&b&lMoney &e&lMan&8&l]&r",false), createTagItem("MoneyMan","&r &8&l[&b&lMoney &e&lMan&8&l]&r",true), 23,1));
                saveTag(new Tag("CannonGod", "&r &8&l[&bCannon&aGod&8&l]&r", "tagsgui.tags.cannongod", createTagItem("CannonGod","&r &8&l[&bCannon&aGod&8&l]&r",false), createTagItem("CannonGod","&r &8&l[&bCannon&aGod&8&l]&r",true), 24,1));
                saveTag(new Tag("Hacker", "&r &8&l[&4&l&k1&b&lHacker&4&l&k1&8&l]&r", "tagsgui.tags.hacker", createTagItem("Hacker","&r &8&l[&4&l&k1&b&lHacker&4&l&k1&8&l]&r",false), createTagItem("Hacker","&r &8&l[&4&l&k1&b&lHacker&4&l&k1&8&l]&r",true), 25,1));
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
        language = new File(dir + File.separator + "Language.yml");
        if (!language.exists()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(language);
            config.set("Tag.Activated", Collections.singletonList("&b&l(!) &bCustomsTags &bYou have activated your tag %chat%"));
            config.set("Tag.Deactivated", Collections.singletonList("&b&l(!) &bCustomsTags &bYou have deactivated your tag !"));

            config.set("Reload-Tags.Reloading", Collections.singletonList("&b&l(!) &bCustomsTags &aReloading tags"));
            config.set("Reload-Tags.Reloaded", Collections.singletonList("&b&l(!) &bCustomsTags &aReloaded all Tags"));
            try {
                config.save(language);
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
        loadValues();
        loadTags();
        loadDataBase();
    }

    private ItemStack createTagItem(String tagName,String chat,boolean locked){
        ItemStack itemStack = locked ? new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)15) : new ItemStack(Material.NAME_TAG);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(fixColour("&b&n"+tagName));
        ArrayList<String> newLore = new ArrayList<>();
        newLore.add(" ");
        newLore.add(locked ? fixColour("&7You have not unlocked this tag") : fixColour("&7You have &2&lunlocked &7this tag"));
        newLore.add(fixColour("&7Preview: &r"+chat));
        if(locked){
            newLore.add(fixColour("&7Purchase this tag at &c&DonationLink"));
        }
        itemMeta.setLore(newLore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack getDefaultInfoItem(){
        ItemStack itemStack = new ItemStack(Material.BOOK);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(fixColour("&c&l&nInfo"));
        itemMeta.setLore(fixColours(Arrays.asList("&8&l&m-------------------------------", "&aYou can purchase tags in the TokenShop", "&cShop.&6YourServer.net", "&8&l&m-------------------------------")));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack getRemoveTagItem(){
        ItemStack itemStack = new ItemStack(Material.CAULDRON_ITEM);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(fixColour("&c&l&nRemove Tag"));
        itemMeta.setLore(fixColours(Arrays.asList("&8&l&m-------------------------------", "&bClick this to deactivate your tag", "&8&l&m-------------------------------")));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack getDefaultPlaceHolderItem(){
        ItemStack itemStack = new ItemStack(Material.STAINED_GLASS_PANE,1,(short)15);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(fixColour("&c"));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
