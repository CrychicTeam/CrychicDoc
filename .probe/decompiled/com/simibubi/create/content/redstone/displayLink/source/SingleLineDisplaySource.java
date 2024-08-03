package com.simibubi.create.content.redstone.displayLink.source;

import com.google.common.collect.ImmutableList;
import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.target.DisplayTargetStats;
import com.simibubi.create.content.trains.display.FlapDisplayBlockEntity;
import com.simibubi.create.content.trains.display.FlapDisplayLayout;
import com.simibubi.create.content.trains.display.FlapDisplaySection;
import com.simibubi.create.foundation.gui.ModularGuiLineBuilder;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class SingleLineDisplaySource extends DisplaySource {

    protected abstract MutableComponent provideLine(DisplayLinkContext var1, DisplayTargetStats var2);

    protected abstract boolean allowsLabeling(DisplayLinkContext var1);

    @OnlyIn(Dist.CLIENT)
    @Override
    public void initConfigurationWidgets(DisplayLinkContext context, ModularGuiLineBuilder builder, boolean isFirstLine) {
        if (isFirstLine && this.allowsLabeling(context)) {
            this.addLabelingTextBox(builder);
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected void addLabelingTextBox(ModularGuiLineBuilder builder) {
        builder.addTextInput(0, 137, (e, t) -> {
            e.setValue("");
            t.withTooltip(ImmutableList.of(Lang.translateDirect("display_source.label").withStyle(s -> s.withColor(5476833)), Lang.translateDirect("gui.schedule.lmb_edit").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC)));
        }, "Label");
    }

    @Override
    public List<MutableComponent> provideText(DisplayLinkContext context, DisplayTargetStats stats) {
        MutableComponent line = this.provideLine(context, stats);
        if (line == EMPTY_LINE) {
            return EMPTY;
        } else {
            if (this.allowsLabeling(context)) {
                String label = context.sourceConfig().getString("Label");
                if (!label.isEmpty()) {
                    line = Components.literal(label + " ").append(line);
                }
            }
            return ImmutableList.of(line);
        }
    }

    @Override
    public List<List<MutableComponent>> provideFlapDisplayText(DisplayLinkContext context, DisplayTargetStats stats) {
        if (this.allowsLabeling(context)) {
            String label = context.sourceConfig().getString("Label");
            if (!label.isEmpty()) {
                return ImmutableList.of(ImmutableList.of(Components.literal(label + " "), this.provideLine(context, stats)));
            }
        }
        return super.provideFlapDisplayText(context, stats);
    }

    @Override
    public void loadFlapDisplayLayout(DisplayLinkContext context, FlapDisplayBlockEntity flapDisplay, FlapDisplayLayout layout) {
        String layoutKey = this.getFlapDisplayLayoutName(context);
        if (!this.allowsLabeling(context)) {
            if (!layout.isLayout(layoutKey)) {
                layout.configure(layoutKey, ImmutableList.of(this.createSectionForValue(context, flapDisplay.getMaxCharCount())));
            }
        } else {
            String label = context.sourceConfig().getString("Label");
            if (label.isEmpty()) {
                if (!layout.isLayout(layoutKey)) {
                    layout.configure(layoutKey, ImmutableList.of(this.createSectionForValue(context, flapDisplay.getMaxCharCount())));
                }
            } else {
                String layoutName = label.length() + "_Labeled_" + layoutKey;
                if (!layout.isLayout(layoutName)) {
                    int maxCharCount = flapDisplay.getMaxCharCount();
                    FlapDisplaySection labelSection = new FlapDisplaySection((float) Math.min(maxCharCount, label.length() + 1) * 7.0F, "alphabet", false, false);
                    if (label.length() + 1 < maxCharCount) {
                        layout.configure(layoutName, ImmutableList.of(labelSection, this.createSectionForValue(context, maxCharCount - label.length() - 1)));
                    } else {
                        layout.configure(layoutName, ImmutableList.of(labelSection));
                    }
                }
            }
        }
    }

    protected String getFlapDisplayLayoutName(DisplayLinkContext context) {
        return "Default";
    }

    protected FlapDisplaySection createSectionForValue(DisplayLinkContext context, int size) {
        return new FlapDisplaySection((float) size * 7.0F, "alphabet", false, false);
    }
}