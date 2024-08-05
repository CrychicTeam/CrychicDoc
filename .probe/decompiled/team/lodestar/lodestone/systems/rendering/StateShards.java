package team.lodestar.lodestone.systems.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.RenderStateShard;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry;

public class StateShards extends RenderStateShard {

    public static final RenderStateShard.TransparencyStateShard ADDITIVE_TRANSPARENCY = new RenderStateShard.TransparencyStateShard("additive_transparency", () -> {
        RenderSystem.enableBlend();
        LodestoneRenderTypeRegistry.ADDITIVE_FUNCTION.run();
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });

    public static final RenderStateShard.TransparencyStateShard NORMAL_TRANSPARENCY = new RenderStateShard.TransparencyStateShard("normal_transparency", () -> {
        RenderSystem.enableBlend();
        LodestoneRenderTypeRegistry.TRANSPARENT_FUNCTION.run();
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });

    public StateShards(String string0, Runnable runnable1, Runnable runnable2) {
        super(string0, runnable1, runnable2);
    }
}