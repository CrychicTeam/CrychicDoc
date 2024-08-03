package com.simibubi.create.content.redstone.diodes;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.blockEntity.renderer.ColoredOverlayBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.Color;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class BrassDiodeRenderer extends ColoredOverlayBlockEntityRenderer<BrassDiodeBlockEntity> {

    public BrassDiodeRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    protected int getColor(BrassDiodeBlockEntity be, float partialTicks) {
        return Color.mixColors(2884352, 13434880, be.getProgress());
    }

    protected SuperByteBuffer getOverlayBuffer(BrassDiodeBlockEntity be) {
        return CachedBufferer.partial(AllPartialModels.FLEXPEATER_INDICATOR, be.m_58900_());
    }
}