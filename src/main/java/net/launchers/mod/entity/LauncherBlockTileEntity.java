package net.launchers.mod.entity;

import net.launchers.mod.entity.abstraction.AbstractLauncherBlockTileEntity;
import net.launchers.mod.initializer.LEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class LauncherBlockTileEntity extends AbstractLauncherBlockTileEntity
{
    public LauncherBlockTileEntity(BlockPos pos, BlockState state)
    {
        super(LEntities.LAUNCHER_BLOCK_TILE_ENTITY.get(),pos,state);
    }
}
