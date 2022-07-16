package me.ddquin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StopwatchCommands implements CommandExecutor {

    private StopwatchMain stopwatchMain;

    public StopwatchCommands(StopwatchMain plugin) {
        this.stopwatchMain = plugin;
    }

    public boolean onCommand(CommandSender s, Command command, String label, String[] args) {
        if (!s.hasPermission("stopwatch.admin")) {
            s.sendMessage(ChatColor.RED + "You have no permission!");
            return false;
        }

        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("stop")) {
                stopwatchMain.stopStopwatch(s);
            } else if (args[0].equalsIgnoreCase("start")) {
                stopwatchMain.startStopwatch();
            } else if (args[0].equalsIgnoreCase("pause")) {
                stopwatchMain.pauseStopwatch(s);
            }  else if (args[0].equalsIgnoreCase("show")) {
                stopwatchMain.showStopwatch(s, args);
            }  else if (args[0].equalsIgnoreCase("showall")) {
                stopwatchMain.showAllStopwatch(s);
            } else if (args[0].equalsIgnoreCase("help")) {
                stopwatchMain.showHelp(s);
            } else if (args[0].equalsIgnoreCase("item")) {
                stopwatchMain.getItems(s);
            } else if (args[0].equalsIgnoreCase("offset")) {
                stopwatchMain.offsetSeconds(s, args);
            } else if (args[0].equalsIgnoreCase("message")) {
                stopwatchMain.setMessage(s, args);
            } else if (args[0].equalsIgnoreCase("endsec")) {
                stopwatchMain.setEndSec(s, args);
            } else if (args[0].equalsIgnoreCase("togglems")) {
                stopwatchMain.toggleMs(s, args);
            }
        } else {
            s.sendMessage(ChatColor.RED + "Do /[stopwatch|st] pause/stop/start/help/item/message/offset/endsec/togglems");
        }
        return false;
    }
}
