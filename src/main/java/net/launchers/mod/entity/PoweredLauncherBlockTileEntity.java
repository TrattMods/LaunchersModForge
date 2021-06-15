package net.launchers.mod.entity;

import net.launchers.mod.entity.abstraction.AbstractLauncherBlockTileEntity;
import net.launchers.mod.initializer.LEntities;
import net.minecraft.tileentity.TileEntityType;

public class PoweredLauncherBlockTileEntity extends AbstractLauncherBlockTileEntity
{
    public PoweredLauncherBlockTileEntity()
    {
        super(LEntities.POWERED_LAUNCHER_BLOCK_TILE_ENTITY.get());
    }
}
