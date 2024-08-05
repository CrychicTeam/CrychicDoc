package net.minecraft.client.gui.components;

import java.util.OptionalInt;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.SingleKeyCache;

public class MultiLineTextWidget extends AbstractStringWidget {

    private OptionalInt maxWidth = OptionalInt.empty();

    private OptionalInt maxRows = OptionalInt.empty();

    private final SingleKeyCache<MultiLineTextWidget.CacheKey, MultiLineLabel> cache;

    private boolean centered = false;

    public MultiLineTextWidget(Component component0, Font font1) {
        this(0, 0, component0, font1);
    }

    public MultiLineTextWidget(int int0, int int1, Component component2, Font font3) {
        super(int0, int1, 0, 0, component2, font3);
        this.cache = Util.singleKeyCache(p_270516_ -> p_270516_.maxRows.isPresent() ? MultiLineLabel.create(font3, p_270516_.message, p_270516_.maxWidth, p_270516_.maxRows.getAsInt()) : MultiLineLabel.create(font3, p_270516_.message, p_270516_.maxWidth));
        this.f_93623_ = false;
    }

    public MultiLineTextWidget setColor(int int0) {
        super.setColor(int0);
        return this;
    }

    public MultiLineTextWidget setMaxWidth(int int0) {
        this.maxWidth = OptionalInt.of(int0);
        return this;
    }

    public MultiLineTextWidget setMaxRows(int int0) {
        this.maxRows = OptionalInt.of(int0);
        return this;
    }

    public MultiLineTextWidget setCentered(boolean boolean0) {
        this.centered = boolean0;
        return this;
    }

    @Override
    public int getWidth() {
        return this.cache.getValue(this.getFreshCacheKey()).getWidth();
    }

    @Override
    public int getHeight() {
        return this.cache.getValue(this.getFreshCacheKey()).getLineCount() * 9;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        MultiLineLabel $$4 = this.cache.getValue(this.getFreshCacheKey());
        int $$5 = this.m_252754_();
        int $$6 = this.m_252907_();
        int $$7 = 9;
        int $$8 = this.m_269468_();
        if (this.centered) {
            $$4.renderCentered(guiGraphics0, $$5 + this.getWidth() / 2, $$6, $$7, $$8);
        } else {
            $$4.renderLeftAligned(guiGraphics0, $$5, $$6, $$7, $$8);
        }
    }

    private MultiLineTextWidget.CacheKey getFreshCacheKey() {
        return new MultiLineTextWidget.CacheKey(this.m_6035_(), this.maxWidth.orElse(Integer.MAX_VALUE), this.maxRows);
    }

    static record CacheKey(Component f_268701_, int f_268646_, OptionalInt f_268550_) {

        private final Component message;

        private final int maxWidth;

        private final OptionalInt maxRows;

        CacheKey(Component f_268701_, int f_268646_, OptionalInt f_268550_) {
            this.message = f_268701_;
            this.maxWidth = f_268646_;
            this.maxRows = f_268550_;
        }
    }
}