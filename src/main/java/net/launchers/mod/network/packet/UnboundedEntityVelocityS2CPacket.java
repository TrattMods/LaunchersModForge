package net.launchers.mod.network.packet;
import io.netty.buffer.Unpooled;
import net.launchers.mod.initializer.LNetwork;
import net.launchers.mod.loader.LLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.function.Supplier;

public class UnboundedEntityVelocityS2CPacket
{
    private  Vector3d velocity;
    private  int entityId;
    public UnboundedEntityVelocityS2CPacket(PacketBuffer buffer)
    {
        read(buffer);
    }
    public UnboundedEntityVelocityS2CPacket(int entityId, Vector3d velocity)
    {
        this.velocity = velocity;
        this.entityId = entityId;
    }
    
    public UnboundedEntityVelocityS2CPacket(int entityId, float x, float y, float z)
    {
        this(entityId, new Vector3d(x, y, z));
    }
    
    public UnboundedEntityVelocityS2CPacket(Entity entity, Vector3d velocity)
    {
        this(entity.getId(), velocity);
    }
    
    public UnboundedEntityVelocityS2CPacket(Entity entity, float x, float y, float z)
    {
        this(entity.getId(), new Vector3d(x, y, z));
    }
    
    public void sendTo(PlayerEntity player)
    {
        PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
        write(buf);
        LNetwork.channel.send(PacketDistributor.PLAYER.with(()->(ServerPlayerEntity) player), this);
    }
    
    public boolean handle(Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() ->
        {
            ClientPlayerEntity player = Minecraft.getInstance().player;
            LLoader.LOGGER.info("Entity: "+entityId+", player: "+player.getId());
            Entity targetEntity =  player.level.getEntity(entityId);
            targetEntity.setDeltaMovement(velocity);
        });
        return true;
    }
    
    public void write(PacketBuffer buf)
    {
        buf.writeVarInt(entityId);
        buf.writeDouble(velocity.x);
        buf.writeDouble(velocity.y);
        buf.writeDouble(velocity.z);
    }
    
    public void read(PacketBuffer buf)
    {
        entityId = buf.readVarInt();
        velocity = new Vector3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
    }
}
