package com.simibubi.create.content.trains.station;

import java.util.List;
import net.minecraft.client.StringSplitter;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import org.joml.Matrix4f;

public class NoShadowFontWrapper extends Font {

    private Font wrapped;

    public NoShadowFontWrapper(Font wrapped) {
        super(null, false);
        this.wrapped = wrapped;
    }

    @Override
    public FontSet getFontSet(ResourceLocation pFontLocation) {
        return this.wrapped.getFontSet(pFontLocation);
    }

    @Override
    public int drawInBatch(Component pText, float pX, float pY, int pColor, boolean pDropShadow, Matrix4f pMatrix, MultiBufferSource pBuffer, Font.DisplayMode pDisplayMode, int pBackgroundColor, int pPackedLightCoords) {
        return this.wrapped.drawInBatch(pText, pX, pY, pColor, false, pMatrix, pBuffer, pDisplayMode, pBackgroundColor, pPackedLightCoords);
    }

    @Override
    public int drawInBatch(FormattedCharSequence pText, float pX, float pY, int pColor, boolean pDropShadow, Matrix4f pMatrix, MultiBufferSource pBuffer, Font.DisplayMode pDisplayMode, int pBackgroundColor, int pPackedLightCoords) {
        return this.wrapped.drawInBatch(pText, pX, pY, pColor, false, pMatrix, pBuffer, pDisplayMode, pBackgroundColor, pPackedLightCoords);
    }

    @Override
    public int drawInBatch(String pText, float pX, float pY, int pColor, boolean pDropShadow, Matrix4f pMatrix, MultiBufferSource pBuffer, Font.DisplayMode pDisplayMode, int pBackgroundColor, int pPackedLightCoords) {
        return this.wrapped.drawInBatch(pText, pX, pY, pColor, false, pMatrix, pBuffer, pDisplayMode, pBackgroundColor, pPackedLightCoords);
    }

    @Override
    public int drawInBatch(String pText, float pX, float pY, int pColor, boolean pDropShadow, Matrix4f pMatrix, MultiBufferSource pBuffer, Font.DisplayMode pDisplayMode, int pBackgroundColor, int pPackedLightCoords, boolean pBidirectional) {
        return this.wrapped.drawInBatch(pText, pX, pY, pColor, false, pMatrix, pBuffer, pDisplayMode, pBackgroundColor, pPackedLightCoords, pBidirectional);
    }

    public FormattedText ellipsize(FormattedText text, int maxWidth) {
        return this.wrapped.ellipsize(text, maxWidth);
    }

    @Override
    public int wordWrapHeight(FormattedText pText, int pMaxWidth) {
        return this.wrapped.wordWrapHeight(pText, pMaxWidth);
    }

    @Override
    public String bidirectionalShaping(String pText) {
        return this.wrapped.bidirectionalShaping(pText);
    }

    @Override
    public void drawInBatch8xOutline(FormattedCharSequence pText, float pX, float pY, int pColor, int pBackgroundColor, Matrix4f pMatrix, MultiBufferSource pBuffer, int pPackedLightCoords) {
        this.wrapped.drawInBatch8xOutline(pText, pX, pY, pColor, pBackgroundColor, pMatrix, pBuffer, pPackedLightCoords);
    }

    @Override
    public int width(String pText) {
        return this.wrapped.width(pText);
    }

    @Override
    public int width(FormattedText pText) {
        return this.wrapped.width(pText);
    }

    @Override
    public int width(FormattedCharSequence pText) {
        return this.wrapped.width(pText);
    }

    @Override
    public String plainSubstrByWidth(String string0, int int1, boolean boolean2) {
        return this.wrapped.plainSubstrByWidth(string0, int1, boolean2);
    }

    @Override
    public String plainSubstrByWidth(String pText, int pMaxWidth) {
        return this.wrapped.plainSubstrByWidth(pText, pMaxWidth);
    }

    @Override
    public FormattedText substrByWidth(FormattedText pText, int pMaxWidth) {
        return this.wrapped.substrByWidth(pText, pMaxWidth);
    }

    @Override
    public int wordWrapHeight(String pStr, int pMaxWidth) {
        return this.wrapped.wordWrapHeight(pStr, pMaxWidth);
    }

    @Override
    public List<FormattedCharSequence> split(FormattedText pText, int pMaxWidth) {
        return this.wrapped.split(pText, pMaxWidth);
    }

    @Override
    public boolean isBidirectional() {
        return this.wrapped.isBidirectional();
    }

    @Override
    public StringSplitter getSplitter() {
        return this.wrapped.getSplitter();
    }
}