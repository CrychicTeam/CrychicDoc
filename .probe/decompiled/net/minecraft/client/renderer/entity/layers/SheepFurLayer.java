package net.minecraft.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.SheepFurModel;
import net.minecraft.client.model.SheepModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;

public class SheepFurLayer extends RenderLayer<Sheep, SheepModel<Sheep>> {

    private static final ResourceLocation SHEEP_FUR_LOCATION = new ResourceLocation("textures/entity/sheep/sheep_fur.png");

    private final SheepFurModel<Sheep> model;

    public SheepFurLayer(RenderLayerParent<Sheep, SheepModel<Sheep>> renderLayerParentSheepSheepModelSheep0, EntityModelSet entityModelSet1) {
        super(renderLayerParentSheepSheepModelSheep0);
        this.model = new SheepFurModel<>(entityModelSet1.bakeLayer(ModelLayers.SHEEP_FUR));
    }

    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, Sheep sheep3, float float4, float float5, float float6, float float7, float float8, float float9) {
        if (!sheep3.isSheared()) {
            if (sheep3.m_20145_()) {
                Minecraft $$10 = Minecraft.getInstance();
                boolean $$11 = $$10.shouldEntityAppearGlowing(sheep3);
                if ($$11) {
                    ((SheepModel) this.m_117386_()).m_102624_(this.model);
                    this.model.prepareMobModel(sheep3, float4, float5, float6);
                    this.model.setupAnim(sheep3, float4, float5, float7, float8, float9);
                    VertexConsumer $$12 = multiBufferSource1.getBuffer(RenderType.outline(SHEEP_FUR_LOCATION));
                    this.model.m_7695_(poseStack0, $$12, int2, LivingEntityRenderer.getOverlayCoords(sheep3, 0.0F), 0.0F, 0.0F, 0.0F, 1.0F);
                }
            } else {
                float $$21;
                float $$22;
                float $$23;
                if (sheep3.m_8077_() && "jeb_".equals(sheep3.m_7755_().getString())) {
                    int $$13 = 25;
                    int $$14 = sheep3.f_19797_ / 25 + sheep3.m_19879_();
                    int $$15 = DyeColor.values().length;
                    int $$16 = $$14 % $$15;
                    int $$17 = ($$14 + 1) % $$15;
                    float $$18 = ((float) (sheep3.f_19797_ % 25) + float6) / 25.0F;
                    float[] $$19 = Sheep.getColorArray(DyeColor.byId($$16));
                    float[] $$20 = Sheep.getColorArray(DyeColor.byId($$17));
                    $$21 = $$19[0] * (1.0F - $$18) + $$20[0] * $$18;
                    $$22 = $$19[1] * (1.0F - $$18) + $$20[1] * $$18;
                    $$23 = $$19[2] * (1.0F - $$18) + $$20[2] * $$18;
                } else {
                    float[] $$24 = Sheep.getColorArray(sheep3.getColor());
                    $$21 = $$24[0];
                    $$22 = $$24[1];
                    $$23 = $$24[2];
                }
                m_117359_(this.m_117386_(), this.model, SHEEP_FUR_LOCATION, poseStack0, multiBufferSource1, int2, sheep3, float4, float5, float7, float8, float9, float6, $$21, $$22, $$23);
            }
        }
    }
}