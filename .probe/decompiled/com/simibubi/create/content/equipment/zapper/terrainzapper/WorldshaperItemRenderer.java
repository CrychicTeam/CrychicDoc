package com.simibubi.create.content.equipment.zapper.terrainzapper;

import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.Create;
import com.simibubi.create.content.equipment.zapper.ZapperItemRenderer;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModel;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class WorldshaperItemRenderer extends ZapperItemRenderer {

    protected static final PartialModel CORE = new PartialModel(Create.asResource("item/handheld_worldshaper/core"));

    protected static final PartialModel CORE_GLOW = new PartialModel(Create.asResource("item/handheld_worldshaper/core_glow"));

    protected static final PartialModel ACCELERATOR = new PartialModel(Create.asResource("item/handheld_worldshaper/accelerator"));

    @Override
    protected void render(ItemStack stack, CustomRenderedItemModel model, PartialItemModelRenderer renderer, ItemDisplayContext transformType, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.render(stack, model, renderer, transformType, ms, buffer, light, overlay);
        float pt = AnimationTickHolder.getPartialTicks();
        float worldTime = AnimationTickHolder.getRenderTime() / 20.0F;
        renderer.renderSolid(model.getOriginalModel(), light);
        LocalPlayer player = Minecraft.getInstance().player;
        boolean leftHanded = player.m_5737_() == HumanoidArm.LEFT;
        boolean mainHand = player.m_21205_() == stack;
        boolean offHand = player.m_21206_() == stack;
        float animation = this.getAnimationProgress(pt, leftHanded, mainHand);
        float multiplier;
        if (!mainHand && !offHand) {
            multiplier = Mth.sin(worldTime * 5.0F);
        } else {
            multiplier = animation;
        }
        int lightItensity = (int) (15.0F * Mth.clamp(multiplier, 0.0F, 1.0F));
        int glowLight = LightTexture.pack(lightItensity, Math.max(lightItensity, 4));
        renderer.renderSolidGlowing(CORE.get(), glowLight);
        renderer.renderGlowing(CORE_GLOW.get(), glowLight);
        float angle = worldTime * -25.0F;
        if (mainHand || offHand) {
            angle += 360.0F * animation;
        }
        angle %= 360.0F;
        float offset = -0.155F;
        ms.translate(0.0F, offset, 0.0F);
        ms.mulPose(Axis.ZP.rotationDegrees(angle));
        ms.translate(0.0F, -offset, 0.0F);
        renderer.render(ACCELERATOR.get(), light);
    }
}