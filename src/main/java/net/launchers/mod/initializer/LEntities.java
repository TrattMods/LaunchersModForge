package net.launchers.mod.initializer;

import net.launchers.mod.block.ExtremeLauncherBlock;
import net.launchers.mod.block.LauncherBlock;
import net.launchers.mod.block.PoweredLauncherBlock;
import net.launchers.mod.entity.ExtremeLauncherBlockEntity;
import net.launchers.mod.entity.LauncherBlockEntity;
import net.launchers.mod.entity.PoweredLauncherBlockEntity;
import net.launchers.mod.loader.LLoader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class LEntities
{
    public static final DeferredRegister<BlockEntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, LLoader.MOD_ID);

    public static final RegistryObject<BlockEntityType<LauncherBlockEntity>> LAUNCHER_BLOCK_TILE_ENTITY = ENTITIES.register(LauncherBlock.ID,
            () -> BlockEntityType.Builder.of(LauncherBlockEntity::new, LBlocks.LAUNCHER_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<PoweredLauncherBlockEntity>> POWERED_LAUNCHER_BLOCK_TILE_ENTITY = ENTITIES.register(PoweredLauncherBlock.ID,
            () -> BlockEntityType.Builder.of(PoweredLauncherBlockEntity::new, LBlocks.POWERED_LAUNCHER_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<ExtremeLauncherBlockEntity>> EXTREME_LAUNCHER_BLOCK_TILE_ENTITY = ENTITIES.register(ExtremeLauncherBlock.ID,
            () -> BlockEntityType.Builder.of(ExtremeLauncherBlockEntity::new, LBlocks.EXTREME_LAUNCHER_BLOCK.get()).build(null));
    public static void initialize()
    {
        ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
