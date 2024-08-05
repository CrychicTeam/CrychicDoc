package journeymap.client.ui.component;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.awt.Color;
import java.awt.geom.Point2D.Double;
import java.util.List;
import java.util.Optional;
import journeymap.client.render.RenderWrapper;
import journeymap.client.render.draw.DrawUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class ScrollPane extends ObjectSelectionList<ScrollPane.ScrollPaneEntry> {

    public int paneWidth = 0;

    public int paneHeight = 0;

    public Double origin = new Double();

    protected Button selected = null;

    private Integer frameColor = new Color(-6250336).getRGB();

    private Minecraft mc;

    private int _mouseX;

    private int _mouseY;

    private boolean showFrame = true;

    private int firstVisibleIndex;

    private int lastVisibleIndex;

    private boolean drawPartialScrollable = true;

    private boolean renderSolidBackground = false;

    private boolean renderDecorations = true;

    private boolean scrollVisible = true;

    public ScrollPane(JmUI parent, Minecraft mc, int width, int height, List<? extends Button> items, int itemHeight, int itemGap) {
        super(mc, width, height, 16, height, itemHeight + itemGap);
        for (Button item : items) {
            super.m_7085_(new ScrollPane.ScrollPaneEntry(this, item));
        }
        this.paneWidth = width;
        this.paneHeight = height;
        this.mc = mc;
    }

    public int getX() {
        return (int) this.origin.getX();
    }

    public int getY() {
        return (int) this.origin.getY();
    }

    public int getSlotHeight() {
        return super.f_93389_;
    }

    public void setDrawPartialScrollable(boolean value) {
        this.drawPartialScrollable = value;
    }

    public void setDimensions(int width, int height, int marginTop, int marginBottom, int x, int y) {
        super.m_93437_(width, height, y, y + height);
        this.paneWidth = width;
        this.paneHeight = height;
        this.origin.setLocation((double) x, (double) y);
        this.m_93507_(x);
    }

    @Override
    protected int getItemCount() {
        return super.m_5773_();
    }

    protected ScrollPane.ScrollPaneEntry getEntry(int index) {
        return (ScrollPane.ScrollPaneEntry) super.m_93500_(index);
    }

    @Override
    public boolean isSelectedItem(int i) {
        return super.m_7987_(i);
    }

    public Button mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            for (ScrollPane.ScrollPaneEntry entry : super.m_6702_()) {
                Button item = entry.item;
                if (item != null && this.inFullView(item)) {
                    if (item.isHovered() && item.mouseClicked((double) mouseX, (double) mouseY, mouseButton)) {
                        item.onPress();
                        return item;
                    }
                    item.clickScrollable(this.mc, mouseX, mouseY);
                }
            }
        }
        return null;
    }

    public Button getButton(int mouseX, int mouseY) {
        for (ScrollPane.ScrollPaneEntry entry : super.m_6702_()) {
            Button item = entry.item;
            if (item != null && this.inFullView(item) && item.isHovered() && item.mouseOver((double) mouseX, (double) mouseY)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public void render(GuiGraphics graphics, int mX, int mY, float partialTicks) {
        this._mouseX = mX;
        this._mouseY = mY;
        this.firstVisibleIndex = -1;
        this.lastVisibleIndex = -1;
        super.m_93488_(false);
        super.m_93496_(false);
        graphics.pose().pushPose();
        RenderWrapper.enableDepthTest();
        super.m_88315_(graphics, mX, mY, partialTicks);
        graphics.pose().popPose();
    }

    @Override
    protected void renderBackground(GuiGraphics graphics) {
        int width = this.getWidth();
        float alpha = 0.4F;
        DrawUtil.drawRectangle(graphics.pose(), (double) this.getX(), (double) this.getY(), (double) width, (double) this.paneHeight, Color.BLACK.getRGB(), this.renderSolidBackground ? 1.0F : alpha);
        this.scrollVisible = 0 < this.getMaxScroll();
        if (this.scrollVisible) {
            DrawUtil.drawRectangle(graphics.pose(), (double) (this.getX() + width - 6), (double) this.f_93390_, 5.0, (double) this.paneHeight, Color.BLACK.getRGB(), alpha);
        }
        if (this.showFrame) {
            alpha = 1.0F;
            DrawUtil.drawRectangle(graphics.pose(), (double) (this.getX() - 1), (double) (this.getY() - 1), (double) (width + 2), 1.0, this.frameColor, alpha);
            DrawUtil.drawRectangle(graphics.pose(), (double) (this.getX() - 1), (double) (this.getY() + this.paneHeight), (double) (width + 2), 1.0, this.frameColor, alpha);
            DrawUtil.drawRectangle(graphics.pose(), (double) (this.getX() - 1), (double) (this.getY() - 1), 1.0, (double) (this.paneHeight + 1), this.frameColor, alpha);
            DrawUtil.drawRectangle(graphics.pose(), (double) (width + (this.scrollVisible ? 1 : 0) + this.getX()), (double) (this.getY() - 1), 1.0, (double) (this.paneHeight + 2), this.frameColor, alpha);
        }
    }

    public void renderItem(GuiGraphics graphics, int index, int y, int x, int l, int var6, int var7, float f) {
        if (this.firstVisibleIndex == -1) {
            this.firstVisibleIndex = index;
        }
        this.lastVisibleIndex = Math.max(this.lastVisibleIndex, index);
        int margin = 4;
        int itemX = this.getX() + 2;
        Button item = ((ScrollPane.ScrollPaneEntry) super.m_93500_(index)).item;
        item.setScrollablePosition(itemX, y);
        item.setScrollableWidth(this.paneWidth - 4);
        if (this.inFullView(item)) {
            item.drawScrollable(graphics, this.mc, this._mouseX, this._mouseY);
            item.renderSpecialDecoration(graphics, this._mouseX, this._mouseY, this.getX(), this.getY(), this.getWidth(), this.f_93389_);
        } else {
            int paneBottomY = this.getY() + this.paneHeight;
            int itemBottomY = y + item.getButtonHeight();
            Integer drawY = null;
            int yDiff = 0;
            if (y < this.getY() && itemBottomY > this.getY()) {
                drawY = this.getY();
                yDiff = drawY - y;
            } else if (y < paneBottomY && itemBottomY > paneBottomY) {
                drawY = y;
                yDiff = itemBottomY - paneBottomY;
            }
            if (drawY != null && this.drawPartialScrollable) {
                item.drawPartialScrollable(graphics, this.mc, itemX, drawY, item.getScrollableWidth(), item.getButtonHeight() - yDiff);
            }
        }
    }

    @Override
    protected void renderDecorations(GuiGraphics graphics, int x, int y) {
        if (this.renderDecorations) {
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder bufferbuilder = tesselator.getBuilder();
            RenderWrapper.depthFunc(515);
            RenderWrapper.disableDepthTest();
            RenderWrapper.enableBlend();
            RenderWrapper.blendFuncSeparate(770, 771, 0, 1);
            RenderWrapper.disableTexture();
            RenderWrapper.setShader(GameRenderer::m_172811_);
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
            bufferbuilder.m_5483_((double) this.f_93393_, (double) (this.f_93390_ + 5), 0.0).color(0, 0, 0, 0).endVertex();
            bufferbuilder.m_5483_((double) this.f_93392_, (double) (this.f_93390_ + 5), 0.0).color(0, 0, 0, 0).endVertex();
            bufferbuilder.m_5483_((double) this.f_93392_, (double) this.f_93390_, 0.0).color(0, 0, 0, 255).endVertex();
            bufferbuilder.m_5483_((double) this.f_93393_, (double) this.f_93390_, 0.0).color(0, 0, 0, 255).endVertex();
            bufferbuilder.m_5483_((double) this.f_93393_, (double) this.f_93391_, 0.0).color(0, 0, 0, 255).endVertex();
            bufferbuilder.m_5483_((double) this.f_93392_, (double) this.f_93391_, 0.0).color(0, 0, 0, 255).endVertex();
            bufferbuilder.m_5483_((double) this.f_93392_, (double) (this.f_93391_ - 5), 0.0).color(0, 0, 0, 0).endVertex();
            bufferbuilder.m_5483_((double) this.f_93393_, (double) (this.f_93391_ - 5), 0.0).color(0, 0, 0, 0).endVertex();
            tesselator.end();
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double mouseDX, double mouseDY) {
        if (this.m_6702_().isEmpty()) {
            return false;
        } else if (button == 0 && this.f_93399_) {
            if (mouseY < (double) this.f_93390_) {
                this.m_93410_(0.0);
            } else {
                double d0 = (double) Math.max(1, this.getMaxScroll());
                int i = this.f_93391_ - this.f_93390_;
                int j = Mth.clamp((int) ((float) (i * i) / (float) this.getMaxPosition()), 32, i - 8);
                double d1 = Math.max(1.0, d0 / (double) (i - j));
                this.m_93410_(this.m_93517_() + mouseDY * d1);
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean isScrollVisible() {
        return this.scrollVisible;
    }

    public boolean inFullView(Button item) {
        return item.m_252907_() >= this.getY() && item.m_252907_() + item.getButtonHeight() <= this.getY() + this.paneHeight;
    }

    @Override
    protected int getScrollbarPosition() {
        return this.paneWidth + this.getX();
    }

    @Override
    protected void updateScrollingState(double xPos, double yPos, int button) {
        double rightX = (double) (this.getX() + this.getWidth() - 5);
        this.f_93399_ = button == 0 && xPos >= rightX && xPos < rightX + 10.0;
    }

    @Override
    protected int getMaxPosition() {
        return super.m_5775_();
    }

    public int getWidth() {
        boolean scrollVisible = 0 < this.getMaxScroll();
        return this.paneWidth + (scrollVisible ? 5 : 0);
    }

    public int getFitWidth(Font fr) {
        int fit = 0;
        for (ScrollPane.ScrollPaneEntry entry : this.m_6702_()) {
            fit = Math.max(fit, entry.item.getFitWidth(fr));
        }
        return fit;
    }

    @Override
    public int getMaxScroll() {
        return super.m_93518_();
    }

    @Override
    public boolean isMouseOver(double posX, double posY) {
        int topY = this.getY();
        int bottomY = this.getY() + this.paneHeight;
        int leftX = this.getX();
        int rightX = this.getX() + this.getWidth();
        return posY >= (double) topY && posY <= (double) bottomY && posX >= (double) leftX && posX <= (double) rightX;
    }

    @Override
    public Optional<GuiEventListener> getChildAt(double mouseX, double mouseY) {
        return Optional.empty();
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        for (ScrollPane.ScrollPaneEntry entry : super.m_6702_()) {
            Button item = entry.item;
            if (item != null && this.inFullView(item)) {
                item.mouseMoved(mouseX, mouseY);
            }
        }
        super.m_94757_(mouseX, mouseY);
    }

    public void setRenderSolidBackground(boolean value) {
        this.renderSolidBackground = value;
    }

    public void setRenderDecorations(boolean value) {
        this.renderDecorations = value;
    }

    class ScrollPaneEntry extends ObjectSelectionList.Entry<ScrollPane.ScrollPaneEntry> {

        private ScrollPane parent;

        private Button item;

        public ScrollPaneEntry(ScrollPane parent, Button item) {
            this.parent = parent;
            this.item = item;
        }

        @Override
        public void render(GuiGraphics graphics, int index, int y, int x, int l, int var6, int var7, int p_230432_8_, boolean p_230432_9_, float f) {
            this.parent.renderItem(graphics, index, y, x, l, var6, var7, f);
        }

        @Override
        public Component getNarration() {
            return null;
        }
    }
}