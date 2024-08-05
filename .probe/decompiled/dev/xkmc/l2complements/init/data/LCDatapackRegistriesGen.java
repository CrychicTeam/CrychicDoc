package dev.xkmc.l2complements.init.data;

import dev.xkmc.l2complements.init.materials.LCMats;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import org.jetbrains.annotations.NotNull;

public class LCDatapackRegistriesGen extends DatapackBuiltinEntriesProvider {

    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder().add(Registries.TRIM_MATERIAL, ctx -> Arrays.stream(LCMats.values()).forEach(e -> ctx.register(ResourceKey.create(Registries.TRIM_MATERIAL, new ResourceLocation("l2complements", e.getID())), TrimMaterial.create(e.getID(), e.getIngot(), 0.0F, e.getIngot().getDescription().copy().withStyle(e.trim_text_color), Map.of()))));

    public LCDatapackRegistriesGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of("minecraft", "l2complements"));
    }

    @NotNull
    @Override
    public String getName() {
        return "L2Complements Data";
    }
}