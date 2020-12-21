package me.ddquin;

import me.ddquin.actionbar.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.apache.commons.lang.time.StopWatch;

import java.io.File;
import java.io.IOException;

public class StopwatchMain extends JavaPlugin {

    private boolean isRunning, isPaused;
    private StopWatch stopWatch;
    private File configFile;
    private YamlConfiguration config;
    private String timeMessage;
    private int offsetSec;
    private int endSec = Integer.MAX_VALUE;
    private Actionbar actionbar;

    @Override
    public void onEnable() {
        if (!setupActionbar()) {
            getLogger().severe("Failed to setup Actionbar!");
            getLogger().severe("Your server version is not compatible with this plugin!");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        this.configFile = getConfig("config", this);
        this.reload();
        if (!this.config.isSet("timerMessage")) {
            this.config.set("timerMessage", "&6{0}");
            this.save();
        }
        timeMessage = this.config.getString("timerMessage");
        stopWatch = new StopWatch();

        isRunning = false;
        isPaused = false;
        getCommand("stopwatch").setExecutor(new StopwatchCommands(this));
        Bukkit.getPluginManager().registerEvents(new ItemListener(this), this);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (isRunning) {

                    int totalSecs = (int) (stopWatch.getTime()/1000.0) + offsetSec;

                    int  minutes = (int)((totalSecs % 3600) / 60);
                    int seconds = totalSecs % 60;
                    String time = String.format("%01d:%02d", minutes, seconds);
                    String timerMessage = Util.replace(timeMessage, time);
                    //System.out.println(timerMessage);
                    Bukkit.getOnlinePlayers().forEach(p -> actionbar.sendActionbar(p, timerMessage));
                    if (totalSecs >= endSec) {
                        stopStopwatch(Bukkit.getConsoleSender());
                    }
                }
            }
        }.runTaskTimer(this, 1L, 1L);
    }



    //Command methods

    public void showHelp(CommandSender s) {
        s.sendMessage(ChatColor.GREEN + "/st pause will either pause or unpause the timer\n" +
                "/st start will start the timer and restart if already started\n" +
                "/st stop will stop the timer\n" +
                "/st item will give you items to use the 3 time control commands\n" +
                "/st message [message] will set the message of the time, make sure to include {0} in it which represents time\n" +
                "/st offset [seconds] will add to the offset seconds by the amount given until next stopwatch reset\n" +
                "/st endsec [seconds] will set the end seconds to the amount given so that the timer will stop at that time, must be set after every reset");
    }

    public void startStopwatch() {
        stopWatch.reset();
        offsetSec = 0;
        endSec = Integer.MAX_VALUE;
        //System.out.println("started");
        stopWatch.start();
        isRunning = true;
        isPaused = false;
    }

    public void stopStopwatch(CommandSender s) {
        if (!isRunning) {
            s.sendMessage(ChatColor.RED + "Stopwatch already stopped!");

        } else {
           // System.out.println("stopped");
            stopWatch.stop();
            isRunning = false;
            isPaused = false;
        }
    }

    public void pauseStopwatch(CommandSender s) {
        if (!isRunning) {
            s.sendMessage(ChatColor.RED + "The timer isn't running at the moment!");
        } else {
            if (isPaused) {
                stopWatch.resume();
                //System.out.println("un paused");
            } else {
                stopWatch.suspend();
                //System.out.println("paused");
            }
            isPaused = !isPaused;
        }
    }

    public void getItems(CommandSender s) {
        if (!(s instanceof Player)) {
            s.sendMessage(ChatColor.RED + "Only players can use items!");
            return;
        }
        Player p = (Player) s;
        p.getInventory().setItem(0, ItemListener.getStartItem());
        p.getInventory().setItem(1, ItemListener.getStopItem());
        p.getInventory().setItem(2, ItemListener.getPauseItem());

    }

    public void setMessage(CommandSender s, String[] args) {
        if (args.length == 1) {
            s.sendMessage(ChatColor.RED + "Specify a message, like this /st message &6Time: {0}");
            return;
        }

        String message = args[1];

        if (args.length > 2) {
            for (int i = 2; i < args.length; i++) {
                message = message + " " + args[i];
            }
        }

        if (!Util.isPlaceholderInMessage(message)) {
            s.sendMessage(ChatColor.RED + "The message " + message + " must contain {0}!");
            return;
        }
        timeMessage = message;
        this.config.set("timerMessage", message);
        s.sendMessage("Timer message set to " + Util.replace(Util.colour(timeMessage), "20"));
        this.save();
    }

    public void offsetSeconds (CommandSender s, String[] args) {
        if (args.length == 1) {
            s.sendMessage(ChatColor.RED + "Specify offset seconds like 40 or -50, the offset seconds go back to 0 once timer is reset, current offset is " + offsetSec);
            return;
        }
        try {
            int offsetArg = Integer.parseInt(args[1]);
            offsetSec += offsetArg;
            s.sendMessage(ChatColor.GREEN + "Successfully offset by " + offsetArg +" current offset now is " + offsetSec);
        } catch (IllegalArgumentException e) {
            s.sendMessage(ChatColor.RED + "Please input an integer");
            return;
        }
    }

    public void setEndSec (CommandSender s, String[] args) {
        if (args.length == 1) {
            s.sendMessage(ChatColor.RED + "Specify end seconds like 40, current end seconds are " + endSec);
            return;
        }
        try {
            int offsetArg = Integer.parseInt(args[1]);
            endSec = offsetArg;
            s.sendMessage(ChatColor.GREEN + "Successfully set end seconds to " + endSec);
        } catch (IllegalArgumentException e) {
            s.sendMessage(ChatColor.RED + "Please input an integer");
            return;
        }
    }

    //Config methods

    public void reload() {
        this.config = YamlConfiguration.loadConfiguration(this.configFile);
    }

    public void save() {
        try {
            this.config.save(this.configFile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File getConfig(final String name, final Plugin plugin) {
        final File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        final File configFile = new File(plugin.getDataFolder() + File.separator + name + ".yml");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return configFile;
    }

    private boolean setupActionbar() {
        String version;
        try {
            version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

        } catch (ArrayIndexOutOfBoundsException whatVersionAreYouUsingException) {
            return false;
        }
        getLogger().info("Your server is running version " + version);
        if (version.equals("v1_7_R4")) {

        } else if (version.equals("v1_8_R1")) {
            //server is running 1.8-1.8.1 so we need to use the 1.8 R1 NMS class
            actionbar = new Actionbar_1_8_R1();
        } else if (version.equals("v1_8_R2")) {
            //server is running 1.8.3 so we need to use the 1.8 R2 NMS class
            actionbar = new Actionbar_1_8_R2();
        } else if (version.equals("v1_8_R3")) {
            //server is running 1.8.4 - 1.8.8 so we need to use the 1.8 R3 NMS class
            actionbar = new Actionbar_1_8_R3();
        } else if (version.equals("v1_9_R1")) {
            //server is running 1.9 - 1.9.2 so we need to use the 1.9 R1 NMS class
            actionbar = new Actionbar_1_9_R1();
        } else if (version.equals("v1_9_R2")) {
            //server is running 1.9.4 so we need to use the 1.9 R2 NMS class
            actionbar = new Actionbar_1_9_R2();
        } else  {
            
            //  we are running 1.10+ where you can use ChatMessageType
            actionbar = new ActionbarModern();
        }
        return actionbar != null;

    }



}
