package net.launchers.mod.block;

import net.launchers.mod.block.abstraction.AbstractLauncherBlock;
import net.launchers.mod.entity.ExtremeLauncherBlockTileEntity;
import net.launchers.mod.initializer.LSounds;
import net.launchers.mod.loader.LLoader;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ExtremeLauncherBlock extends AbstractLauncherBlock
{
    public static final String ID = "extreme_launcher_block";
    public ExtremeLauncherBlock()
    {
        super();
        baseMultiplier = 2.95F;
        stackPowerPercentage = 0.275F;
        stackMultiplier = baseMultiplier * stackPowerPercentage;
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader p_196283_1_)
    {
        return new ExtremeLauncherBlockTileEntity();
    }

    @Override
    protected void playLaunchSound(World world, BlockPos pos)
    {
        world.playSound(null,pos, LSounds.LAUNCHER_SOUND_EVENT.get(), SoundCategory.BLOCKS, 1.15F, 0.775F);
    }
}
