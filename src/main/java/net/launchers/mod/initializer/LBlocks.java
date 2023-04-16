package net.launchers.mod.initializer;

import net.launchers.mod.block.ExtremeLauncherBlock;
import net.launchers.mod.block.LauncherBlock;
import net.launchers.mod.block.PoweredLauncherBlock;
import net.launchers.mod.loader.LLoader;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class LBlocks
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, LLoader.MOD_ID);

    public static final RegistryObject<Block> LAUNCHER_BLOCK = BLOCKS.register(LauncherBlock.ID, LauncherBlock::new);
    public static final RegistryObject<Block> POWERED_LAUNCHER_BLOCK = BLOCKS.register(PoweredLauncherBlock.ID, PoweredLauncherBlock::new);
    public static final RegistryObject<Block> EXTREME_LAUNCHER_BLOCK = BLOCKS.register(ExtremeLauncherBlock.ID, ExtremeLauncherBlock::new);

    public static void initialize()
    {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
