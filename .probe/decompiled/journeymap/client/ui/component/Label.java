package journeymap.client.ui.component;

import journeymap.client.Constants;
import journeymap.client.render.draw.DrawUtil;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public class Label extends Button {

    private DrawUtil.HAlign hAlign = DrawUtil.HAlign.Left;

    public Label(int width, String key, Object... labelArgs) {
        super(Constants.getString(key, labelArgs));
        this.setDrawBackground(false);
        this.setDrawFrame(false);
        this.setEnabled(false);
        this.setLabelColors(Integer.valueOf(12632256), Integer.valueOf(12632256), Integer.valueOf(12632256));
        this.m_93674_(width);
    }

    @Override
    public int getFitWidth(Font fr) {
        return this.fontRenderer.width(this.m_6035_().getString());
    }

    @Override
    public void fitWidth(Font fr) {
    }

    public void setHAlign(DrawUtil.HAlign hAlign) {
        this.hAlign = hAlign;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float ticks) {
        DrawUtil.drawLabel(graphics, this.m_6035_().getString(), (double) (switch(this.hAlign) {
            case Left ->
                this.getRightX();
            case Right ->
                this.m_252754_();
            default ->
                this.getCenterX();
        }), (double) this.getMiddleY(), this.hAlign, DrawUtil.VAlign.Middle, null, 0.0F, this.labelColor, 1.0F, 1.0, this.drawLabelShadow);
    }
}