package icyllis.modernui.mc.text;

import icyllis.arc3d.core.MathUtil;
import icyllis.modernui.graphics.text.CharSequenceBuilder;
import icyllis.modernui.util.Pools;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.Arrays;
import java.util.Optional;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.FormattedCharSink;

public class FormattedLayoutKey {

    private String[] mTexts;

    private Object[] mFonts;

    private int[] mCodes;

    int mHash;

    private FormattedLayoutKey() {
    }

    private FormattedLayoutKey(String[] texts, Object[] fonts, int[] codes, int hash) {
        this.mTexts = texts;
        this.mFonts = fonts;
        this.mCodes = codes;
        this.mHash = hash;
    }

    public int hashCode() {
        int h = this.mHash;
        if (h == 0) {
            h = 1;
            int[] codes = this.mCodes;
            int i = 0;
            for (int e = codes.length; i < e; i++) {
                h = 31 * h + this.mTexts[i].hashCode();
                h = 31 * h + this.mFonts[i].hashCode();
                h = 31 * h + codes[i];
            }
            this.mHash = h;
        }
        return h;
    }

    public boolean equals(Object o) {
        if (o.getClass() != FormattedLayoutKey.class) {
            return false;
        } else {
            FormattedLayoutKey key = (FormattedLayoutKey) o;
            return Arrays.equals(this.mCodes, key.mCodes) && Arrays.equals(this.mFonts, key.mFonts) && Arrays.equals(this.mTexts, key.mTexts);
        }
    }

    public String toString() {
        return "FormattedLayoutKey{mTexts=" + Arrays.toString(this.mTexts) + ", mFonts=" + Arrays.toString(this.mFonts) + ", mCodes=" + Arrays.toString(this.mCodes) + ", mHash=" + this.mHash + "}";
    }

    public int getMemorySize() {
        int size = 0;
        for (CharSequence s : this.mTexts) {
            size += MathUtil.align4(s.length()) << 1;
        }
        size += (16 + (MathUtil.align2(this.mTexts.length) << 2)) * 3;
        return size + 32;
    }

    public static class Lookup extends FormattedLayoutKey {

        private final ObjectArrayList<CharSequence> mTexts = new ObjectArrayList();

        private final ObjectArrayList<ResourceLocation> mFonts = new ObjectArrayList();

        private final IntArrayList mCodes = new IntArrayList();

        private final FormattedLayoutKey.Lookup.ContentBuilder mContentBuilder = new FormattedLayoutKey.Lookup.ContentBuilder();

        private final FormattedLayoutKey.Lookup.SequenceBuilder mSequenceBuilder = new FormattedLayoutKey.Lookup.SequenceBuilder();

        private void reset() {
            assert this.mTexts.size() == this.mFonts.size() && this.mTexts.size() == this.mCodes.size();
            this.mTexts.clear();
            this.mFonts.clear();
            this.mCodes.clear();
            this.mHash = 0;
        }

        @Nonnull
        public FormattedLayoutKey update(@Nonnull FormattedText text, @Nonnull Style style) {
            this.reset();
            text.visit(this.mContentBuilder, style);
            return this;
        }

        @Nonnull
        public FormattedLayoutKey update(@Nonnull FormattedCharSequence sequence) {
            this.reset();
            sequence.accept(this.mSequenceBuilder);
            this.mSequenceBuilder.end();
            return this;
        }

        @Override
        public int hashCode() {
            int h = this.mHash;
            if (h == 0) {
                h = 1;
                Object[] texts = this.mTexts.elements();
                Object[] fonts = this.mFonts.elements();
                int[] codes = this.mCodes.elements();
                int i = 0;
                for (int e = this.mCodes.size(); i < e; i++) {
                    h = 31 * h + texts[i].hashCode();
                    h = 31 * h + fonts[i].hashCode();
                    h = 31 * h + codes[i];
                }
                this.mHash = h;
            }
            return h;
        }

        @Override
        public boolean equals(Object o) {
            if (o.getClass() != FormattedLayoutKey.class) {
                return false;
            } else {
                FormattedLayoutKey key = (FormattedLayoutKey) o;
                int length = this.mTexts.size();
                return length == key.mTexts.length && Arrays.equals(this.mCodes.elements(), 0, length, key.mCodes, 0, length) && Arrays.equals(this.mFonts.elements(), 0, length, key.mFonts, 0, length) && Arrays.equals(this.mTexts.elements(), 0, length, key.mTexts, 0, length);
            }
        }

        @Override
        public String toString() {
            return "Lookup{mTexts=" + this.mTexts + ", mFonts=" + this.mFonts + ", mCodes=" + this.mCodes + "}";
        }

        @Nonnull
        public FormattedLayoutKey copy() {
            int length = this.mTexts.size();
            String[] texts = new String[length];
            for (int i = 0; i < length; i++) {
                texts[i] = ((CharSequence) this.mTexts.get(i)).toString();
            }
            return new FormattedLayoutKey(texts, this.mFonts.toArray(), this.mCodes.toIntArray(), this.mHash);
        }

        private class ContentBuilder implements FormattedText.StyledContentConsumer<Object> {

            @Nonnull
            @Override
            public Optional<Object> accept(@Nonnull Style style, @Nonnull String content) {
                Lookup.this.mTexts.add(content);
                Lookup.this.mFonts.add(style.getFont());
                Lookup.this.mCodes.add(CharacterStyle.flatten(style));
                return Optional.empty();
            }
        }

        private class SequenceBuilder implements FormattedCharSink {

            private final Pools.Pool<CharSequenceBuilder> mPool = Pools.newSimplePool(20);

            private CharSequenceBuilder mBuilder = null;

            private Style mStyle = null;

            private void allocate() {
                this.mBuilder = this.mPool.acquire();
                if (this.mBuilder == null) {
                    this.mBuilder = new CharSequenceBuilder();
                } else {
                    this.mBuilder.clear();
                }
            }

            @Override
            public boolean accept(int index, @Nonnull Style style, int codePoint) {
                if (this.mStyle == null) {
                    this.allocate();
                    this.mStyle = style;
                } else if (!CharacterStyle.equalsForTextLayout(this.mStyle, style)) {
                    if (!this.mBuilder.isEmpty()) {
                        Lookup.this.mTexts.add(this.mBuilder);
                        Lookup.this.mFonts.add(this.mStyle.getFont());
                        Lookup.this.mCodes.add(CharacterStyle.flatten(this.mStyle));
                        this.allocate();
                    }
                    this.mStyle = style;
                }
                this.mBuilder.addCodePoint(codePoint);
                return true;
            }

            private void end() {
                if (this.mBuilder != null && !this.mBuilder.isEmpty()) {
                    Lookup.this.mTexts.add(this.mBuilder);
                    Lookup.this.mFonts.add(this.mStyle.getFont());
                    Lookup.this.mCodes.add(CharacterStyle.flatten(this.mStyle));
                }
                ObjectListIterator var1 = Lookup.this.mTexts.iterator();
                while (var1.hasNext()) {
                    CharSequence s = (CharSequence) var1.next();
                    this.mPool.release((CharSequenceBuilder) s);
                }
                this.mBuilder = null;
                this.mStyle = null;
            }
        }
    }
}