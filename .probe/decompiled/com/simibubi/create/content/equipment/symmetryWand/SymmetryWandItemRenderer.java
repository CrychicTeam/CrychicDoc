package com.simibubi.create.content.equipment.symmetryWand;

import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModel;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModelRenderer;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class SymmetryWandItemRenderer extends CustomRenderedItemModelRenderer {

    protected static final PartialModel BITS = new PartialModel(Create.asResource("item/wand_of_symmetry/bits"));

    protected static final PartialModel CORE = new PartialModel(Create.asResource("item/wand_of_symmetry/core"));

    protected static final PartialModel CORE_GLOW = new PartialModel(Create.asResource("item/wand_of_symmetry/core_glow"));

    @Override
    protected void render(ItemStack stack, CustomRenderedItemModel model, PartialItemModelRenderer renderer, ItemDisplayContext transformType, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        float worldTime = AnimationTickHolder.getRenderTime() / 20.0F;
        int maxLight = 15728880;
        renderer.render(model.getOriginalModel(), light);
        renderer.renderSolidGlowing(CORE.get(), maxLight);
        renderer.renderGlowing(CORE_GLOW.get(), maxLight);
        float floating = Mth.sin(worldTime) * 0.05F;
        float angle = worldTime * -10.0F % 360.0F;
        ms.translate(0.0F, floating, 0.0F);
        ms.mulPose(Axis.YP.rotationDegrees(angle));
        renderer.renderGlowing(BITS.get(), maxLight);
    }
}