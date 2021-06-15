package net.launchers.mod.loader;

import net.launchers.mod.entity.ExtremeLauncherBlockTileEntity;
import net.launchers.mod.entity.LauncherBlockTileEntity;
import net.launchers.mod.entity.PoweredLauncherBlockTileEntity;
import net.launchers.mod.entity_renderer.abstraction.AbstractLauncherBlockTileEntityRenderer;
import net.launchers.mod.initializer.LEntities;
import net.launchers.mod.loader.LLoader;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;


@Mod.EventBusSubscriber(modid = LLoader.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LClientLoader
{
    @SubscribeEvent
    public static void init(final FMLClientSetupEvent event)
    {
        ClientRegistry.bindTileEntityRenderer(LEntities.LAUNCHER_BLOCK_TILE_ENTITY.get(), tileEntityRendererDispatcher -> new AbstractLauncherBlockTileEntityRenderer<LauncherBlockTileEntity>(tileEntityRendererDispatcher));
        ClientRegistry.bindTileEntityRenderer(LEntities.POWERED_LAUNCHER_BLOCK_TILE_ENTITY.get(), tileEntityRendererDispatcher -> new AbstractLauncherBlockTileEntityRenderer<PoweredLauncherBlockTileEntity>(tileEntityRendererDispatcher));
        ClientRegistry.bindTileEntityRenderer(LEntities.EXTREME_LAUNCHER_BLOCK_TILE_ENTITY.get(), tileEntityRendererDispatcher -> new AbstractLauncherBlockTileEntityRenderer<ExtremeLauncherBlockTileEntity>(tileEntityRendererDispatcher));
    }
}