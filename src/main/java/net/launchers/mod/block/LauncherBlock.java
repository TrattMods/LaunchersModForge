package net.launchers.mod.block;

import net.launchers.mod.block.abstraction.AbstractLauncherBlock;
import net.launchers.mod.entity.LauncherBlockTileEntity;
import net.launchers.mod.initializer.LSounds;
import net.launchers.mod.loader.LLoader;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class LauncherBlock extends AbstractLauncherBlock
{
    public static final String ID = "launcher_block";
    public LauncherBlock()
    {
        super();
        baseMultiplier = 1.25F;
        stackPowerPercentage = 0.335F;
        stackMultiplier = baseMultiplier * stackPowerPercentage;
    }

    @Override
    protected void playLaunchSound(World world, BlockPos pos)
    {
        world.playSound(null,pos, LSounds.LAUNCHER_SOUND_EVENT.get(), SoundCategory.BLOCKS, 0.75F, 1F);
    }
    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader p_196283_1_)
    {
        return new LauncherBlockTileEntity();
    }
}
