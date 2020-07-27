package me.ddquin.actionbar;

import me.ddquin.Util;
import net.minecraft.server.v1_9_R2.IChatBaseComponent;
import net.minecraft.server.v1_9_R2.Packet;
import net.minecraft.server.v1_9_R2.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Actionbar_1_9_R2 implements Actionbar {
    @Override
    public void sendActionbar(Player p, String message) {
        final PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + Util.colour(message) + "\"}"), (byte)2);
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket((Packet)packet);
    }
}
