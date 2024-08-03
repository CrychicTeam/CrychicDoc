package com.yungnickyoung.minecraft.yungsapi.module;

import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegisterCreativeTab;
import com.yungnickyoung.minecraft.yungsapi.autoregister.AutoRegisterField;
import com.yungnickyoung.minecraft.yungsapi.autoregister.AutoRegistrationManager;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;

public class CreativeModeTabModuleForge {

    public static void processEntries() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(CreativeModeTabModuleForge::registerTabs);
    }

    private static void registerTabs(RegisterEvent event) {
        event.register(Registries.CREATIVE_MODE_TAB, helper -> AutoRegistrationManager.CREATIVE_MODE_TABS.stream().filter(data -> !data.processed()).forEach(data -> registerTab(data, helper)));
    }

    private static void registerTab(AutoRegisterField data, RegisterEvent.RegisterHelper<CreativeModeTab> helper) {
        AutoRegisterCreativeTab autoRegisterCreativeTab = (AutoRegisterCreativeTab) data.object();
        CreativeModeTab.Builder creativeModeTabBuilder = CreativeModeTab.builder().title(autoRegisterCreativeTab.getDisplayName()).icon(autoRegisterCreativeTab.getIconItemStackSupplier()).displayItems((params, output) -> autoRegisterCreativeTab.getDisplayItemsGenerator().accept(params, output)).backgroundSuffix(autoRegisterCreativeTab.getBackgroundSuffix());
        if (!autoRegisterCreativeTab.canScroll()) {
            creativeModeTabBuilder.noScrollBar();
        }
        if (!autoRegisterCreativeTab.showTitle()) {
            creativeModeTabBuilder.hideTitle();
        }
        if (autoRegisterCreativeTab.alignedRight()) {
            creativeModeTabBuilder.alignedRight();
        }
        CreativeModeTab creativeModeTab = creativeModeTabBuilder.build();
        helper.register(data.name(), creativeModeTab);
        autoRegisterCreativeTab.setSupplier(() -> creativeModeTab);
        data.markProcessed();
    }
}