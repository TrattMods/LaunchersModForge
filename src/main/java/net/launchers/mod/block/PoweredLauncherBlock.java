package net.launchers.mod.block;

import net.launchers.mod.block.abstraction.AbstractLauncherBlock;
import net.launchers.mod.entity.PoweredLauncherBlockTileEntity;
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

public class PoweredLauncherBlock extends AbstractLauncherBlock
{
    public static final String ID = "powered_launcher_block";
    public PoweredLauncherBlock()
    {
        super(AbstractBlock.Properties.of(Material.PISTON).requiresCorrectToolForDrops().strength(40F, 900F).sound(SoundType.STONE));
        baseMultiplier = 2.125F;
        stackPowerPercentage = 0.2975F;
        stackMultiplier = baseMultiplier * stackPowerPercentage;
    }
    @Override
    protected void playLaunchSound(World world, BlockPos pos)
    {
        world.playSound(null,pos, LSounds.LAUNCHER_SOUND_EVENT.get(), SoundCategory.BLOCKS, 0.9F, 0.875F);
    }
    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader p_196283_1_)
    {
        return new PoweredLauncherBlockTileEntity();
    }
}
