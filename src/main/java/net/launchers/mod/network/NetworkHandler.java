package net.launchers.mod.network;

import net.launchers.mod.loader.LLoader;
import net.launchers.mod.network.packet.UnboundedEntityVelocityS2CPacket;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;


import java.util.List;

public class NetworkHandler
{
    public static void sendToAll(UnboundedEntityVelocityS2CPacket packet, PlayerList manager)
    {
        List<ServerPlayer> targets = manager.getPlayers();
        for (ServerPlayer target : targets) {
            LLoader.LOGGER.info(target.getName().getString());
            packet.sendTo(target);
        }
    }
}
