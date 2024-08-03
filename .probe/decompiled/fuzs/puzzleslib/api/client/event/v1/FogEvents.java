package fuzs.puzzleslib.api.client.event.v1;

import com.mojang.blaze3d.shaders.FogShape;
import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.data.MutableFloat;
import fuzs.puzzleslib.api.event.v1.data.MutableValue;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.level.material.FogType;

public final class FogEvents {

    public static final EventInvoker<FogEvents.Render> RENDER = EventInvoker.lookup(FogEvents.Render.class);

    public static final EventInvoker<FogEvents.ComputeColor> COMPUTE_COLOR = EventInvoker.lookup(FogEvents.ComputeColor.class);

    private FogEvents() {
    }

    @FunctionalInterface
    public interface ComputeColor {

        void onComputeFogColor(GameRenderer var1, Camera var2, float var3, MutableFloat var4, MutableFloat var5, MutableFloat var6);
    }

    @FunctionalInterface
    public interface Render {

        void onRenderFog(GameRenderer var1, Camera var2, float var3, FogRenderer.FogMode var4, FogType var5, MutableFloat var6, MutableFloat var7, MutableValue<FogShape> var8);
    }
}