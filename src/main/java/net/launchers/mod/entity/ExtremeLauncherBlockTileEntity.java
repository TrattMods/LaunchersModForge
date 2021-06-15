package net.launchers.mod.entity;

import net.launchers.mod.entity.abstraction.AbstractLauncherBlockTileEntity;
import net.launchers.mod.initializer.LEntities;
import net.minecraft.tileentity.TileEntityType;

public class ExtremeLauncherBlockTileEntity extends AbstractLauncherBlockTileEntity
{
    public ExtremeLauncherBlockTileEntity()
    {
        super(LEntities.EXTREME_LAUNCHER_BLOCK_TILE_ENTITY.get());
    }
}
