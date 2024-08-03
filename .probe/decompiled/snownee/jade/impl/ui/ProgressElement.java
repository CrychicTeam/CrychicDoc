package snownee.jade.impl.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.theme.IThemeHelper;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.api.ui.Element;
import snownee.jade.api.ui.IBoxStyle;
import snownee.jade.api.ui.IProgressStyle;
import snownee.jade.overlay.ProgressTracker;
import snownee.jade.overlay.WailaTickHandler;

public class ProgressElement extends Element {

    private final float progress;

    @Nullable
    private final Component text;

    private final IProgressStyle style;

    private final IBoxStyle boxStyle;

    private ProgressTracker.TrackInfo track;

    private boolean canDecrease;

    public ProgressElement(float progress, Component text, IProgressStyle style, IBoxStyle boxStyle, boolean canDecrease) {
        this.progress = Mth.clamp(progress, 0.0F, 1.0F);
        this.text = text;
        this.style = style;
        if (boxStyle == BoxStyle.DEFAULT && IThemeHelper.get().isLightColorScheme()) {
            BoxStyle newStyle = new BoxStyle();
            newStyle.borderWidth = BoxStyle.DEFAULT.borderWidth;
            newStyle.bgColor = 1145324612;
            boxStyle = newStyle;
        }
        this.boxStyle = boxStyle;
        this.canDecrease = canDecrease;
    }

    @Override
    public Vec2 getSize() {
        int height = this.text == null ? 8 : 14;
        float width = 0.0F;
        width += this.boxStyle.borderWidth() * 2.0F;
        if (this.text != null) {
            Font font = Minecraft.getInstance().font;
            width += (float) (font.width(this.text) + 3);
        }
        width = Math.max(20.0F, width);
        if (this.getTag() != null) {
            this.track = WailaTickHandler.instance().progressTracker.createInfo(this.getTag(), this.progress, this.canDecrease, width);
            width = this.track.getWidth();
        }
        return new Vec2(width, (float) height);
    }

    @Override
    public void render(GuiGraphics guiGraphics, float x, float y, float maxX, float maxY) {
        Vec2 size = this.getCachedSize();
        float b = this.boxStyle.borderWidth();
        this.boxStyle.render(guiGraphics, x, y, maxX - x, size.y - 2.0F);
        float progress = this.progress;
        if (this.track == null && this.getTag() != null) {
            this.track = WailaTickHandler.instance().progressTracker.createInfo(this.getTag(), progress, this.canDecrease, this.getSize().y);
        }
        if (this.track != null) {
            progress = this.track.tick(Minecraft.getInstance().getDeltaFrameTime());
        }
        this.style.render(guiGraphics, x + b, y + b, maxX - x - b * 2.0F, size.y - b * 2.0F - 2.0F, progress, this.text);
    }

    @Nullable
    @Override
    public String getMessage() {
        return this.text == null ? null : this.text.getString();
    }
}