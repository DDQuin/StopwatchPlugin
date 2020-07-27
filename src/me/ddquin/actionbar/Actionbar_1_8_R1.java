package me.ddquin.actionbar;

import me.ddquin.Util;
import me.ddquin.actionbar.Actionbar;
import net.minecraft.server.v1_8_R1.*;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Actionbar_1_8_R1 implements Actionbar {
    @Override
    public void sendActionbar(Player p, String message) {

        final PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(Util.colour(message)), (byte)2);
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket((Packet)packet);
    }
}
