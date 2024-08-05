package dev.lambdaurora.lambdynlights.api.item;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.lambdaurora.lambdynlights.LambDynLights;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public final class ItemLightSources {

    private static final JsonParser JSON_PARSER = new JsonParser();

    private static final List<ItemLightSource> ITEM_LIGHT_SOURCES = new ArrayList();

    private static final List<ItemLightSource> STATIC_ITEM_LIGHT_SOURCES = new ArrayList();

    private ItemLightSources() {
        throw new UnsupportedOperationException("ItemLightSources only contains static definitions.");
    }

    public static void load(@NotNull ResourceManager resourceManager) {
        ITEM_LIGHT_SOURCES.clear();
        resourceManager.listResources("dynamiclights/item", path -> path.getPath().endsWith(".json")).forEach((id, resource) -> load(resourceManager, id, resource));
        ITEM_LIGHT_SOURCES.addAll(STATIC_ITEM_LIGHT_SOURCES);
    }

    private static void load(@NotNull ResourceManager resourceManager, @NotNull ResourceLocation resourceId, Resource resource) {
        ResourceLocation id = new ResourceLocation(resourceId.getNamespace(), resourceId.getPath().replace(".json", ""));
        try {
            JsonObject json = JSON_PARSER.parse(new InputStreamReader(resource.open())).getAsJsonObject();
            ItemLightSource.fromJson(id, json).ifPresent(data -> {
                if (!STATIC_ITEM_LIGHT_SOURCES.contains(data)) {
                    register(data);
                }
            });
        } catch (IllegalStateException | IOException var5) {
            LambDynLights.get().warn("Failed to load item light source \"" + id + "\".");
        }
    }

    private static void register(@NotNull ItemLightSource data) {
        for (ItemLightSource other : ITEM_LIGHT_SOURCES) {
            if (other.item() == data.item()) {
                LambDynLights.get().warn("Failed to register item light source \"" + data.id() + "\", duplicates item \"" + ForgeRegistries.ITEMS.getKey(data.item()) + "\" found in \"" + other.id() + "\".");
                return;
            }
        }
        ITEM_LIGHT_SOURCES.add(data);
    }

    public static void registerItemLightSource(@NotNull ItemLightSource data) {
        for (ItemLightSource other : STATIC_ITEM_LIGHT_SOURCES) {
            if (other.item() == data.item()) {
                LambDynLights.get().warn("Failed to register item light source \"" + data.id() + "\", duplicates item \"" + ForgeRegistries.ITEMS.getKey(data.item()) + "\" found in \"" + other.id() + "\".");
                return;
            }
        }
        STATIC_ITEM_LIGHT_SOURCES.add(data);
    }

    public static int getLuminance(@NotNull ItemStack stack, boolean submergedInWater) {
        for (ItemLightSource data : ITEM_LIGHT_SOURCES) {
            if (data.item() == stack.getItem()) {
                return data.getLuminance(stack, submergedInWater);
            }
        }
        return stack.getItem() instanceof BlockItem blockItem ? ItemLightSource.BlockItemLightSource.getLuminance(stack, blockItem.getBlock().defaultBlockState()) : 0;
    }
}