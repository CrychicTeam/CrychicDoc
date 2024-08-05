package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.TabulaModel;
import com.github.alexthe666.iceandfire.client.model.util.TabulaModelHandlerHelper;
import com.github.alexthe666.iceandfire.client.render.TabulaModelAccessor;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import com.github.alexthe666.iceandfire.entity.EntityLightningDragon;
import com.github.alexthe666.iceandfire.enums.EnumDragonTextures;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class LayerDragonEyes extends RenderLayer<EntityDragonBase, AdvancedEntityModel<EntityDragonBase>> {

    private final MobRenderer render;

    private TabulaModel fireHead;

    private TabulaModel iceHead;

    private TabulaModel lightningHead;

    public LayerDragonEyes(MobRenderer renderIn) {
        super(renderIn);
        this.render = renderIn;
        try {
            this.fireHead = this.onlyKeepCubes(new TabulaModelAccessor(TabulaModelHandlerHelper.loadTabulaModel("/assets/iceandfire/models/tabula/firedragon/firedragon_Ground"), null), Collections.singletonList("HeadFront"));
            this.iceHead = this.onlyKeepCubes(new TabulaModelAccessor(TabulaModelHandlerHelper.loadTabulaModel("/assets/iceandfire/models/tabula/icedragon/icedragon_Ground"), null), Collections.singletonList("HeadFront"));
            this.lightningHead = this.onlyKeepCubes(new TabulaModelAccessor(TabulaModelHandlerHelper.loadTabulaModel("/assets/iceandfire/models/tabula/lightningdragon/lightningdragon_Ground"), null), Collections.singletonList("HeadFront"));
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    public void render(@NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn, EntityDragonBase dragon, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (dragon.shouldRenderEyes()) {
            RenderType eyes = RenderType.eyes(EnumDragonTextures.getEyeTextureFromDragon(dragon));
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(eyes);
            if (dragon instanceof EntityLightningDragon && this.lightningHead != null) {
                this.copyPositions(this.lightningHead, (TabulaModel) this.m_117386_());
                this.lightningHead.m_7695_(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            } else if (dragon instanceof EntityIceDragon && this.iceHead != null) {
                this.copyPositions(this.iceHead, (TabulaModel) this.m_117386_());
                this.iceHead.m_7695_(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            } else if (this.fireHead != null) {
                this.copyPositions(this.fireHead, (TabulaModel) this.m_117386_());
                this.fireHead.m_7695_(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            } else {
                ((AdvancedEntityModel) this.m_117386_()).m_7695_(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }

    @NotNull
    protected ResourceLocation getTextureLocation(@NotNull EntityDragonBase entityIn) {
        return null;
    }

    private TabulaModel onlyKeepCubes(TabulaModelAccessor model, List<String> strings) {
        List<AdvancedModelBox> keepCubes = new ArrayList();
        for (String str : strings) {
            AdvancedModelBox cube = model.getCube(str);
            keepCubes.add(cube);
            while (cube.getParent() != null) {
                keepCubes.add(cube.getParent());
                cube = cube.getParent();
            }
        }
        this.removeChildren(model, keepCubes);
        model.getCubes().values().removeIf(advancedModelBox -> !keepCubes.contains(advancedModelBox));
        return model;
    }

    private void removeChildren(TabulaModelAccessor model, List<AdvancedModelBox> keepCubes) {
        model.getRootBox().forEach(modelRenderer -> {
            modelRenderer.childModels.removeIf(child -> !keepCubes.contains(child));
            modelRenderer.childModels.forEach(childModel -> this.removeChildren((AdvancedModelBox) childModel, keepCubes));
        });
    }

    private void removeChildren(AdvancedModelBox modelBox, List<AdvancedModelBox> keepCubes) {
        modelBox.childModels.removeIf(modelRenderer -> !keepCubes.contains(modelRenderer));
        modelBox.childModels.forEach(modelRenderer -> this.removeChildren((AdvancedModelBox) modelRenderer, keepCubes));
    }

    public boolean isAngleEqual(AdvancedModelBox original, AdvancedModelBox pose) {
        return pose != null && pose.rotateAngleX == original.rotateAngleX && pose.rotateAngleY == original.rotateAngleY && pose.rotateAngleZ == original.rotateAngleZ;
    }

    public boolean isPositionEqual(AdvancedModelBox original, AdvancedModelBox pose) {
        return pose.rotationPointX == original.rotationPointX && pose.rotationPointY == original.rotationPointY && pose.rotationPointZ == original.rotationPointZ;
    }

    public void copyPositions(TabulaModel model, TabulaModel modelTo) {
        for (AdvancedModelBox cube : model.getCubes().values()) {
            AdvancedModelBox modelToCube = modelTo.getCube(cube.boxName);
            if (!this.isAngleEqual(cube, modelToCube)) {
                cube.rotateAngleX = modelToCube.rotateAngleX;
                cube.rotateAngleY = modelToCube.rotateAngleY;
                cube.rotateAngleZ = modelToCube.rotateAngleZ;
            }
            if (!this.isPositionEqual(cube, modelToCube)) {
                cube.rotationPointX = modelToCube.rotationPointX;
                cube.rotationPointY = modelToCube.rotationPointY;
                cube.rotationPointZ = modelToCube.rotationPointZ;
            }
        }
    }

    public boolean shouldCombineTextures() {
        return true;
    }
}