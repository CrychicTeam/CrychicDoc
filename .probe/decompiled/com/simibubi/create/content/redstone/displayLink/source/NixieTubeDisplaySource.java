package com.simibubi.create.content.redstone.displayLink.source;

import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.target.DisplayTargetStats;
import com.simibubi.create.content.redstone.displayLink.target.NixieTubeDisplayTarget;
import com.simibubi.create.content.redstone.nixieTube.NixieTubeBlockEntity;
import com.simibubi.create.content.trains.display.FlapDisplaySection;
import net.minecraft.network.chat.MutableComponent;

public class NixieTubeDisplaySource extends SingleLineDisplaySource {

    @Override
    protected String getTranslationKey() {
        return "nixie_tube";
    }

    @Override
    protected MutableComponent provideLine(DisplayLinkContext context, DisplayTargetStats stats) {
        if (context.getSourceBlockEntity() instanceof NixieTubeBlockEntity nbe) {
            MutableComponent text = nbe.getFullText();
            try {
                String line = text.getString();
                Integer.valueOf(line);
                context.flapDisplayContext = Boolean.TRUE;
            } catch (NumberFormatException var7) {
            }
            return text;
        } else {
            return EMPTY_LINE;
        }
    }

    @Override
    protected boolean allowsLabeling(DisplayLinkContext context) {
        return !(context.blockEntity().activeTarget instanceof NixieTubeDisplayTarget);
    }

    @Override
    protected String getFlapDisplayLayoutName(DisplayLinkContext context) {
        return this.isNumeric(context) ? "Number" : super.getFlapDisplayLayoutName(context);
    }

    @Override
    protected FlapDisplaySection createSectionForValue(DisplayLinkContext context, int size) {
        return this.isNumeric(context) ? new FlapDisplaySection((float) size * 7.0F, "numeric", false, false) : super.createSectionForValue(context, size);
    }

    protected boolean isNumeric(DisplayLinkContext context) {
        return context.flapDisplayContext == Boolean.TRUE;
    }
}