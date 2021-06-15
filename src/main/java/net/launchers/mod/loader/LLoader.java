package net.launchers.mod.loader;

import net.launchers.mod.initializer.*;
import net.launchers.mod.network.packet.UnboundedEntityVelocityS2CPacket;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkDirection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(LLoader.MOD_ID)
public class LLoader
{
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "launchersmodforge";

    public LLoader()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::commonSetup);

        LBlocks.initialize();
        LEntities.initialize();
        LItems.initialize();
        LSounds.initialize();

        MinecraftForge.EVENT_BUS.addListener(this::onCommands);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void commonSetup(FMLCommonSetupEvent event)
    {
        int messageNumber = 0;
        LNetwork.channel.messageBuilder(UnboundedEntityVelocityS2CPacket.class, messageNumber++, NetworkDirection.PLAY_TO_CLIENT).encoder(UnboundedEntityVelocityS2CPacket::write).decoder(UnboundedEntityVelocityS2CPacket::new).consumer(UnboundedEntityVelocityS2CPacket::handle).add();
    }

    @SubscribeEvent
    public void onCommands(RegisterCommandsEvent event)
    {
        LCommands.initialize(event.getDispatcher());
    }
}