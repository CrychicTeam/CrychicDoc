package net.minecraft.client.gui.screens.inventory.tooltip;

import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.FormattedCharSequence;
import org.joml.Matrix4f;

public class ClientTextTooltip implements ClientTooltipComponent {

    private final FormattedCharSequence text;

    public ClientTextTooltip(FormattedCharSequence formattedCharSequence0) {
        this.text = formattedCharSequence0;
    }

    @Override
    public int getWidth(Font font0) {
        return font0.width(this.text);
    }

    @Override
    public int getHeight() {
        return 10;
    }

    @Override
    public void renderText(Font font0, int int1, int int2, Matrix4f matrixF3, MultiBufferSource.BufferSource multiBufferSourceBufferSource4) {
        font0.drawInBatch(this.text, (float) int1, (float) int2, -1, true, matrixF3, multiBufferSourceBufferSource4, Font.DisplayMode.NORMAL, 0, 15728880);
    }
}