package fuzs.puzzleslib.api.client.event.v1;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import org.joml.Matrix4f;

public final class RenderLevelEvents {

    public static final EventInvoker<RenderLevelEvents.AfterTerrain> AFTER_TERRAIN = EventInvoker.lookup(RenderLevelEvents.AfterTerrain.class);

    public static final EventInvoker<RenderLevelEvents.AfterEntities> AFTER_ENTITIES = EventInvoker.lookup(RenderLevelEvents.AfterEntities.class);

    public static final EventInvoker<RenderLevelEvents.AfterTranslucent> AFTER_TRANSLUCENT = EventInvoker.lookup(RenderLevelEvents.AfterTranslucent.class);

    public static final EventInvoker<RenderLevelEvents.AfterLevel> AFTER_LEVEL = EventInvoker.lookup(RenderLevelEvents.AfterLevel.class);

    private RenderLevelEvents() {
    }

    @FunctionalInterface
    public interface AfterEntities {

        void onRenderLevelAfterEntities(LevelRenderer var1, Camera var2, GameRenderer var3, float var4, PoseStack var5, Matrix4f var6, Frustum var7, ClientLevel var8);
    }

    @FunctionalInterface
    public interface AfterLevel {

        void onRenderLevelAfterLevel(LevelRenderer var1, Camera var2, GameRenderer var3, float var4, PoseStack var5, Matrix4f var6, Frustum var7, ClientLevel var8);
    }

    @FunctionalInterface
    public interface AfterTerrain {

        void onRenderLevelAfterTerrain(LevelRenderer var1, Camera var2, GameRenderer var3, float var4, PoseStack var5, Matrix4f var6, Frustum var7, ClientLevel var8);
    }

    @FunctionalInterface
    public interface AfterTranslucent {

        void onRenderLevelAfterTranslucent(LevelRenderer var1, Camera var2, GameRenderer var3, float var4, PoseStack var5, Matrix4f var6, Frustum var7, ClientLevel var8);
    }
}