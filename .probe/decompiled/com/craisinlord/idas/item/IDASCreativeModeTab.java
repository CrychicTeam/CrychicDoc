package com.craisinlord.idas.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class IDASCreativeModeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "idas");

    public static final RegistryObject<CreativeModeTab> IDAS_TAB = CREATIVE_MODE_TABS.register("idas_tab", () -> CreativeModeTab.builder().icon(() -> new ItemStack(IDASItems.MUSIC_DISC_SLITHER.get())).title(Component.translatable("creativetab.idas_tab")).displayItems((pParameters, pOutput) -> {
        pOutput.accept(IDASItems.DISC_FRAGMENT_SLITHER.get());
        pOutput.accept(IDASItems.MUSIC_DISC_SLITHER.get());
        pOutput.accept(IDASItems.MUSIC_DISC_CALIDUM.get());
    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}