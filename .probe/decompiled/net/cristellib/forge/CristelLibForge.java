package net.cristellib.forge;

import net.cristellib.CristelLib;
import net.cristellib.forge.extrapackutil.RepositorySourceMaker;
import net.minecraft.server.packs.PackType;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("cristellib")
public class CristelLibForge {

    public CristelLibForge() {
        CristelLib.preInit();
        CristelLib.init();
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::injectPackRepositories);
    }

    private void injectPackRepositories(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.SERVER_DATA) {
            event.addRepositorySource(new RepositorySourceMaker());
        }
    }
}