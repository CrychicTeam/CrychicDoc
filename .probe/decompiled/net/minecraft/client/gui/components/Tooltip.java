package net.minecraft.client.gui.components;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.narration.NarrationSupplier;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

public class Tooltip implements NarrationSupplier {

    private static final int MAX_WIDTH = 170;

    private final Component message;

    @Nullable
    private List<FormattedCharSequence> cachedTooltip;

    @Nullable
    private final Component narration;

    private Tooltip(Component component0, @Nullable Component component1) {
        this.message = component0;
        this.narration = component1;
    }

    public static Tooltip create(Component component0, @Nullable Component component1) {
        return new Tooltip(component0, component1);
    }

    public static Tooltip create(Component component0) {
        return new Tooltip(component0, component0);
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput0) {
        if (this.narration != null) {
            narrationElementOutput0.add(NarratedElementType.HINT, this.narration);
        }
    }

    public List<FormattedCharSequence> toCharSequence(Minecraft minecraft0) {
        if (this.cachedTooltip == null) {
            this.cachedTooltip = splitTooltip(minecraft0, this.message);
        }
        return this.cachedTooltip;
    }

    public static List<FormattedCharSequence> splitTooltip(Minecraft minecraft0, Component component1) {
        return minecraft0.font.split(component1, 170);
    }
}