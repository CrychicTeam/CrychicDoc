package team.lodestar.lodestone.registry.client;

import java.util.HashMap;
import team.lodestar.lodestone.systems.worldevent.WorldEventInstance;
import team.lodestar.lodestone.systems.worldevent.WorldEventRenderer;
import team.lodestar.lodestone.systems.worldevent.WorldEventType;

public class LodestoneWorldEventRendererRegistry {

    public static HashMap<WorldEventType, WorldEventRenderer<WorldEventInstance>> RENDERERS = new HashMap();

    public static void registerRenderer(WorldEventType type, WorldEventRenderer<? extends WorldEventInstance> renderer) {
        RENDERERS.put(type, renderer);
    }
}