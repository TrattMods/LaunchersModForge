package net.launchers.mod.loader;

import net.launchers.mod.initializer.*;
import net.launchers.mod.network.packet.UnboundedEntityVelocityS2CPacket;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkDirection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(LLoader.MOD_ID)
public class LLoader {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "launchersmodforge";

    public LLoader() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);

        LBlocks.initialize();
        LEntities.initialize();
        LItems.initialize();
        LCommands.initialize();
        LSounds.initialize();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::buildContents);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        int messageNumber = 0;
        LNetwork.channel
                .messageBuilder(UnboundedEntityVelocityS2CPacket.class, messageNumber++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(UnboundedEntityVelocityS2CPacket::write)
                .decoder(UnboundedEntityVelocityS2CPacket::new)
                .consumerMainThread(UnboundedEntityVelocityS2CPacket::handle)
                .add();
    }

    private void buildContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
            event.accept(LItems.LAUNCHER_BLOCK_ITEM);
            event.accept(LItems.POWERED_LAUNCHER_BLOCK_ITEM);
            event.accept(LItems.EXTREME_LAUNCHER_BLOCK_ITEM);
        }
    }

}