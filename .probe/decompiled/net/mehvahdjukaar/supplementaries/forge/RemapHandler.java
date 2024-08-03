package net.mehvahdjukaar.supplementaries.forge;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.MissingMappingsEvent;

@EventBusSubscriber
public class RemapHandler {

    private static final Map<String, String> REMAP = new HashMap();

    @SubscribeEvent
    public static void onRemapBlocks(MissingMappingsEvent event) {
        remapAll(event, BuiltInRegistries.BLOCK);
        remapAll(event, BuiltInRegistries.ITEM);
    }

    private static <T> void remapAll(MissingMappingsEvent event, DefaultedRegistry<T> block) {
        for (MissingMappingsEvent.Mapping<T> v : event.getMappings(block.m_123023_(), "supplementaries")) {
            String rem = (String) REMAP.get(v.getKey().toString());
            if (rem != null) {
                Optional<T> b = block.m_6612_(new ResourceLocation(rem));
                b.ifPresent(v::remap);
            } else {
                v.ignore();
            }
        }
    }

    static {
        REMAP.put("supplementaries:copper_lantern", "suppsquared:copper_lantern");
        REMAP.put("supplementaries:crimson_lantern", "suppsquared:crimson_lantern");
        REMAP.put("supplementaries:brass_lantern", "suppsquared:brass_lantern");
        REMAP.put("supplementaries:silver_lantern", "oreganized:silver_lantern");
        REMAP.put("supplementaries:silver_door", "oreganized:silver_door");
        REMAP.put("supplementaries:silver_trapdoor", "oreganized:silver_trapdoor");
        REMAP.put("supplementaries:lead_lantern", "oreganized:lead_lantern");
        REMAP.put("supplementaries:lead_door", "oreganized:lead_door");
        REMAP.put("supplementaries:lead_trapdoor", "oreganized:lead_trapdoor");
    }
}