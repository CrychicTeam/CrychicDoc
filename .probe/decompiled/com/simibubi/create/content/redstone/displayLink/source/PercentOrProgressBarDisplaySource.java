package com.simibubi.create.content.redstone.displayLink.source;

import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.target.DisplayTargetStats;
import com.simibubi.create.content.trains.display.FlapDisplayBlockEntity;
import com.simibubi.create.content.trains.display.FlapDisplaySection;
import com.simibubi.create.foundation.utility.Components;
import javax.annotation.Nullable;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.SignBlockEntity;

public abstract class PercentOrProgressBarDisplaySource extends NumericSingleLineDisplaySource {

    @Override
    protected MutableComponent provideLine(DisplayLinkContext context, DisplayTargetStats stats) {
        Float currentLevel = this.getProgress(context);
        if (currentLevel == null) {
            return EMPTY_LINE;
        } else if (!this.progressBarActive(context)) {
            return this.formatNumeric(context, currentLevel);
        } else {
            String label = context.sourceConfig().getString("Label");
            int labelSize = label.isEmpty() ? 0 : label.length() + 1;
            int length = Math.min(stats.maxColumns() - labelSize, 128);
            if (context.getTargetBlockEntity() instanceof SignBlockEntity) {
                length = (int) ((float) length * 6.0F / 9.0F);
            }
            if (context.getTargetBlockEntity() instanceof FlapDisplayBlockEntity) {
                length = this.sizeForWideChars(length);
            }
            int filledLength = (int) (currentLevel * (float) length);
            if (length < 1) {
                return EMPTY_LINE;
            } else {
                StringBuilder s = new StringBuilder();
                int emptySpaces = length - filledLength;
                for (int i = 0; i < filledLength; i++) {
                    s.append("█");
                }
                for (int i = 0; i < emptySpaces; i++) {
                    s.append("▒");
                }
                return Components.literal(s.toString());
            }
        }
    }

    protected MutableComponent formatNumeric(DisplayLinkContext context, Float currentLevel) {
        return Components.literal(Mth.clamp((int) (currentLevel * 100.0F), 0, 100) + "%");
    }

    @Nullable
    protected abstract Float getProgress(DisplayLinkContext var1);

    protected abstract boolean progressBarActive(DisplayLinkContext var1);

    @Override
    protected String getFlapDisplayLayoutName(DisplayLinkContext context) {
        return !this.progressBarActive(context) ? super.getFlapDisplayLayoutName(context) : "Progress";
    }

    @Override
    protected FlapDisplaySection createSectionForValue(DisplayLinkContext context, int size) {
        return !this.progressBarActive(context) ? super.createSectionForValue(context, size) : new FlapDisplaySection((float) size * 7.0F, "pixel", false, false).wideFlaps();
    }

    private int sizeForWideChars(int size) {
        return (int) ((float) size * 7.0F / 9.0F);
    }
}