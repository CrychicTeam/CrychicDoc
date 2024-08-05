package icyllis.modernui.graphics.text;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.graphics.MathUtil;
import icyllis.modernui.util.Pools;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map.Entry;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class LayoutCache {

    public static final int MAX_PIECE_LENGTH = 128;

    public static final int COMPUTE_CLUSTER_ADVANCES = 1;

    public static final int COMPUTE_GLYPHS_PIXEL_BOUNDS = 2;

    private static final Pools.Pool<LayoutCache.LookupKey> sLookupKeys = Pools.newSynchronizedPool(3);

    private static volatile Cache<LayoutCache.Key, LayoutPiece> sCache;

    @NonNull
    public static LayoutPiece getOrCreate(@NonNull char[] buf, int contextStart, int contextLimit, int start, int limit, boolean isRtl, @NonNull FontPaint paint, int computeFlags) {
        if (contextStart >= 0 && contextStart < contextLimit && buf.length != 0 && contextLimit <= buf.length && start >= contextStart && limit <= contextLimit) {
            if (limit - start > 128) {
                return new LayoutPiece(buf, contextStart, contextLimit, start, limit, isRtl, paint, null, computeFlags);
            } else {
                if (sCache == null) {
                    synchronized (LayoutCache.class) {
                        if (sCache == null) {
                            sCache = Caffeine.newBuilder().maximumSize(2000L).build();
                        }
                    }
                }
                LayoutCache.LookupKey key = sLookupKeys.acquire();
                if (key == null) {
                    key = new LayoutCache.LookupKey();
                }
                LayoutPiece piece = (LayoutPiece) sCache.getIfPresent(key.update(buf, contextStart, contextLimit, start, limit, paint, isRtl));
                if (piece == null) {
                    LayoutCache.Key k = key.copy();
                    sLookupKeys.release(key);
                    piece = new LayoutPiece(buf, contextStart, contextLimit, start, limit, isRtl, paint, null, computeFlags);
                    sCache.put(k, piece);
                } else {
                    int currFlags = piece.mComputeFlags & computeFlags;
                    if (currFlags != computeFlags) {
                        LayoutCache.Key k = key.copy();
                        sLookupKeys.release(key);
                        piece = new LayoutPiece(buf, contextStart, contextLimit, start, limit, isRtl, paint, piece, currFlags ^ computeFlags);
                        sCache.put(k, piece);
                    } else {
                        sLookupKeys.release(key);
                    }
                }
                return piece;
            }
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public static int getSize() {
        return sCache == null ? 0 : (int) Math.min(sCache.estimatedSize(), 2147483647L);
    }

    public static int getMemoryUsage() {
        if (sCache == null) {
            return 0;
        } else {
            int size = 0;
            for (Entry<LayoutCache.Key, LayoutPiece> entry : sCache.asMap().entrySet()) {
                size += ((LayoutCache.Key) entry.getKey()).getMemoryUsage();
                size += ((LayoutPiece) entry.getValue()).getMemoryUsage();
                size += 40;
            }
            return size;
        }
    }

    public static void clear() {
        if (sCache != null) {
            sCache.invalidateAll();
        }
    }

    private static class Key {

        char[] mChars;

        int mStart;

        int mLimit;

        FontCollection mFont;

        int mFlags;

        int mSize;

        Locale mLocale;

        boolean mIsRtl;

        private Key() {
        }

        private Key(@NonNull LayoutCache.LookupKey key) {
            this.mChars = new char[key.mContextLimit - key.mContextStart];
            System.arraycopy(key.mChars, key.mContextStart, this.mChars, 0, this.mChars.length);
            this.mStart = key.mStart;
            this.mLimit = key.mLimit;
            this.mFont = key.mFont;
            this.mFlags = key.mFlags;
            this.mSize = key.mSize;
            this.mLocale = key.mLocale;
            this.mIsRtl = key.mIsRtl;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (o.getClass() != LayoutCache.Key.class) {
                throw new IllegalStateException();
            } else {
                LayoutCache.Key key = (LayoutCache.Key) o;
                if (this.mStart != key.mStart) {
                    return false;
                } else if (this.mLimit != key.mLimit) {
                    return false;
                } else if (this.mFlags != key.mFlags) {
                    return false;
                } else if (this.mSize != key.mSize) {
                    return false;
                } else if (this.mIsRtl != key.mIsRtl) {
                    return false;
                } else if (!Arrays.equals(this.mChars, key.mChars)) {
                    return false;
                } else {
                    return !this.mFont.equals(key.mFont) ? false : this.mLocale.equals(key.mLocale);
                }
            }
        }

        public int hashCode() {
            int result = 1;
            for (char c : this.mChars) {
                result = 31 * result + c;
            }
            result = 31 * result + this.mFont.hashCode();
            result = 31 * result + this.mFlags;
            result = 31 * result + this.mSize;
            result = 31 * result + this.mStart;
            result = 31 * result + this.mLimit;
            result = 31 * result + this.mLocale.hashCode();
            return 31 * result + (this.mIsRtl ? 1 : 0);
        }

        private int getMemoryUsage() {
            return MathUtil.align8(61 + (this.mChars.length << 1));
        }
    }

    private static class LookupKey extends LayoutCache.Key {

        private int mContextStart;

        private int mContextLimit;

        public LookupKey() {
        }

        @NonNull
        public LayoutCache.Key update(@NonNull char[] text, int contextStart, int contextLimit, int start, int limit, @NonNull FontPaint paint, boolean dir) {
            this.mChars = text;
            this.mContextStart = contextStart;
            this.mContextLimit = contextLimit;
            this.mStart = start - contextStart;
            this.mLimit = limit - contextStart;
            this.mFont = paint.mFont;
            this.mFlags = paint.mFlags;
            this.mSize = paint.mSize;
            this.mLocale = paint.mLocale;
            this.mIsRtl = dir;
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (o.getClass() != LayoutCache.Key.class) {
                throw new IllegalStateException();
            } else {
                LayoutCache.Key key = (LayoutCache.Key) o;
                if (this.mStart != key.mStart) {
                    return false;
                } else if (this.mLimit != key.mLimit) {
                    return false;
                } else if (this.mFlags != key.mFlags) {
                    return false;
                } else if (this.mSize != key.mSize) {
                    return false;
                } else if (this.mIsRtl != key.mIsRtl) {
                    return false;
                } else if (!Arrays.equals(this.mChars, this.mContextStart, this.mContextLimit, key.mChars, 0, key.mChars.length)) {
                    return false;
                } else {
                    return !this.mFont.equals(key.mFont) ? false : this.mLocale.equals(key.mLocale);
                }
            }
        }

        @Override
        public int hashCode() {
            int result = 1;
            for (int i = this.mContextStart; i < this.mContextLimit; i++) {
                result = 31 * result + this.mChars[i];
            }
            result = 31 * result + this.mFont.hashCode();
            result = 31 * result + this.mFlags;
            result = 31 * result + this.mSize;
            result = 31 * result + this.mStart;
            result = 31 * result + this.mLimit;
            result = 31 * result + this.mLocale.hashCode();
            return 31 * result + (this.mIsRtl ? 1 : 0);
        }

        @NonNull
        public LayoutCache.Key copy() {
            return new LayoutCache.Key(this);
        }
    }
}