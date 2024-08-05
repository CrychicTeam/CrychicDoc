package com.mrcrayfish.configured.client.screen;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

public interface IColouredTooltip {

    List<Component> DUMMY_TOOLTIP = ImmutableList.of(CommonComponents.EMPTY);

    @Nullable
    List<FormattedCharSequence> getTooltipText();

    @Nullable
    Integer getTooltipX();

    @Nullable
    Integer getTooltipY();

    @Nullable
    Integer getTooltipOutlineColour();

    @Nullable
    Integer getTooltipBackgroundColour();

    default boolean drawColouredTooltip(PoseStack poseStack, int mouseX, int mouseY, Screen screen) {
        if (this.getTooltipText() == null) {
            return false;
        } else {
            boolean positioned = this.getTooltipX() != null && this.getTooltipY() != null;
            if (positioned) {
                int var10000 = this.getTooltipX() + 12;
            }
            if (positioned) {
                int var8 = this.getTooltipY() - 12;
            }
            return true;
        }
    }
}