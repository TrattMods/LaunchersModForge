package net.launchers.mod.initializer;

import net.launchers.mod.block.ExtremeLauncherBlock;
import net.launchers.mod.block.LauncherBlock;
import net.launchers.mod.block.PoweredLauncherBlock;
import net.launchers.mod.entity.ExtremeLauncherBlockTileEntity;
import net.launchers.mod.entity.LauncherBlockTileEntity;
import net.launchers.mod.entity.PoweredLauncherBlockTileEntity;
import net.launchers.mod.loader.LLoader;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class LEntities
{
    public static final DeferredRegister<TileEntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, LLoader.MOD_ID);

    public static final RegistryObject<TileEntityType<LauncherBlockTileEntity>> LAUNCHER_BLOCK_TILE_ENTITY = ENTITIES.register(LauncherBlock.ID,
            () -> TileEntityType.Builder.of(LauncherBlockTileEntity::new, LBlocks.LAUNCHER_BLOCK.get()).build(null));

    public static final RegistryObject<TileEntityType<PoweredLauncherBlockTileEntity>> POWERED_LAUNCHER_BLOCK_TILE_ENTITY = ENTITIES.register(PoweredLauncherBlock.ID,
            () -> TileEntityType.Builder.of(PoweredLauncherBlockTileEntity::new, LBlocks.POWERED_LAUNCHER_BLOCK.get()).build(null));

    public static final RegistryObject<TileEntityType<ExtremeLauncherBlockTileEntity>> EXTREME_LAUNCHER_BLOCK_TILE_ENTITY = ENTITIES.register(ExtremeLauncherBlock.ID,
            () -> TileEntityType.Builder.of(ExtremeLauncherBlockTileEntity::new, LBlocks.EXTREME_LAUNCHER_BLOCK.get()).build(null));
    public static void initialize()
    {
        ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
