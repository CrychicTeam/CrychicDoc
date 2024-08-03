package com.yungnickyoung.minecraft.yungsapi.module;

import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegisterBlock;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegisterItem;
import com.yungnickyoung.minecraft.yungsapi.autoregister.AutoRegisterField;
import com.yungnickyoung.minecraft.yungsapi.autoregister.AutoRegistrationManager;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;

public class ItemModuleForge {

    public static void processEntries() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ItemModuleForge::registerItems);
    }

    private static void registerItems(RegisterEvent event) {
        event.register(Registries.ITEM, helper -> {
            AutoRegistrationManager.BLOCKS.forEach(data -> registerBlockItem(data, helper));
            BlockModuleForge.EXTRA_BLOCKS.forEach(extraBlockData -> registerExtraBlockItem(extraBlockData, helper));
            AutoRegistrationManager.ITEMS.stream().filter(data -> !data.processed()).forEach(data -> registerItem(data, helper));
        });
    }

    private static void registerBlockItem(AutoRegisterField data, RegisterEvent.RegisterHelper<Item> helper) {
        AutoRegisterBlock autoRegisterBlock = (AutoRegisterBlock) data.object();
        if (autoRegisterBlock.hasItemProperties()) {
            BlockItem blockItem = new BlockItem(autoRegisterBlock.get(), (Item.Properties) autoRegisterBlock.getItemProperties().get());
            helper.register(data.name(), blockItem);
        }
    }

    private static void registerExtraBlockItem(BlockModuleForge.ExtraBlockData extraBlockData, RegisterEvent.RegisterHelper<Item> helper) {
        BlockItem blockItem = new BlockItem(extraBlockData.block(), (Item.Properties) extraBlockData.itemProperties().get());
        helper.register(extraBlockData.blockRegisteredName(), blockItem);
    }

    private static void registerItem(AutoRegisterField data, RegisterEvent.RegisterHelper<Item> helper) {
        AutoRegisterItem autoRegisterItem = (AutoRegisterItem) data.object();
        Item item = autoRegisterItem.get();
        helper.register(data.name(), item);
        data.markProcessed();
    }
}