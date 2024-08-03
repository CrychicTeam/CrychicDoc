package net.minecraft.client.gui.layouts;

import java.util.function.Consumer;
import net.minecraft.client.gui.screens.Screen;

public class HeaderAndFooterLayout implements Layout {

    private static final int DEFAULT_HEADER_AND_FOOTER_HEIGHT = 36;

    private static final int DEFAULT_CONTENT_MARGIN_TOP = 30;

    private final FrameLayout headerFrame = new FrameLayout();

    private final FrameLayout footerFrame = new FrameLayout();

    private final FrameLayout contentsFrame = new FrameLayout();

    private final Screen screen;

    private int headerHeight;

    private int footerHeight;

    public HeaderAndFooterLayout(Screen screen0) {
        this(screen0, 36);
    }

    public HeaderAndFooterLayout(Screen screen0, int int1) {
        this(screen0, int1, int1);
    }

    public HeaderAndFooterLayout(Screen screen0, int int1, int int2) {
        this.screen = screen0;
        this.headerHeight = int1;
        this.footerHeight = int2;
        this.headerFrame.defaultChildLayoutSetting().align(0.5F, 0.5F);
        this.footerFrame.defaultChildLayoutSetting().align(0.5F, 0.5F);
        this.contentsFrame.defaultChildLayoutSetting().align(0.5F, 0.0F).paddingTop(30);
    }

    @Override
    public void setX(int int0) {
    }

    @Override
    public void setY(int int0) {
    }

    @Override
    public int getX() {
        return 0;
    }

    @Override
    public int getY() {
        return 0;
    }

    @Override
    public int getWidth() {
        return this.screen.width;
    }

    @Override
    public int getHeight() {
        return this.screen.height;
    }

    public int getFooterHeight() {
        return this.footerHeight;
    }

    public void setFooterHeight(int int0) {
        this.footerHeight = int0;
    }

    public void setHeaderHeight(int int0) {
        this.headerHeight = int0;
    }

    public int getHeaderHeight() {
        return this.headerHeight;
    }

    @Override
    public void visitChildren(Consumer<LayoutElement> consumerLayoutElement0) {
        this.headerFrame.visitChildren(consumerLayoutElement0);
        this.contentsFrame.visitChildren(consumerLayoutElement0);
        this.footerFrame.visitChildren(consumerLayoutElement0);
    }

    @Override
    public void arrangeElements() {
        int $$0 = this.getHeaderHeight();
        int $$1 = this.getFooterHeight();
        this.headerFrame.setMinWidth(this.screen.width);
        this.headerFrame.setMinHeight($$0);
        this.headerFrame.m_264152_(0, 0);
        this.headerFrame.arrangeElements();
        this.footerFrame.setMinWidth(this.screen.width);
        this.footerFrame.setMinHeight($$1);
        this.footerFrame.arrangeElements();
        this.footerFrame.m_253211_(this.screen.height - $$1);
        this.contentsFrame.setMinWidth(this.screen.width);
        this.contentsFrame.setMinHeight(this.screen.height - $$0 - $$1);
        this.contentsFrame.m_264152_(0, $$0);
        this.contentsFrame.arrangeElements();
    }

    public <T extends LayoutElement> T addToHeader(T t0) {
        return this.headerFrame.addChild(t0);
    }

    public <T extends LayoutElement> T addToHeader(T t0, LayoutSettings layoutSettings1) {
        return this.headerFrame.addChild(t0, layoutSettings1);
    }

    public <T extends LayoutElement> T addToFooter(T t0) {
        return this.footerFrame.addChild(t0);
    }

    public <T extends LayoutElement> T addToFooter(T t0, LayoutSettings layoutSettings1) {
        return this.footerFrame.addChild(t0, layoutSettings1);
    }

    public <T extends LayoutElement> T addToContents(T t0) {
        return this.contentsFrame.addChild(t0);
    }

    public <T extends LayoutElement> T addToContents(T t0, LayoutSettings layoutSettings1) {
        return this.contentsFrame.addChild(t0, layoutSettings1);
    }

    public LayoutSettings newHeaderLayoutSettings() {
        return this.headerFrame.newChildLayoutSettings();
    }

    public LayoutSettings newContentLayoutSettings() {
        return this.contentsFrame.newChildLayoutSettings();
    }

    public LayoutSettings newFooterLayoutSettings() {
        return this.footerFrame.newChildLayoutSettings();
    }
}