package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;
import com.mojang.blaze3d.font.GlyphInfo;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.List;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.client.StringSplitter;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.client.gui.font.glyphs.EmptyGlyph;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.FormattedCharSink;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringDecomposer;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Font {

    private static final float EFFECT_DEPTH = 0.01F;

    private static final Vector3f SHADOW_OFFSET = new Vector3f(0.0F, 0.0F, 0.03F);

    public static final int ALPHA_CUTOFF = 8;

    public final int lineHeight = 9;

    public final RandomSource random = RandomSource.create();

    private final Function<ResourceLocation, FontSet> fonts;

    final boolean filterFishyGlyphs;

    private final StringSplitter splitter;

    public Font(Function<ResourceLocation, FontSet> functionResourceLocationFontSet0, boolean boolean1) {
        this.fonts = functionResourceLocationFontSet0;
        this.filterFishyGlyphs = boolean1;
        this.splitter = new StringSplitter((p_92722_, p_92723_) -> this.getFontSet(p_92723_.getFont()).getGlyphInfo(p_92722_, this.filterFishyGlyphs).getAdvance(p_92723_.isBold()));
    }

    FontSet getFontSet(ResourceLocation resourceLocation0) {
        return (FontSet) this.fonts.apply(resourceLocation0);
    }

    public String bidirectionalShaping(String string0) {
        try {
            Bidi $$1 = new Bidi(new ArabicShaping(8).shape(string0), 127);
            $$1.setReorderingMode(0);
            return $$1.writeReordered(2);
        } catch (ArabicShapingException var3) {
            return string0;
        }
    }

    public int drawInBatch(String string0, float float1, float float2, int int3, boolean boolean4, Matrix4f matrixF5, MultiBufferSource multiBufferSource6, Font.DisplayMode fontDisplayMode7, int int8, int int9) {
        return this.drawInBatch(string0, float1, float2, int3, boolean4, matrixF5, multiBufferSource6, fontDisplayMode7, int8, int9, this.isBidirectional());
    }

    public int drawInBatch(String string0, float float1, float float2, int int3, boolean boolean4, Matrix4f matrixF5, MultiBufferSource multiBufferSource6, Font.DisplayMode fontDisplayMode7, int int8, int int9, boolean boolean10) {
        return this.drawInternal(string0, float1, float2, int3, boolean4, matrixF5, multiBufferSource6, fontDisplayMode7, int8, int9, boolean10);
    }

    public int drawInBatch(Component component0, float float1, float float2, int int3, boolean boolean4, Matrix4f matrixF5, MultiBufferSource multiBufferSource6, Font.DisplayMode fontDisplayMode7, int int8, int int9) {
        return this.drawInBatch(component0.getVisualOrderText(), float1, float2, int3, boolean4, matrixF5, multiBufferSource6, fontDisplayMode7, int8, int9);
    }

    public int drawInBatch(FormattedCharSequence formattedCharSequence0, float float1, float float2, int int3, boolean boolean4, Matrix4f matrixF5, MultiBufferSource multiBufferSource6, Font.DisplayMode fontDisplayMode7, int int8, int int9) {
        return this.drawInternal(formattedCharSequence0, float1, float2, int3, boolean4, matrixF5, multiBufferSource6, fontDisplayMode7, int8, int9);
    }

    public void drawInBatch8xOutline(FormattedCharSequence formattedCharSequence0, float float1, float float2, int int3, int int4, Matrix4f matrixF5, MultiBufferSource multiBufferSource6, int int7) {
        int $$8 = adjustColor(int4);
        Font.StringRenderOutput $$9 = new Font.StringRenderOutput(multiBufferSource6, 0.0F, 0.0F, $$8, false, matrixF5, Font.DisplayMode.NORMAL, int7);
        for (int $$10 = -1; $$10 <= 1; $$10++) {
            for (int $$11 = -1; $$11 <= 1; $$11++) {
                if ($$10 != 0 || $$11 != 0) {
                    float[] $$12 = new float[] { float1 };
                    int $$13 = $$10;
                    int $$14 = $$11;
                    formattedCharSequence0.accept((p_168661_, p_168662_, p_168663_) -> {
                        boolean $$9x = p_168662_.isBold();
                        FontSet $$10x = this.getFontSet(p_168662_.getFont());
                        GlyphInfo $$11x = $$10x.getGlyphInfo(p_168663_, this.filterFishyGlyphs);
                        $$9.x = $$12[0] + (float) $$13 * $$11x.getShadowOffset();
                        $$9.y = float2 + (float) $$14 * $$11x.getShadowOffset();
                        $$12[0] += $$11x.getAdvance($$9x);
                        return $$9.accept(p_168661_, p_168662_.withColor($$8), p_168663_);
                    });
                }
            }
        }
        Font.StringRenderOutput $$15 = new Font.StringRenderOutput(multiBufferSource6, float1, float2, adjustColor(int3), false, matrixF5, Font.DisplayMode.POLYGON_OFFSET, int7);
        formattedCharSequence0.accept($$15);
        $$15.finish(0, float1);
    }

    private static int adjustColor(int int0) {
        return (int0 & -67108864) == 0 ? int0 | 0xFF000000 : int0;
    }

    private int drawInternal(String string0, float float1, float float2, int int3, boolean boolean4, Matrix4f matrixF5, MultiBufferSource multiBufferSource6, Font.DisplayMode fontDisplayMode7, int int8, int int9, boolean boolean10) {
        if (boolean10) {
            string0 = this.bidirectionalShaping(string0);
        }
        int3 = adjustColor(int3);
        Matrix4f $$11 = new Matrix4f(matrixF5);
        if (boolean4) {
            this.renderText(string0, float1, float2, int3, true, matrixF5, multiBufferSource6, fontDisplayMode7, int8, int9);
            $$11.translate(SHADOW_OFFSET);
        }
        float1 = this.renderText(string0, float1, float2, int3, false, $$11, multiBufferSource6, fontDisplayMode7, int8, int9);
        return (int) float1 + (boolean4 ? 1 : 0);
    }

    private int drawInternal(FormattedCharSequence formattedCharSequence0, float float1, float float2, int int3, boolean boolean4, Matrix4f matrixF5, MultiBufferSource multiBufferSource6, Font.DisplayMode fontDisplayMode7, int int8, int int9) {
        int3 = adjustColor(int3);
        Matrix4f $$10 = new Matrix4f(matrixF5);
        if (boolean4) {
            this.renderText(formattedCharSequence0, float1, float2, int3, true, matrixF5, multiBufferSource6, fontDisplayMode7, int8, int9);
            $$10.translate(SHADOW_OFFSET);
        }
        float1 = this.renderText(formattedCharSequence0, float1, float2, int3, false, $$10, multiBufferSource6, fontDisplayMode7, int8, int9);
        return (int) float1 + (boolean4 ? 1 : 0);
    }

    private float renderText(String string0, float float1, float float2, int int3, boolean boolean4, Matrix4f matrixF5, MultiBufferSource multiBufferSource6, Font.DisplayMode fontDisplayMode7, int int8, int int9) {
        Font.StringRenderOutput $$10 = new Font.StringRenderOutput(multiBufferSource6, float1, float2, int3, boolean4, matrixF5, fontDisplayMode7, int9);
        StringDecomposer.iterateFormatted(string0, Style.EMPTY, $$10);
        return $$10.finish(int8, float1);
    }

    private float renderText(FormattedCharSequence formattedCharSequence0, float float1, float float2, int int3, boolean boolean4, Matrix4f matrixF5, MultiBufferSource multiBufferSource6, Font.DisplayMode fontDisplayMode7, int int8, int int9) {
        Font.StringRenderOutput $$10 = new Font.StringRenderOutput(multiBufferSource6, float1, float2, int3, boolean4, matrixF5, fontDisplayMode7, int9);
        formattedCharSequence0.accept($$10);
        return $$10.finish(int8, float1);
    }

    void renderChar(BakedGlyph bakedGlyph0, boolean boolean1, boolean boolean2, float float3, float float4, float float5, Matrix4f matrixF6, VertexConsumer vertexConsumer7, float float8, float float9, float float10, float float11, int int12) {
        bakedGlyph0.render(boolean2, float4, float5, matrixF6, vertexConsumer7, float8, float9, float10, float11, int12);
        if (boolean1) {
            bakedGlyph0.render(boolean2, float4 + float3, float5, matrixF6, vertexConsumer7, float8, float9, float10, float11, int12);
        }
    }

    public int width(String string0) {
        return Mth.ceil(this.splitter.stringWidth(string0));
    }

    public int width(FormattedText formattedText0) {
        return Mth.ceil(this.splitter.stringWidth(formattedText0));
    }

    public int width(FormattedCharSequence formattedCharSequence0) {
        return Mth.ceil(this.splitter.stringWidth(formattedCharSequence0));
    }

    public String plainSubstrByWidth(String string0, int int1, boolean boolean2) {
        return boolean2 ? this.splitter.plainTailByWidth(string0, int1, Style.EMPTY) : this.splitter.plainHeadByWidth(string0, int1, Style.EMPTY);
    }

    public String plainSubstrByWidth(String string0, int int1) {
        return this.splitter.plainHeadByWidth(string0, int1, Style.EMPTY);
    }

    public FormattedText substrByWidth(FormattedText formattedText0, int int1) {
        return this.splitter.headByWidth(formattedText0, int1, Style.EMPTY);
    }

    public int wordWrapHeight(String string0, int int1) {
        return 9 * this.splitter.splitLines(string0, int1, Style.EMPTY).size();
    }

    public int wordWrapHeight(FormattedText formattedText0, int int1) {
        return 9 * this.splitter.splitLines(formattedText0, int1, Style.EMPTY).size();
    }

    public List<FormattedCharSequence> split(FormattedText formattedText0, int int1) {
        return Language.getInstance().getVisualOrder(this.splitter.splitLines(formattedText0, int1, Style.EMPTY));
    }

    public boolean isBidirectional() {
        return Language.getInstance().isDefaultRightToLeft();
    }

    public StringSplitter getSplitter() {
        return this.splitter;
    }

    public static enum DisplayMode {

        NORMAL, SEE_THROUGH, POLYGON_OFFSET
    }

    class StringRenderOutput implements FormattedCharSink {

        final MultiBufferSource bufferSource;

        private final boolean dropShadow;

        private final float dimFactor;

        private final float r;

        private final float g;

        private final float b;

        private final float a;

        private final Matrix4f pose;

        private final Font.DisplayMode mode;

        private final int packedLightCoords;

        float x;

        float y;

        @Nullable
        private List<BakedGlyph.Effect> effects;

        private void addEffect(BakedGlyph.Effect bakedGlyphEffect0) {
            if (this.effects == null) {
                this.effects = Lists.newArrayList();
            }
            this.effects.add(bakedGlyphEffect0);
        }

        public StringRenderOutput(MultiBufferSource multiBufferSource0, float float1, float float2, int int3, boolean boolean4, Matrix4f matrixF5, Font.DisplayMode fontDisplayMode6, int int7) {
            this.bufferSource = multiBufferSource0;
            this.x = float1;
            this.y = float2;
            this.dropShadow = boolean4;
            this.dimFactor = boolean4 ? 0.25F : 1.0F;
            this.r = (float) (int3 >> 16 & 0xFF) / 255.0F * this.dimFactor;
            this.g = (float) (int3 >> 8 & 0xFF) / 255.0F * this.dimFactor;
            this.b = (float) (int3 & 0xFF) / 255.0F * this.dimFactor;
            this.a = (float) (int3 >> 24 & 0xFF) / 255.0F;
            this.pose = matrixF5;
            this.mode = fontDisplayMode6;
            this.packedLightCoords = int7;
        }

        @Override
        public boolean accept(int int0, Style style1, int int2) {
            FontSet $$3 = Font.this.getFontSet(style1.getFont());
            GlyphInfo $$4 = $$3.getGlyphInfo(int2, Font.this.filterFishyGlyphs);
            BakedGlyph $$5 = style1.isObfuscated() && int2 != 32 ? $$3.getRandomGlyph($$4) : $$3.getGlyph(int2);
            boolean $$6 = style1.isBold();
            float $$7 = this.a;
            TextColor $$8 = style1.getColor();
            float $$10;
            float $$11;
            float $$12;
            if ($$8 != null) {
                int $$9 = $$8.getValue();
                $$10 = (float) ($$9 >> 16 & 0xFF) / 255.0F * this.dimFactor;
                $$11 = (float) ($$9 >> 8 & 0xFF) / 255.0F * this.dimFactor;
                $$12 = (float) ($$9 & 0xFF) / 255.0F * this.dimFactor;
            } else {
                $$10 = this.r;
                $$11 = this.g;
                $$12 = this.b;
            }
            if (!($$5 instanceof EmptyGlyph)) {
                float $$16 = $$6 ? $$4.getBoldOffset() : 0.0F;
                float $$17 = this.dropShadow ? $$4.getShadowOffset() : 0.0F;
                VertexConsumer $$18 = this.bufferSource.getBuffer($$5.renderType(this.mode));
                Font.this.renderChar($$5, $$6, style1.isItalic(), $$16, this.x + $$17, this.y + $$17, this.pose, $$18, $$10, $$11, $$12, $$7, this.packedLightCoords);
            }
            float $$19 = $$4.getAdvance($$6);
            float $$20 = this.dropShadow ? 1.0F : 0.0F;
            if (style1.isStrikethrough()) {
                this.addEffect(new BakedGlyph.Effect(this.x + $$20 - 1.0F, this.y + $$20 + 4.5F, this.x + $$20 + $$19, this.y + $$20 + 4.5F - 1.0F, 0.01F, $$10, $$11, $$12, $$7));
            }
            if (style1.isUnderlined()) {
                this.addEffect(new BakedGlyph.Effect(this.x + $$20 - 1.0F, this.y + $$20 + 9.0F, this.x + $$20 + $$19, this.y + $$20 + 9.0F - 1.0F, 0.01F, $$10, $$11, $$12, $$7));
            }
            this.x += $$19;
            return true;
        }

        public float finish(int int0, float float1) {
            if (int0 != 0) {
                float $$2 = (float) (int0 >> 24 & 0xFF) / 255.0F;
                float $$3 = (float) (int0 >> 16 & 0xFF) / 255.0F;
                float $$4 = (float) (int0 >> 8 & 0xFF) / 255.0F;
                float $$5 = (float) (int0 & 0xFF) / 255.0F;
                this.addEffect(new BakedGlyph.Effect(float1 - 1.0F, this.y + 9.0F, this.x + 1.0F, this.y - 1.0F, 0.01F, $$3, $$4, $$5, $$2));
            }
            if (this.effects != null) {
                BakedGlyph $$6 = Font.this.getFontSet(Style.DEFAULT_FONT).whiteGlyph();
                VertexConsumer $$7 = this.bufferSource.getBuffer($$6.renderType(this.mode));
                for (BakedGlyph.Effect $$8 : this.effects) {
                    $$6.renderEffect($$8, this.pose, $$7, this.packedLightCoords);
                }
            }
            return this.x;
        }
    }
}