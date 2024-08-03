package snownee.lychee.util;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Objects;
import java.util.function.Function;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import snownee.lychee.client.gui.ILightingSettings;

public class CachedRenderingEntity<T extends Entity> {

    private Function<Level, T> factory;

    protected T entity;

    protected float scale = 15.0F;

    public static <T extends Entity> CachedRenderingEntity<T> of(@NotNull T entity) {
        return new CachedRenderingEntity<>(entity);
    }

    public static <T extends Entity> CachedRenderingEntity<T> ofFactory(Function<Level, T> factory) {
        return new CachedRenderingEntity<>(factory);
    }

    protected CachedRenderingEntity(@NotNull T entity) {
        this.setEntity(entity);
    }

    protected CachedRenderingEntity(Function<Level, T> factory) {
        this.factory = factory;
    }

    private void ensureEntity() {
        if (this.entity == null) {
            this.entity = (T) Objects.requireNonNull((Entity) this.factory.apply(Minecraft.getInstance().level));
            this.factory = null;
        }
    }

    public T getEntity() {
        this.ensureEntity();
        this.entity.tickCount = Minecraft.getInstance().player.f_19797_;
        return this.entity;
    }

    public void setEntity(@NotNull T entity) {
        this.entity = entity;
        this.factory = null;
        entity.setLevel(null);
    }

    public T earlySetLevel() {
        this.ensureEntity();
        this.entity.setLevel((Level) Objects.requireNonNull(Minecraft.getInstance().level));
        return this.entity;
    }

    public float getScale() {
        return this.scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void render(PoseStack matrixStack, float x, float y, float z, Quaternionf rotation) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            this.ensureEntity();
            this.entity.setLevel(mc.level);
            this.entity.tickCount = mc.player.f_19797_;
            Vec3 position = mc.player.m_20182_();
            this.entity.setPosRaw(position.x(), position.y(), position.z());
            matrixStack.pushPose();
            matrixStack.translate(x, y, z);
            matrixStack.scale(this.scale, this.scale, this.scale);
            matrixStack.mulPose(rotation);
            EntityRenderDispatcher renderDispatcher = mc.getEntityRenderDispatcher();
            rotation.conjugate();
            renderDispatcher.overrideCameraOrientation(rotation);
            renderDispatcher.setRenderShadow(false);
            MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
            renderDispatcher.render(this.entity, 0.0, 0.0, 0.0, mc.getFrameTime(), 1.0F, matrixStack, bufferSource, 15728880);
            bufferSource.endBatch();
            renderDispatcher.setRenderShadow(true);
            matrixStack.popPose();
            this.entity.setLevel(null);
            ILightingSettings.DEFAULT_3D.applyLighting();
        }
    }
}