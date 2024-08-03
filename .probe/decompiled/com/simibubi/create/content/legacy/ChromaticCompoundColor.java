package com.simibubi.create.content.legacy;

import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

public class ChromaticCompoundColor implements ItemColor {

    @Override
    public int getColor(ItemStack stack, int layer) {
        Minecraft mc = Minecraft.getInstance();
        float pt = AnimationTickHolder.getPartialTicks();
        float progress = (float) ((double) (mc.player.getViewYRot(pt) / 180.0F) * Math.PI) + AnimationTickHolder.getRenderTime() / 10.0F;
        if (layer == 0) {
            return Color.mixColors(7231347, 7024756, (Mth.sin(progress) + 1.0F) / 2.0F);
        } else if (layer == 1) {
            return Color.mixColors(13917561, 7231347, (Mth.sin((float) ((double) progress + Math.PI)) + 1.0F) / 2.0F);
        } else {
            return layer == 2 ? Color.mixColors(15372421, 13917561, (Mth.sin((float) ((double) (progress * 1.5F) + Math.PI)) + 1.0F) / 2.0F) : 0;
        }
    }
}