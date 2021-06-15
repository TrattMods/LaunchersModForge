package net.launchers.mod.initializer;

import net.launchers.mod.block.abstraction.AbstractLauncherBlock;
import net.launchers.mod.loader.LLoader;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class LSounds
{
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, LLoader.MOD_ID);
    public static final RegistryObject<SoundEvent> LAUNCHER_SOUND_EVENT = SOUNDS.register(AbstractLauncherBlock.LAUNCH_SOUND.getPath(), ()-> new SoundEvent(AbstractLauncherBlock.LAUNCH_SOUND));
    public static void initialize()
    {
        SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
