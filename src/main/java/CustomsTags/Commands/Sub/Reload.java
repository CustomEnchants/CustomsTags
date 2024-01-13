package CustomsTags.Commands.Sub;

import CustomsTags.Objects.SubCommand;
import org.bukkit.command.CommandSender;

public class Reload extends SubCommand {

    public Reload() {
        super("Reload", "Reload the configuration", "", "CustomsTags.reload");
    }

    @Override
    public void run(CommandSender cs, String[] args) {
        if (!cs.hasPermission(getPermission())) {
            cs.sendMessage(fixColour("&b&l(!) &bCustomsTags &cYou don't have permission to execute this command!"));
            return;
        }
        instance.getFileUtil().reload_tags_reloading.forEach(cs::sendMessage);
        instance.getTags().clear();
        instance.getFileUtil().loadValues(true);
        instance.getFileUtil().reload_tags_reloaded.forEach(cs::sendMessage);
    }
}
