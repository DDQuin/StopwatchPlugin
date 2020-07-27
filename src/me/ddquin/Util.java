package me.ddquin;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {



    public static String colour(final String s) {
        return (s != null) ? ChatColor.translateAlternateColorCodes('&', s) : "";
    }

    public static boolean isPlaceholderInMessage(String message) {
        Pattern pattern = Pattern.compile("\\{0}");
        Matcher matcher = pattern.matcher(message);
        return matcher.find();

    }


    public static String replace(String message, String... replaces) {
        for (int i = 0; i < replaces.length; i++) {
            message = message.replaceAll("\\{" + i + "}", replaces[i]);
        }
        return message;
    }
}
