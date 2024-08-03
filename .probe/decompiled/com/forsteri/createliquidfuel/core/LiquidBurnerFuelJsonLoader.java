package com.forsteri.createliquidfuel.core;

import com.forsteri.createliquidfuel.util.Triplet;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class LiquidBurnerFuelJsonLoader extends SimpleJsonResourceReloadListener {

    public static final ResourceLocation IDENTIFIER = ResourceLocation.of("createliquidfuel:drainable_fuel_loader", ':');

    private static final Gson GSON = new Gson();

    public static final LiquidBurnerFuelJsonLoader INSTANCE = new LiquidBurnerFuelJsonLoader();

    public LiquidBurnerFuelJsonLoader() {
        super(GSON, "blaze_burner_fuel");
    }

    protected void apply(Map<ResourceLocation, JsonElement> mapResourceLocationJsonElement0, @NotNull ResourceManager resourceManager1, @NotNull ProfilerFiller profilerFiller2) {
        for (Entry<ResourceLocation, JsonElement> entry : mapResourceLocationJsonElement0.entrySet()) {
            JsonElement element = (JsonElement) entry.getValue();
            if (element.isJsonObject()) {
                ResourceLocation id = (ResourceLocation) entry.getKey();
                JsonObject object = element.getAsJsonObject();
                JsonElement fluidElement = object.get("fluid");
                if (fluidElement == null) {
                    throw new RuntimeException("No fluid specified for liquid burner fuel: " + id);
                }
                try {
                    Fluid value = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fluidElement.getAsString()));
                    if (value != null) {
                        BurnerStomachHandler.LIQUID_BURNER_FUEL_MAP.put(value, Pair.of(IDENTIFIER, Triplet.of(object.has("burnTime") ? object.get("burnTime").getAsInt() : (object.has("superHeat") && object.get("superHeat").getAsBoolean() ? 32 : 20), object.has("superHeat") && object.get("superHeat").getAsBoolean(), object.has("amountConsumedPerTick") ? object.get("amountConsumedPerTick").getAsInt() : (object.has("superHeat") && object.get("superHeat").getAsBoolean() ? 10 : 1))));
                    }
                } catch (ResourceLocationException var11) {
                    throw new RuntimeException("Fluid liquid burner fuel " + id + " has invalid fluid: " + fluidElement.getAsString());
                }
            }
        }
    }
}