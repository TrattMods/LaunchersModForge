package net.launchers.mod.network.packet;

import io.netty.buffer.Unpooled;
import net.launchers.mod.initializer.LNetwork;
import net.launchers.mod.loader.LLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class UnboundedEntityVelocityS2CPacket {
    private Vec3 velocity;
    private int entityId;

    public UnboundedEntityVelocityS2CPacket(FriendlyByteBuf buffer) {
        read(buffer);
    }

    public UnboundedEntityVelocityS2CPacket(int entityId, Vec3 velocity) {
        this.velocity = velocity;
        this.entityId = entityId;
    }

    public void sendTo(Player player) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        write(buf);
        LNetwork.channel.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), this);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            LocalPlayer player = Minecraft.getInstance().player;
            assert player != null;
            LLoader.LOGGER.info("Entity: " + entityId + ", player: " + player.getId());
            Entity targetEntity = player.level().getEntity(entityId);
            assert targetEntity != null;
            targetEntity.setDeltaMovement(velocity);
        });
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeDouble(velocity.x);
        buf.writeDouble(velocity.y);
        buf.writeDouble(velocity.z);
    }

    public void read(FriendlyByteBuf buf) {
        entityId = buf.readInt();
        velocity = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
    }
}
