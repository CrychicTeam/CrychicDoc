package icyllis.modernui.graphics.text;

import com.ibm.icu.impl.UCharacterProperty;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.lang.UScript;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.Unmodifiable;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public class FontCollection {

    public static final int GC_M_MASK = UCharacterProperty.getMask(8) | UCharacterProperty.getMask(7) | UCharacterProperty.getMask(6);

    public static final int REPLACEMENT_CHARACTER = 65533;

    @NonNull
    private final List<FontFamily> mFamilies;

    private final BitSet mExclusiveEastAsianBits;

    public static final int UNSUPPORTED_FONT_SCORE = 0;

    public static boolean doesNotNeedFontSupport(int c) {
        return c == 173 || c == 847 || c == 1564 || 8204 <= c && c <= 8207 || 8234 <= c && c <= 8238 || 8294 <= c && c <= 8297 || c == 65279 || isVariationSelector(c);
    }

    public static boolean isStickyWhitelisted(int c) {
        return switch(c) {
            case 33, 44, 45, 46, 58, 59, 63, 160, 8208, 8209, 8239, 9792, 9794, 9877 ->
                true;
            default ->
                false;
        };
    }

    public static boolean isCombining(int c) {
        return (UCharacterProperty.getMask(UCharacter.getType(c)) & GC_M_MASK) != 0;
    }

    public static boolean isVariationSelector(int c) {
        return UCharacter.hasBinaryProperty(c, 36);
    }

    public static boolean isEmojiBreak(int prevCh, int ch) {
        return !Emoji.isEmojiModifier(ch) && (!Emoji.isRegionalIndicatorSymbol(prevCh) || !Emoji.isRegionalIndicatorSymbol(ch)) && ch != 8419 && !Emoji.isTagSpecChar(ch) && ch != 8205 && prevCh != 8205;
    }

    public FontCollection(@NonNull FontFamily... families) {
        this(families, null);
    }

    @Internal
    public FontCollection(@NonNull FontFamily[] families, @Nullable BitSet exclusiveEastAsianBits) {
        if (families.length == 0) {
            throw new IllegalArgumentException("families cannot be empty");
        } else {
            this.mFamilies = List.of(families);
            this.mExclusiveEastAsianBits = exclusiveEastAsianBits;
        }
    }

    @NonNull
    public List<FontCollection.Run> itemize(@NonNull char[] text, int offset, int limit) {
        return this.itemize(text, offset, limit, limit - offset);
    }

    @NonNull
    public List<FontCollection.Run> itemize(@NonNull char[] text, int offset, int limit, int runLimit) {
        if (offset < 0 || offset > limit || limit > text.length || runLimit < 0) {
            throw new IllegalArgumentException();
        } else if (offset == limit) {
            return Collections.emptyList();
        } else {
            List<FontCollection.Run> result = new ArrayList();
            FontCollection.Run lastRun = null;
            List<FontFamily> lastFamilies = null;
            int prevCh = 0;
            int next = offset;
            int index = offset;
            char _c1 = text[offset];
            int nextCh;
            if (Character.isHighSurrogate(_c1) && offset + 1 < limit) {
                char _c2 = text[offset + 1];
                if (Character.isLowSurrogate(_c2)) {
                    nextCh = Character.toCodePoint(_c1, _c2);
                    index = offset + 1;
                } else {
                    nextCh = 65533;
                }
            } else if (Character.isSurrogate(_c1)) {
                nextCh = 65533;
            } else {
                nextCh = _c1;
            }
            index++;
            boolean running = true;
            do {
                int ch = nextCh;
                int pos = next;
                next = index;
                if (index >= limit) {
                    running = false;
                } else {
                    _c1 = text[index];
                    if (Character.isHighSurrogate(_c1) && index + 1 < limit) {
                        char _c2 = text[index + 1];
                        if (Character.isLowSurrogate(_c2)) {
                            nextCh = Character.toCodePoint(_c1, _c2);
                            index++;
                        } else {
                            nextCh = 65533;
                        }
                    } else if (Character.isSurrogate(_c1)) {
                        nextCh = 65533;
                    } else {
                        nextCh = _c1;
                    }
                    index++;
                }
                boolean shouldContinueRun = false;
                if (doesNotNeedFontSupport(ch)) {
                    shouldContinueRun = true;
                } else if (lastFamilies != null && !lastFamilies.isEmpty() && (isStickyWhitelisted(ch) || isCombining(ch))) {
                    if (((FontFamily) lastFamilies.get(0)).isColorEmojiFamily()) {
                        for (FontFamily family : lastFamilies) {
                            shouldContinueRun |= family.hasGlyph(ch);
                        }
                    } else {
                        shouldContinueRun = ((FontFamily) lastFamilies.get(0)).hasGlyph(ch);
                    }
                }
                if (!shouldContinueRun) {
                    List<FontFamily> families = this.getFamilyForChar(ch, running && isVariationSelector(nextCh) ? nextCh : 0);
                    boolean breakRun;
                    if (pos == 0 || lastFamilies == null || lastFamilies.isEmpty()) {
                        breakRun = true;
                    } else if (((FontFamily) lastFamilies.get(0)).isColorEmojiFamily()) {
                        List<FontFamily> intersection = new ArrayList(families);
                        intersection.retainAll(lastFamilies);
                        if (intersection.isEmpty()) {
                            breakRun = true;
                        } else {
                            breakRun = isEmojiBreak(prevCh, ch);
                            if (!breakRun) {
                                families = intersection;
                                lastFamilies = intersection;
                                lastRun.families = intersection;
                            }
                        }
                    } else {
                        breakRun = families.get(0) != lastFamilies.get(0);
                    }
                    if (breakRun) {
                        int start = pos;
                        if (pos != 0 && (isCombining(ch) || Emoji.isEmojiModifier(ch) && Emoji.isEmojiModifierBase(prevCh))) {
                            for (FontFamily family : families) {
                                if (family.hasGlyph(prevCh)) {
                                    int prevLength = Character.charCount(prevCh);
                                    if (lastRun != null) {
                                        lastRun.limit -= prevLength;
                                        if (lastRun.start == lastRun.limit) {
                                            result.remove(lastRun);
                                        }
                                    }
                                    start = pos - prevLength;
                                    break;
                                }
                            }
                        }
                        if (lastFamilies == null || lastFamilies.isEmpty()) {
                            start = offset;
                        }
                        FontCollection.Run run = new FontCollection.Run(families, start, 0);
                        result.add(run);
                        lastRun = run;
                        lastFamilies = families;
                    }
                }
                prevCh = ch;
                if (lastRun != null) {
                    lastRun.limit = next;
                }
            } while ((result.size() < 2 || runLimit != result.size() - 2) && running);
            if (lastFamilies == null || lastFamilies.isEmpty()) {
                result.add(new FontCollection.Run(List.of((FontFamily) this.mFamilies.get(0)), offset, limit));
            }
            return result;
        }
    }

    @NonNull
    @Unmodifiable
    public List<FontFamily> getFamilies() {
        return this.mFamilies;
    }

    private int calcCoverageScore(int ch, int vs, @NonNull FontFamily family, boolean isExclusiveEastAsianFamily) {
        if (isExclusiveEastAsianFamily) {
            int script = UScript.getScript(ch);
            if (script > 1) {
                switch(script) {
                    case 5:
                    case 17:
                    case 18:
                    case 20:
                    case 22:
                    case 41:
                    case 92:
                    case 131:
                    case 150:
                    case 154:
                    case 191:
                        break;
                    default:
                        return 0;
                }
            }
        }
        if (!family.hasGlyph(ch, vs)) {
            return 0;
        } else {
            boolean colorEmojiRequest;
            switch(vs) {
                case 65038:
                    colorEmojiRequest = false;
                    break;
                case 65039:
                    colorEmojiRequest = true;
                    break;
                default:
                    return 1;
            }
            return colorEmojiRequest == family.isColorEmojiFamily() ? 2 : 1;
        }
    }

    @NonNull
    private List<FontFamily> getFamilyForChar(int ch, int vs) {
        List<FontFamily> families = null;
        int bestScore = 0;
        int i = 0;
        for (int e = this.mFamilies.size(); i < e; i++) {
            FontFamily family = (FontFamily) this.mFamilies.get(i);
            int score = this.calcCoverageScore(ch, vs, family, this.mExclusiveEastAsianBits != null && this.mExclusiveEastAsianBits.get(i));
            if (score != 0 && score >= bestScore) {
                if (families == null) {
                    families = new ArrayList(2);
                }
                if (score > bestScore) {
                    families.clear();
                    bestScore = score;
                }
                if (families.size() < 2) {
                    families.add(family);
                }
            }
        }
        if (families != null && !((FontFamily) families.get(0)).isColorEmojiFamily()) {
            return families;
        } else {
            for (FontFamily family : FontFamily.getSystemFontMap().values()) {
                int score = this.calcCoverageScore(ch, vs, family, false);
                if (score != 0 && score >= bestScore) {
                    if (families == null) {
                        families = new ArrayList(8);
                    }
                    if (score > bestScore) {
                        families.clear();
                        bestScore = score;
                    }
                    if (families.size() < 8) {
                        families.add(family);
                    }
                }
            }
            return families != null ? families : List.of((FontFamily) this.mFamilies.get(0));
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            FontCollection that = (FontCollection) o;
            return this.mFamilies.equals(that.mFamilies);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.mFamilies.hashCode();
    }

    public String toString() {
        StringBuilder s = new StringBuilder("FontCollection");
        s.append('{').append("mFamilies").append('=').append('[');
        int i = 0;
        for (int e = this.mFamilies.size(); i < e; i++) {
            if (i > 0) {
                s.append(',').append(' ');
            }
            s.append(((FontFamily) this.mFamilies.get(i)).getFamilyName());
        }
        return s.append(']').append(',').append(' ').append("mExclusiveEastAsianBits").append('=').append(this.mExclusiveEastAsianBits).append('}').toString();
    }

    public static final class Run {

        List<FontFamily> families;

        int start;

        int limit;

        Run(List<FontFamily> families, int start, int limit) {
            this.families = families;
            this.start = start;
            this.limit = limit;
        }

        public Font getBestFont(char[] text, int style) {
            int bestIndex = 0;
            int bestScore = 0;
            if (((FontFamily) this.families.get(0)).isColorEmojiFamily() && this.families.size() > 1) {
                for (int i = 0; i < this.families.size(); i++) {
                    Font font = ((FontFamily) this.families.get(i)).getClosestMatch(0);
                    int score = font.calcGlyphScore(text, this.start, this.limit);
                    if (score > bestScore) {
                        bestIndex = i;
                        bestScore = score;
                    }
                }
            }
            return ((FontFamily) this.families.get(bestIndex)).getClosestMatch(style);
        }

        public int start() {
            return this.start;
        }

        public int limit() {
            return this.limit;
        }
    }
}