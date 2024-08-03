package icyllis.modernui.text;

import icyllis.modernui.ModernUI;
import java.nio.CharBuffer;
import java.util.Locale;

public final class TextDirectionHeuristics {

    public static final TextDirectionHeuristic LTR = new TextDirectionHeuristics.TextDirectionHeuristicInternal(null, false);

    public static final TextDirectionHeuristic RTL = new TextDirectionHeuristics.TextDirectionHeuristicInternal(null, true);

    public static final TextDirectionHeuristic FIRSTSTRONG_LTR = new TextDirectionHeuristics.TextDirectionHeuristicInternal(TextDirectionHeuristics.FirstStrong.INSTANCE, false);

    public static final TextDirectionHeuristic FIRSTSTRONG_RTL = new TextDirectionHeuristics.TextDirectionHeuristicInternal(TextDirectionHeuristics.FirstStrong.INSTANCE, true);

    public static final TextDirectionHeuristic ANYRTL_LTR = new TextDirectionHeuristics.TextDirectionHeuristicInternal(TextDirectionHeuristics.AnyStrong.INSTANCE_RTL, false);

    public static final TextDirectionHeuristic LOCALE = new TextDirectionHeuristics.TextDirectionHeuristicLocale();

    private static final int STATE_TRUE = 0;

    private static final int STATE_FALSE = 1;

    private static final int STATE_UNKNOWN = 2;

    private TextDirectionHeuristics() {
    }

    private static int isRtlCodePoint(int codePoint) {
        switch(Character.getDirectionality(codePoint)) {
            case -1:
                if ((1424 > codePoint || codePoint > 2303) && (64285 > codePoint || codePoint > 64975) && (65008 > codePoint || codePoint > 65023) && (65136 > codePoint || codePoint > 65279) && (67584 > codePoint || codePoint > 69631) && (124928 > codePoint || codePoint > 126975)) {
                    return (8293 > codePoint || codePoint > 8297) && (65520 > codePoint || codePoint > 65528) && (917504 > codePoint || codePoint > 921599) && (64976 > codePoint || codePoint > 65007) && (codePoint & 65534) != 65534 && (8352 > codePoint || codePoint > 8399) && (55296 > codePoint || codePoint > 57343) ? 1 : 2;
                } else {
                    return 0;
                }
            case 0:
                return 1;
            case 1:
            case 2:
                return 0;
            default:
                return 2;
        }
    }

    private static class AnyStrong implements TextDirectionHeuristics.TextDirectionAlgorithm {

        public static final TextDirectionHeuristics.AnyStrong INSTANCE_RTL = new TextDirectionHeuristics.AnyStrong(true);

        public static final TextDirectionHeuristics.AnyStrong INSTANCE_LTR = new TextDirectionHeuristics.AnyStrong(false);

        private final boolean mLookForRtl;

        private AnyStrong(boolean lookForRtl) {
            this.mLookForRtl = lookForRtl;
        }

        @Override
        public int checkRtl(CharSequence cs, int start, int count) {
            boolean haveUnlookedFor = false;
            int openIsolateCount = 0;
            int i = start;
            int end = start + count;
            while (i < end) {
                int cp = Character.codePointAt(cs, i);
                if (8294 <= cp && cp <= 8296) {
                    openIsolateCount++;
                } else if (cp == 8297) {
                    if (openIsolateCount > 0) {
                        openIsolateCount--;
                    }
                } else if (openIsolateCount == 0) {
                    switch(TextDirectionHeuristics.isRtlCodePoint(cp)) {
                        case 0:
                            if (this.mLookForRtl) {
                                return 0;
                            }
                            haveUnlookedFor = true;
                            break;
                        case 1:
                            if (!this.mLookForRtl) {
                                return 1;
                            }
                            haveUnlookedFor = true;
                    }
                }
                i += Character.charCount(cp);
            }
            if (haveUnlookedFor) {
                return this.mLookForRtl ? 1 : 0;
            } else {
                return 2;
            }
        }
    }

    private static class FirstStrong implements TextDirectionHeuristics.TextDirectionAlgorithm {

        public static final TextDirectionHeuristics.FirstStrong INSTANCE = new TextDirectionHeuristics.FirstStrong();

        @Override
        public int checkRtl(CharSequence cs, int start, int count) {
            int result = 2;
            int openIsolateCount = 0;
            int i = start;
            int end = start + count;
            while (i < end && result == 2) {
                int cp = Character.codePointAt(cs, i);
                if (8294 <= cp && cp <= 8296) {
                    openIsolateCount++;
                } else if (cp == 8297) {
                    if (openIsolateCount > 0) {
                        openIsolateCount--;
                    }
                } else if (openIsolateCount == 0) {
                    result = TextDirectionHeuristics.isRtlCodePoint(cp);
                }
                i += Character.charCount(cp);
            }
            return result;
        }
    }

    private interface TextDirectionAlgorithm {

        int checkRtl(CharSequence var1, int var2, int var3);
    }

    private abstract static class TextDirectionHeuristicImpl implements TextDirectionHeuristic {

        private final TextDirectionHeuristics.TextDirectionAlgorithm mAlgorithm;

        public TextDirectionHeuristicImpl(TextDirectionHeuristics.TextDirectionAlgorithm algorithm) {
            this.mAlgorithm = algorithm;
        }

        protected abstract boolean isDefaultRtl();

        @Override
        public boolean isRtl(char[] array, int start, int count) {
            return this.isRtl(CharBuffer.wrap(array), start, count);
        }

        @Override
        public boolean isRtl(CharSequence cs, int start, int count) {
            if (cs == null || start < 0 || count < 0 || cs.length() - count < start) {
                throw new IllegalArgumentException();
            } else {
                return this.mAlgorithm == null ? this.isDefaultRtl() : this.doCheck(cs, start, count);
            }
        }

        private boolean doCheck(CharSequence cs, int start, int count) {
            switch(this.mAlgorithm.checkRtl(cs, start, count)) {
                case 0:
                    return true;
                case 1:
                    return false;
                default:
                    return this.isDefaultRtl();
            }
        }
    }

    private static class TextDirectionHeuristicInternal extends TextDirectionHeuristics.TextDirectionHeuristicImpl {

        private final boolean mDefaultRtl;

        private TextDirectionHeuristicInternal(TextDirectionHeuristics.TextDirectionAlgorithm algorithm, boolean defaultRtl) {
            super(algorithm);
            this.mDefaultRtl = defaultRtl;
        }

        @Override
        protected boolean isDefaultRtl() {
            return this.mDefaultRtl;
        }
    }

    private static class TextDirectionHeuristicLocale extends TextDirectionHeuristics.TextDirectionHeuristicImpl {

        public TextDirectionHeuristicLocale() {
            super(null);
        }

        @Override
        protected boolean isDefaultRtl() {
            Locale locale = ModernUI.getSelectedLocale();
            return TextUtils.getLayoutDirectionFromLocale(locale) == 1;
        }
    }
}