package me.ddquin.actionbar;

import me.ddquin.Util;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player.Spigot;

import org.bukkit.entity.Player;

public class ActionbarModern implements Actionbar {
    @Override
    public void sendActionbar(Player p, String message) {
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Util.colour(message)));
    }
}
