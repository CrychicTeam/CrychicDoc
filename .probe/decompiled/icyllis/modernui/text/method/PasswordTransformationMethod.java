package icyllis.modernui.text.method;

import icyllis.modernui.graphics.Rect;
import icyllis.modernui.text.Editable;
import icyllis.modernui.text.GetChars;
import icyllis.modernui.text.NoCopySpan;
import icyllis.modernui.text.Spannable;
import icyllis.modernui.text.Spanned;
import icyllis.modernui.text.TextUtils;
import icyllis.modernui.text.TextWatcher;
import icyllis.modernui.text.style.UpdateLayout;
import icyllis.modernui.view.View;
import java.lang.ref.WeakReference;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PasswordTransformationMethod implements TransformationMethod, TextWatcher {

    private static final PasswordTransformationMethod sInstance = new PasswordTransformationMethod();

    private static final char DOT = '•';

    private PasswordTransformationMethod() {
    }

    public static PasswordTransformationMethod getInstance() {
        return sInstance;
    }

    @Nonnull
    @Override
    public CharSequence getTransformation(@Nonnull CharSequence source, @Nonnull View view) {
        if (source instanceof Spannable sp) {
            for (PasswordTransformationMethod.ViewReference r : sp.getSpans(0, sp.length(), PasswordTransformationMethod.ViewReference.class)) {
                sp.removeSpan(r);
            }
            removeVisibleSpans(sp);
            sp.setSpan(new PasswordTransformationMethod.ViewReference(view), 0, 0, 34);
        }
        return new PasswordTransformationMethod.PasswordCharSequence(source);
    }

    @Override
    public void onFocusChanged(@Nonnull View view, @Nonnull CharSequence sourceText, boolean focused, int direction, @Nullable Rect previouslyFocusedRect) {
        if (!focused && sourceText instanceof Spannable) {
            removeVisibleSpans((Spannable) sourceText);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s instanceof Spannable sp) {
            List<PasswordTransformationMethod.ViewReference> vr = sp.getSpans(0, s.length(), PasswordTransformationMethod.ViewReference.class);
            if (vr.isEmpty()) {
                return;
            }
            View v = null;
            for (int i = 0; v == null && i < vr.size(); i++) {
                v = (View) ((PasswordTransformationMethod.ViewReference) vr.get(i)).get();
            }
            if (v == null) {
                return;
            }
            if (count > 0) {
                removeVisibleSpans(sp);
                if (count == 1) {
                    PasswordTransformationMethod.Visible visible = new PasswordTransformationMethod.Visible(sp, this);
                    sp.setSpan(visible, start, start + count, 33);
                    v.postDelayed(visible, 1500L);
                }
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    private static void removeVisibleSpans(@Nonnull Spannable sp) {
        for (PasswordTransformationMethod.Visible visible : sp.getSpans(0, sp.length(), PasswordTransformationMethod.Visible.class)) {
            sp.removeSpan(visible);
        }
    }

    private static class PasswordCharSequence implements CharSequence, GetChars {

        private final CharSequence mSource;

        public PasswordCharSequence(CharSequence source) {
            this.mSource = source;
        }

        public int length() {
            return this.mSource.length();
        }

        public char charAt(int i) {
            if (this.mSource instanceof Spanned sp) {
                for (PasswordTransformationMethod.Visible value : sp.getSpans(0, sp.length(), PasswordTransformationMethod.Visible.class)) {
                    if (sp.getSpanStart(value.mTransformer) >= 0) {
                        int st = sp.getSpanStart(value);
                        int en = sp.getSpanEnd(value);
                        if (i >= st && i < en) {
                            return this.mSource.charAt(i);
                        }
                    }
                }
            }
            return '•';
        }

        @Nonnull
        public CharSequence subSequence(int start, int end) {
            char[] buf = new char[end - start];
            this.getChars(start, end, buf, 0);
            return new String(buf);
        }

        @Nonnull
        public String toString() {
            char[] buf = new char[this.mSource.length()];
            this.getChars(0, this.mSource.length(), buf, 0);
            return new String(buf);
        }

        @Override
        public void getChars(int start, int end, char[] dest, int off) {
            TextUtils.getChars(this.mSource, start, end, dest, off);
            int count = 0;
            int[] starts = null;
            int[] ends = null;
            if (this.mSource instanceof Spanned sp) {
                List<PasswordTransformationMethod.Visible> visible = sp.getSpans(0, sp.length(), PasswordTransformationMethod.Visible.class);
                count = visible.size();
                starts = new int[count];
                ends = new int[count];
                for (int i = 0; i < count; i++) {
                    if (sp.getSpanStart(((PasswordTransformationMethod.Visible) visible.get(i)).mTransformer) >= 0) {
                        starts[i] = sp.getSpanStart(visible.get(i));
                        ends[i] = sp.getSpanEnd(visible.get(i));
                    }
                }
            }
            for (int ix = start; ix < end; ix++) {
                boolean visible = false;
                for (int a = 0; a < count; a++) {
                    if (ix >= starts[a] && ix < ends[a]) {
                        visible = true;
                        break;
                    }
                }
                if (!visible) {
                    dest[ix - start + off] = 8226;
                }
            }
        }
    }

    private static class ViewReference extends WeakReference<View> implements NoCopySpan {

        private ViewReference(View v) {
            super(v);
        }
    }

    private static class Visible implements UpdateLayout, Runnable {

        private final Spannable mText;

        private final PasswordTransformationMethod mTransformer;

        public Visible(Spannable sp, PasswordTransformationMethod ptm) {
            this.mText = sp;
            this.mTransformer = ptm;
        }

        public void run() {
            this.mText.removeSpan(this);
        }
    }
}