package net.launchers.mod.initializer;

import net.launchers.mod.block.ExtremeLauncherBlock;
import net.launchers.mod.block.LauncherBlock;
import net.launchers.mod.block.PoweredLauncherBlock;
import net.launchers.mod.loader.LLoader;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class LItems
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, LLoader.MOD_ID);

    public static final RegistryObject<BlockItem> LAUNCHER_BLOCK_ITEM = ITEMS.register(LauncherBlock.ID,
            () -> new BlockItem(LBlocks.LAUNCHER_BLOCK.get(), new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));

    public static final RegistryObject<BlockItem> POWERED_LAUNCHER_BLOCK_ITEM = ITEMS.register(PoweredLauncherBlock.ID,
            () -> new BlockItem(LBlocks.POWERED_LAUNCHER_BLOCK.get(), new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));

    public static final RegistryObject<BlockItem> EXTREME_LAUNCHER_BLOCK_ITEM = ITEMS.register(ExtremeLauncherBlock.ID,
            () -> new BlockItem(LBlocks.EXTREME_LAUNCHER_BLOCK.get(), new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));

    public static void initialize()
    {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
