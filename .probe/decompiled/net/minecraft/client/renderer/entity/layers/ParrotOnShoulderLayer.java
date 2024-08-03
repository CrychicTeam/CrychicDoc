package net.minecraft.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ParrotModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ParrotRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.player.Player;

public class ParrotOnShoulderLayer<T extends Player> extends RenderLayer<T, PlayerModel<T>> {

    private final ParrotModel model;

    public ParrotOnShoulderLayer(RenderLayerParent<T, PlayerModel<T>> renderLayerParentTPlayerModelT0, EntityModelSet entityModelSet1) {
        super(renderLayerParentTPlayerModelT0);
        this.model = new ParrotModel(entityModelSet1.bakeLayer(ModelLayers.PARROT));
    }

    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, T t3, float float4, float float5, float float6, float float7, float float8, float float9) {
        this.render(poseStack0, multiBufferSource1, int2, t3, float4, float5, float8, float9, true);
        this.render(poseStack0, multiBufferSource1, int2, t3, float4, float5, float8, float9, false);
    }

    private void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, T t3, float float4, float float5, float float6, float float7, boolean boolean8) {
        CompoundTag $$9 = boolean8 ? t3.getShoulderEntityLeft() : t3.getShoulderEntityRight();
        EntityType.byString($$9.getString("id")).filter(p_117294_ -> p_117294_ == EntityType.PARROT).ifPresent(p_262538_ -> {
            poseStack0.pushPose();
            poseStack0.translate(boolean8 ? 0.4F : -0.4F, t3.m_6047_() ? -1.3F : -1.5F, 0.0F);
            Parrot.Variant $$11 = Parrot.Variant.byId($$9.getInt("Variant"));
            VertexConsumer $$12 = multiBufferSource1.getBuffer(this.model.m_103119_(ParrotRenderer.getVariantTexture($$11)));
            this.model.renderOnShoulder(poseStack0, $$12, int2, OverlayTexture.NO_OVERLAY, float4, float5, float6, float7, t3.f_19797_);
            poseStack0.popPose();
        });
    }
}