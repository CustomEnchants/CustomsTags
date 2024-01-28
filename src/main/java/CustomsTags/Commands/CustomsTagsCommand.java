package CustomsTags.Commands;

import CustomsTags.Commands.Sub.Reload;
import CustomsTags.CustomsTagsPlugin;
import CustomsTags.Objects.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class CustomsTagsCommand implements CommandExecutor, TabCompleter {

    private final CustomsTagsPlugin instance = CustomsTagsPlugin.getInstance();
    private final ArrayList<SubCommand> subCommands = new ArrayList<>();

    public CustomsTagsCommand() {
        subCommands.add(new Reload());
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String s, String[] args) {
        if (cmd.getName().equalsIgnoreCase("CustomsTags")) {
            if (args.length == 0) {
                sendHelp(cs, cmd, 1);
                return false;
            }
            if (args[0].equalsIgnoreCase("help")) {
                int page = 1;
                if (args.length >= 2) {
                    try {
                        page = Integer.parseInt(args[1]);
                    } catch (NumberFormatException ignored) {
                    }
                }
                sendHelp(cs, cmd, page);
                return false;
            }
            ArrayList<String> a = new ArrayList<>(Arrays.asList(args));
            a.remove(0);
            Optional<SubCommand> subCommandOptional = subCommands.stream().filter(subCommand -> subCommand.getName().equalsIgnoreCase(args[0])).findFirst();
            if (!subCommandOptional.isPresent()) {
                sendHelp(cs, cmd, 1);
            } else {
                subCommandOptional.get().run(cs, a.toArray(new String[0]));
            }
            return false;
        }
        return false;
    }

    private void sendHelp(CommandSender cs, Command cmd, int page) {
        new BukkitRunnable() {
            public void run() {
                cs.sendMessage(instance.getUtil().fixColour("&6____________.[ &2CustomsTags Help Page &c%page% &6].____________".replace("%page%", "" + page)));
                int perPage = 11;
                int maxPage = subCommands.isEmpty() ? 1 : Math.max((int) Math.ceil((double) subCommands.size() / perPage), 1);
                int actualPage = Math.min(page, maxPage);
                int min = actualPage == 1 ? 0 : actualPage * perPage - perPage;
                int max = actualPage == 1 ? perPage : min + perPage;
                ArrayList<SubCommand> subCommandsForPlayer = subCommands.stream().filter(subCommand -> subCommand.hasAccess(cs)).collect(Collectors.toCollection(ArrayList::new));
                for (int i = min; i < max; i++) {
                    if (subCommandsForPlayer.size() <= i) break;
                    SubCommand c = subCommandsForPlayer.get(i);
                    cs.sendMessage(instance.getUtil().fixColour("&b/" + cmd.getName() + " &7" + c.getName() + " &7" + c.getArgs()));
                }
                cancel();
            }
        }.runTaskAsynchronously(instance);
    }

    @Override
    public java.util.List<String> onTabComplete(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("CustomsTags")) {
            return subCommands.stream().filter(subCommand -> subCommand.hasAccess(cs)).map(SubCommand::getName).collect(Collectors.toCollection(ArrayList::new));
        }
        return null;
    }
}
