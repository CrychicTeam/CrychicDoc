package com.yungnickyoung.minecraft.yungsapi.module;

import com.yungnickyoung.minecraft.yungsapi.autoregister.AutoRegisterField;
import com.yungnickyoung.minecraft.yungsapi.autoregister.AutoRegistrationManager;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class StructurePoolElementTypeModuleForge {

    public static void processEntries() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(StructurePoolElementTypeModuleForge::commonSetup);
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> AutoRegistrationManager.STRUCTURE_POOL_ELEMENT_TYPES.stream().filter(data -> !data.processed()).forEach(StructurePoolElementTypeModuleForge::register));
    }

    private static void register(AutoRegisterField data) {
        Registry.register(BuiltInRegistries.STRUCTURE_POOL_ELEMENT, data.name(), (StructurePoolElementType) data.object());
        data.markProcessed();
    }
}