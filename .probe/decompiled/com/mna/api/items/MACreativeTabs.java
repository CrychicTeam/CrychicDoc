package com.mna.api.items;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

@EventBusSubscriber(modid = "mna", bus = Bus.MOD)
public class MACreativeTabs {

    public static CreativeModeTab GENERAL = CreativeModeTab.builder().icon(() -> new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("mna", "guide_book")))).title(Component.translatable("itemGroup.mna.items")).build();

    @SubscribeEvent
    public static void onRegistrCreativeTabs(RegisterEvent event) {
        event.register(Registries.CREATIVE_MODE_TAB, helper -> helper.register(new ResourceLocation("mna", "tab_general"), GENERAL));
    }
}