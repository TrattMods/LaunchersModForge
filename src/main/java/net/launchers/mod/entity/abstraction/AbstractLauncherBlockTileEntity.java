package net.launchers.mod.entity.abstraction;

import jdk.nashorn.internal.codegen.CompileUnit;
import net.launchers.mod.loader.LLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public class AbstractLauncherBlockTileEntity extends TileEntity implements ITickableTileEntity
{
    private final VoxelShape RETRACTED_BASE_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private final VoxelShape EXTENDED_BASE_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);
    private final VoxelShape SHORT_EXTENDER_SHAPE = Block.box(6.0D, 2.0D, 6.0D, 10.0D, 10.0D, 10.0D);
    private final VoxelShape LONG_EXTENDER_SHAPE = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D);
    private final VoxelShape HEAD_SHAPE = Block.box(0.0D, 12.0D, 0.0D, 16.0D, 16.0D, 16.0D);

    public enum LauncherState
    {EXTENDED, RETRACTED, MOVING}

    public LauncherState[] states;
    private float extensionStride = 1F; // 1/stride ticks per move
    private float retractingStride = extensionStride / 4;
    private int retractingDelay = 2;

    private float maxExtendCoefficient;
    private float progress;
    private float lastProgress;
    private boolean extending = true; // true if its extending, false if retracting

    protected int currentTick = 0;

    public LauncherState launcherState;
    public AbstractLauncherBlockTileEntity(TileEntityType<?> p_i48289_1_)
    {
        super(p_i48289_1_);
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
                        this.progress -= retractingStride;
                        if(this.progress <= 0F)
                        {
                            this.progress = 0F;
                        }
                    }
                }
                break;
        }
        if(!level.isClientSide &&lastProgress != progress)
        {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
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
    public CompoundNBT getUpdateTag()
    {
        CompoundNBT nbt = super.getUpdateTag();
        nbt.putInt("currentTick", currentTick);
        nbt.putFloat("progress", progress);
        nbt.putBoolean("extending", extending);
        nbt.putInt("launcherState", launcherState.ordinal());
        return nbt;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        CompoundNBT tag = getUpdateTag();
        return new SUpdateTileEntityPacket(getBlockPos(),-1, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
    {
        CompoundNBT tag = pkt.getTag();
        handleUpdateTag(getBlockState(), tag);

    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag)
    {
        currentTick = tag.getInt("currentTick");
        progress = tag.getFloat("progress");
        extending = tag.getBoolean("extending");
        launcherState = states[tag.getInt("launcherState")];
        super.handleUpdateTag(state, tag);
    }



    public float getDeltaProgress(float tickDelta)
    {
        if(tickDelta > 1.0F)
        {
            tickDelta = 1.0F;
        }
        return MathHelper.lerp(tickDelta, this.lastProgress, this.progress);
    }
}
