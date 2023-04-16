package net.launchers.mod.entity;

import net.launchers.mod.entity.abstraction.AbstractLauncherBlockTileEntity;
import net.launchers.mod.initializer.LEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class PoweredLauncherBlockTileEntity extends AbstractLauncherBlockTileEntity
{
    public PoweredLauncherBlockTileEntity(BlockPos pos, BlockState state)
    {
        super(LEntities.POWERED_LAUNCHER_BLOCK_TILE_ENTITY.get(),pos,state);
    }
}
