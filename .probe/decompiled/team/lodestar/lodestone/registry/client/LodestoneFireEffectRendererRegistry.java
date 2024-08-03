package team.lodestar.lodestone.registry.client;

import java.util.HashMap;
import team.lodestar.lodestone.systems.fireeffect.FireEffectInstance;
import team.lodestar.lodestone.systems.fireeffect.FireEffectRenderer;
import team.lodestar.lodestone.systems.fireeffect.FireEffectType;

public class LodestoneFireEffectRendererRegistry {

    public static HashMap<FireEffectType, FireEffectRenderer<FireEffectInstance>> RENDERERS = new HashMap();

    public static void registerRenderer(FireEffectType type, FireEffectRenderer<? extends FireEffectInstance> renderer) {
        RENDERERS.put(type, renderer);
    }
}