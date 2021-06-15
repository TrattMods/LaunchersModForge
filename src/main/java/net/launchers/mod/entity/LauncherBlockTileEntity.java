package net.launchers.mod.entity;

import net.launchers.mod.entity.abstraction.AbstractLauncherBlockTileEntity;
import net.launchers.mod.initializer.LEntities;
import net.minecraft.tileentity.TileEntityType;

public class LauncherBlockTileEntity extends AbstractLauncherBlockTileEntity
{
    public LauncherBlockTileEntity()
    {
        super(LEntities.LAUNCHER_BLOCK_TILE_ENTITY.get());
    }
}
