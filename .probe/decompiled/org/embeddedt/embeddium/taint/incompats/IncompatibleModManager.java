package org.embeddedt.embeddium.taint.incompats;

import com.google.common.collect.ImmutableList;
import java.util.List;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.ModLoadingStage;
import net.minecraftforge.fml.ModLoadingWarning;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.forgespi.language.IModInfo;

public class IncompatibleModManager {

    private static final List<ModDeclaration> INCOMPATIBLE_MODS = ImmutableList.builder().add(new ModDeclaration.Single("entityculling", "Entity Culling")).add(new ModDeclaration.Single("sound_physics_remastered", "Sound Physics Remastered")).build();

    public static void checkMods(FMLClientSetupEvent event) {
        IModInfo selfInfo = ModLoadingContext.get().getActiveContainer().getModInfo();
        if (ModList.get().isLoaded("lazurite")) {
            event.enqueueWork(() -> ModLoader.get().addWarning(new ModLoadingWarning(selfInfo, ModLoadingStage.SIDED_SETUP, "Embeddium includes FFAPI support as of 0.3.20, Lazurite should be removed", new Object[0])));
        }
    }
}