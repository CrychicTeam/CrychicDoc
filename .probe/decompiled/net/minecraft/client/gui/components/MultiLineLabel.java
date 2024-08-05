package net.minecraft.client.gui.components;

import com.google.common.collect.ImmutableList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;

public interface MultiLineLabel {

    MultiLineLabel EMPTY = new MultiLineLabel() {

        @Override
        public int renderCentered(GuiGraphics p_283287_, int p_94383_, int p_94384_) {
            return p_94384_;
        }

        @Override
        public int renderCentered(GuiGraphics p_283384_, int p_94395_, int p_94396_, int p_94397_, int p_94398_) {
            return p_94396_;
        }

        @Override
        public int renderLeftAligned(GuiGraphics p_283077_, int p_94379_, int p_94380_, int p_282157_, int p_282742_) {
            return p_94380_;
        }

        @Override
        public int renderLeftAlignedNoShadow(GuiGraphics p_283645_, int p_94389_, int p_94390_, int p_94391_, int p_94392_) {
            return p_94390_;
        }

        @Override
        public void renderBackgroundCentered(GuiGraphics p_283208_, int p_210825_, int p_210826_, int p_210827_, int p_210828_, int p_210829_) {
        }

        @Override
        public int getLineCount() {
            return 0;
        }

        @Override
        public int getWidth() {
            return 0;
        }
    };

    static MultiLineLabel create(Font font0, FormattedText formattedText1, int int2) {
        return createFixed(font0, (List<MultiLineLabel.TextWithWidth>) font0.split(formattedText1, int2).stream().map(p_94374_ -> new MultiLineLabel.TextWithWidth(p_94374_, font0.width(p_94374_))).collect(ImmutableList.toImmutableList()));
    }

    static MultiLineLabel create(Font font0, FormattedText formattedText1, int int2, int int3) {
        return createFixed(font0, (List<MultiLineLabel.TextWithWidth>) font0.split(formattedText1, int2).stream().limit((long) int3).map(p_94371_ -> new MultiLineLabel.TextWithWidth(p_94371_, font0.width(p_94371_))).collect(ImmutableList.toImmutableList()));
    }

    static MultiLineLabel create(Font font0, Component... component1) {
        return createFixed(font0, (List<MultiLineLabel.TextWithWidth>) Arrays.stream(component1).map(Component::m_7532_).map(p_94360_ -> new MultiLineLabel.TextWithWidth(p_94360_, font0.width(p_94360_))).collect(ImmutableList.toImmutableList()));
    }

    static MultiLineLabel create(Font font0, List<Component> listComponent1) {
        return createFixed(font0, (List<MultiLineLabel.TextWithWidth>) listComponent1.stream().map(Component::m_7532_).map(p_169035_ -> new MultiLineLabel.TextWithWidth(p_169035_, font0.width(p_169035_))).collect(ImmutableList.toImmutableList()));
    }

    static MultiLineLabel createFixed(final Font font0, final List<MultiLineLabel.TextWithWidth> listMultiLineLabelTextWithWidth1) {
        return listMultiLineLabelTextWithWidth1.isEmpty() ? EMPTY : new MultiLineLabel() {

            private final int width = listMultiLineLabelTextWithWidth1.stream().mapToInt(p_232527_ -> p_232527_.width).max().orElse(0);

            @Override
            public int renderCentered(GuiGraphics p_283492_, int p_283184_, int p_282078_) {
                return this.renderCentered(p_283492_, p_283184_, p_282078_, 9, 16777215);
            }

            @Override
            public int renderCentered(GuiGraphics p_281603_, int p_281267_, int p_281819_, int p_281545_, int p_282780_) {
                int $$5 = p_281819_;
                for (MultiLineLabel.TextWithWidth $$6 : listMultiLineLabelTextWithWidth1) {
                    p_281603_.drawString(font0, $$6.text, p_281267_ - $$6.width / 2, $$5, p_282780_);
                    $$5 += p_281545_;
                }
                return $$5;
            }

            @Override
            public int renderLeftAligned(GuiGraphics p_282318_, int p_283665_, int p_283416_, int p_281919_, int p_281686_) {
                int $$5 = p_283416_;
                for (MultiLineLabel.TextWithWidth $$6 : listMultiLineLabelTextWithWidth1) {
                    p_282318_.drawString(font0, $$6.text, p_283665_, $$5, p_281686_);
                    $$5 += p_281919_;
                }
                return $$5;
            }

            @Override
            public int renderLeftAlignedNoShadow(GuiGraphics p_281782_, int p_282841_, int p_283554_, int p_282768_, int p_283499_) {
                int $$5 = p_283554_;
                for (MultiLineLabel.TextWithWidth $$6 : listMultiLineLabelTextWithWidth1) {
                    p_281782_.drawString(font0, $$6.text, p_282841_, $$5, p_283499_, false);
                    $$5 += p_282768_;
                }
                return $$5;
            }

            @Override
            public void renderBackgroundCentered(GuiGraphics p_281633_, int p_210832_, int p_210833_, int p_210834_, int p_210835_, int p_210836_) {
                int $$6 = listMultiLineLabelTextWithWidth1.stream().mapToInt(p_232524_ -> p_232524_.width).max().orElse(0);
                if ($$6 > 0) {
                    p_281633_.fill(p_210832_ - $$6 / 2 - p_210835_, p_210833_ - p_210835_, p_210832_ + $$6 / 2 + p_210835_, p_210833_ + listMultiLineLabelTextWithWidth1.size() * p_210834_ + p_210835_, p_210836_);
                }
            }

            @Override
            public int getLineCount() {
                return listMultiLineLabelTextWithWidth1.size();
            }

            @Override
            public int getWidth() {
                return this.width;
            }
        };
    }

    int renderCentered(GuiGraphics var1, int var2, int var3);

    int renderCentered(GuiGraphics var1, int var2, int var3, int var4, int var5);

    int renderLeftAligned(GuiGraphics var1, int var2, int var3, int var4, int var5);

    int renderLeftAlignedNoShadow(GuiGraphics var1, int var2, int var3, int var4, int var5);

    void renderBackgroundCentered(GuiGraphics var1, int var2, int var3, int var4, int var5, int var6);

    int getLineCount();

    int getWidth();

    public static class TextWithWidth {

        final FormattedCharSequence text;

        final int width;

        TextWithWidth(FormattedCharSequence formattedCharSequence0, int int1) {
            this.text = formattedCharSequence0;
            this.width = int1;
        }
    }
}