package lio.playeranimatorapi.registry;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.entity.EntityRenderer;

public class PlayerEffectsRendererRegistry {

    private static List<EntityRenderer> renderers = new ArrayList();

    public static List<EntityRenderer> getRenderers() {
        return renderers;
    }

    public static void register(EntityRenderer renderer) {
        renderers.add(renderer);
    }
}