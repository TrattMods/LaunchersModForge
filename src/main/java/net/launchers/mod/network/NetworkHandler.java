package net.launchers.mod.network;

import net.launchers.mod.loader.LLoader;
import net.launchers.mod.network.packet.UnboundedEntityVelocityS2CPacket;
import net.minecraft.entity.player.ServerPlayerEntity;

import net.minecraft.server.management.PlayerList;


import java.util.List;

public class NetworkHandler
{
    public static void sendToAll(UnboundedEntityVelocityS2CPacket packet, PlayerList manager)
    {
        List<ServerPlayerEntity> targets = manager.getPlayers();
        for(int i = 0; i < targets.size(); ++i)
        {
            LLoader.LOGGER.info(targets.get(i).getName().getString());
            packet.sendTo(targets.get(i));
        }
    }
}
