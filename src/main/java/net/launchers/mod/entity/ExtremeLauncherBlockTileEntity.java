package net.launchers.mod.entity;

import net.launchers.mod.entity.abstraction.AbstractLauncherBlockTileEntity;
import net.launchers.mod.initializer.LEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class ExtremeLauncherBlockTileEntity extends AbstractLauncherBlockTileEntity
{
    public ExtremeLauncherBlockTileEntity(BlockPos pos, BlockState state)
    {
        super(LEntities.EXTREME_LAUNCHER_BLOCK_TILE_ENTITY.get(),pos,state);
    }
}
