package me.ddquin.actionbar;

import me.ddquin.Util;
import net.minecraft.server.v1_9_R1.IChatBaseComponent;
import net.minecraft.server.v1_9_R1.Packet;
import net.minecraft.server.v1_9_R1.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Actionbar_1_9_R1 implements Actionbar {
    @Override
    public void sendActionbar(Player p, String message) {
        final PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + Util.colour(message) + "\"}"), (byte)2);
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket((Packet)packet);
    }
}
