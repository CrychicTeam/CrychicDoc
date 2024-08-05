package net.minecraft.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.item.DyeableHorseArmorItem;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.ItemStack;

public class HorseArmorLayer extends RenderLayer<Horse, HorseModel<Horse>> {

    private final HorseModel<Horse> model;

    public HorseArmorLayer(RenderLayerParent<Horse, HorseModel<Horse>> renderLayerParentHorseHorseModelHorse0, EntityModelSet entityModelSet1) {
        super(renderLayerParentHorseHorseModelHorse0);
        this.model = new HorseModel<>(entityModelSet1.bakeLayer(ModelLayers.HORSE_ARMOR));
    }

    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, Horse horse3, float float4, float float5, float float6, float float7, float float8, float float9) {
        ItemStack $$10 = horse3.getArmor();
        if ($$10.getItem() instanceof HorseArmorItem) {
            HorseArmorItem $$11 = (HorseArmorItem) $$10.getItem();
            ((HorseModel) this.m_117386_()).m_102624_(this.model);
            this.model.prepareMobModel(horse3, float4, float5, float6);
            this.model.setupAnim(horse3, float4, float5, float7, float8, float9);
            float $$13;
            float $$14;
            float $$15;
            if ($$11 instanceof DyeableHorseArmorItem) {
                int $$12 = ((DyeableHorseArmorItem) $$11).m_41121_($$10);
                $$13 = (float) ($$12 >> 16 & 0xFF) / 255.0F;
                $$14 = (float) ($$12 >> 8 & 0xFF) / 255.0F;
                $$15 = (float) ($$12 & 0xFF) / 255.0F;
            } else {
                $$13 = 1.0F;
                $$14 = 1.0F;
                $$15 = 1.0F;
            }
            VertexConsumer $$19 = multiBufferSource1.getBuffer(RenderType.entityCutoutNoCull($$11.getTexture()));
            this.model.m_7695_(poseStack0, $$19, int2, OverlayTexture.NO_OVERLAY, $$13, $$14, $$15, 1.0F);
        }
    }
}