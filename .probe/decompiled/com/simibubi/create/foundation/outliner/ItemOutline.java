package com.simibubi.create.foundation.outliner;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.render.SuperRenderTypeBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class ItemOutline extends Outline {

    protected Vec3 pos;

    protected ItemStack stack;

    public ItemOutline(Vec3 pos, ItemStack stack) {
        this.pos = pos;
        this.stack = stack;
    }

    @Override
    public void render(PoseStack ms, SuperRenderTypeBuffer buffer, Vec3 camera, float pt) {
        Minecraft mc = Minecraft.getInstance();
        ms.pushPose();
        ((TransformStack) TransformStack.cast(ms).translate(this.pos.x - camera.x, this.pos.y - camera.y, this.pos.z - camera.z)).scale(this.params.alpha);
        mc.getItemRenderer().render(this.stack, ItemDisplayContext.FIXED, false, ms, buffer, 15728880, OverlayTexture.NO_OVERLAY, mc.getItemRenderer().getModel(this.stack, null, null, 0));
        ms.popPose();
    }
}