package net.launchers.mod.entity.abstraction;

import com.mojang.math.Constants;
import net.launchers.mod.initializer.LEntities;
import net.minecraft.client.renderer.FaceInfo;
import net.minecraft.client.renderer.texture.Tickable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class AbstractLauncherBlockTileEntity extends BlockEntity implements Tickable
{
    private final VoxelShape RETRACTED_BASE_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private final VoxelShape EXTENDED_BASE_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);
    private final VoxelShape SHORT_EXTENDER_SHAPE = Block.box(6.0D, 2.0D, 6.0D, 10.0D, 10.0D, 10.0D);
    private final VoxelShape LONG_EXTENDER_SHAPE = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D);
    private final VoxelShape HEAD_SHAPE = Block.box(0.0D, 12.0D, 0.0D, 16.0D, 16.0D, 16.0D);

    public enum LauncherState
    {EXTENDED, RETRACTED, MOVING}

    public LauncherState[] states;

    private float maxExtendCoefficient;
    private float progress;
    private float lastProgress;
    private boolean extending = true; // true if its extending, false if retracting

    protected int currentTick = 0;

    public LauncherState launcherState;
    public AbstractLauncherBlockTileEntity(BlockEntityType type, BlockPos pos, BlockState state)
    {
        super(type,pos,state);
        states = LauncherState.values();
        launcherState = LauncherState.RETRACTED;
    }
    public boolean isRetracted()
    {
        return launcherState == LauncherState.RETRACTED;
    }
    @Override
    public void tick()
    {
        int retractingDelay = 2;
        // 1/stride ticks per move
        float extensionStride = 1F;
        switch(launcherState)
        {
            case EXTENDED:
                currentTick++;
                if(currentTick >= retractingDelay)
                {
                    currentTick = 0;
                    startRetracting();
                }
                break;
            case RETRACTED:
                break;
            case MOVING:
                this.lastProgress = this.progress;
                if(extending)
                {
                    if(this.lastProgress >= 1.0F)
                    {
                        launcherState = LauncherState.EXTENDED;
                        this.lastProgress = 1F;
                    }
                    else
                    {
                        this.progress += extensionStride;
                        if(this.progress >= 1.0F)
                        {
                            this.progress = 1.0F;
                        }
                    }
                }
                else
                {
                    if(this.lastProgress <= 0F)
                    {
                        launcherState = LauncherState.RETRACTED;
                        lastProgress = 0F;
                    }
                    else
                    {
                        float retractingStride = extensionStride / 4;
                        this.progress -= retractingStride;
                        if(this.progress <= 0F)
                        {
                            this.progress = 0F;
                        }
                    }
                }
                break;
        }
        if(!level.isClientSide && lastProgress != progress)
        {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(),1);
        }
        //LLoader.LOGGER.info("State: "+launcherState+", progr: "+progress);
    }
    public void startExtending()
    {
        extending = true;
        launcherState = LauncherState.MOVING;
        progress = 0;
    }

    public void startRetracting()
    {
        extending = false;
        launcherState = LauncherState.MOVING;
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        nbt.putInt("currentTick", currentTick);
        nbt.putFloat("progress", progress);
        nbt.putBoolean("extending", extending);
        nbt.putInt("launcherState", launcherState.ordinal());
        return nbt;
    }


    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag tag = pkt.getTag();
        handleUpdateTag(tag);
    }


    @Override
    public void handleUpdateTag(CompoundTag tag)
    {
        currentTick = tag.getInt("currentTick");
        progress = tag.getFloat("progress");
        extending = tag.getBoolean("extending");
        launcherState = states[tag.getInt("launcherState")];
        super.handleUpdateTag(tag);
    }


    double lerp(float a, float b, float f)
    {
        return a * (1.0 - f) + (b * f);
    }
    public float getDeltaProgress(float tickDelta)
    {
        if(tickDelta > 1.0F)
        {
            tickDelta = 1.0F;
        }
        return (float) lerp(tickDelta, this.lastProgress, this.progress);
    }
}
