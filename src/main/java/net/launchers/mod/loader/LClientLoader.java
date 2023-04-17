package net.launchers.mod.loader;


import net.launchers.mod.entity_renderer.abstraction.LauncherBlockEntityRenderer;
import net.launchers.mod.initializer.LEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = LLoader.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LClientLoader
{
    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event)
    {
        event.registerBlockEntityRenderer(LEntities.LAUNCHER_BLOCK_TILE_ENTITY.get(), LauncherBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(LEntities.POWERED_LAUNCHER_BLOCK_TILE_ENTITY.get(), LauncherBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(LEntities.EXTREME_LAUNCHER_BLOCK_TILE_ENTITY.get(), LauncherBlockEntityRenderer::new);
    }
}