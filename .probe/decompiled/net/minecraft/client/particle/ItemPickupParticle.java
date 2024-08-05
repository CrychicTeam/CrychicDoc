package net.minecraft.client.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.Vec3;

public class ItemPickupParticle extends Particle {

    private static final int LIFE_TIME = 3;

    private final RenderBuffers renderBuffers;

    private final Entity itemEntity;

    private final Entity target;

    private int life;

    private final EntityRenderDispatcher entityRenderDispatcher;

    public ItemPickupParticle(EntityRenderDispatcher entityRenderDispatcher0, RenderBuffers renderBuffers1, ClientLevel clientLevel2, Entity entity3, Entity entity4) {
        this(entityRenderDispatcher0, renderBuffers1, clientLevel2, entity3, entity4, entity3.getDeltaMovement());
    }

    private ItemPickupParticle(EntityRenderDispatcher entityRenderDispatcher0, RenderBuffers renderBuffers1, ClientLevel clientLevel2, Entity entity3, Entity entity4, Vec3 vec5) {
        super(clientLevel2, entity3.getX(), entity3.getY(), entity3.getZ(), vec5.x, vec5.y, vec5.z);
        this.renderBuffers = renderBuffers1;
        this.itemEntity = this.getSafeCopy(entity3);
        this.target = entity4;
        this.entityRenderDispatcher = entityRenderDispatcher0;
    }

    private Entity getSafeCopy(Entity entity0) {
        return (Entity) (!(entity0 instanceof ItemEntity) ? entity0 : ((ItemEntity) entity0).copy());
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    @Override
    public void render(VertexConsumer vertexConsumer0, Camera camera1, float float2) {
        float $$3 = ((float) this.life + float2) / 3.0F;
        $$3 *= $$3;
        double $$4 = Mth.lerp((double) float2, this.target.xOld, this.target.getX());
        double $$5 = Mth.lerp((double) float2, this.target.yOld, (this.target.getY() + this.target.getEyeY()) / 2.0);
        double $$6 = Mth.lerp((double) float2, this.target.zOld, this.target.getZ());
        double $$7 = Mth.lerp((double) $$3, this.itemEntity.getX(), $$4);
        double $$8 = Mth.lerp((double) $$3, this.itemEntity.getY(), $$5);
        double $$9 = Mth.lerp((double) $$3, this.itemEntity.getZ(), $$6);
        MultiBufferSource.BufferSource $$10 = this.renderBuffers.bufferSource();
        Vec3 $$11 = camera1.getPosition();
        this.entityRenderDispatcher.render(this.itemEntity, $$7 - $$11.x(), $$8 - $$11.y(), $$9 - $$11.z(), this.itemEntity.getYRot(), float2, new PoseStack(), $$10, this.entityRenderDispatcher.getPackedLightCoords(this.itemEntity, float2));
        $$10.endBatch();
    }

    @Override
    public void tick() {
        this.life++;
        if (this.life == 3) {
            this.m_107274_();
        }
    }
}