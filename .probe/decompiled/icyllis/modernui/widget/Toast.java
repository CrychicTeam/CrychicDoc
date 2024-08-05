package icyllis.modernui.widget;

import icyllis.modernui.ModernUI;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.app.Activity;
import icyllis.modernui.core.Context;

public final class Toast {

    public static final int LENGTH_SHORT = 0;

    public static final int LENGTH_LONG = 1;

    private final Context mContext;

    private int mDuration;

    private CharSequence mText;

    private Toast(@NonNull Context context) {
        this.mContext = context;
    }

    @Deprecated(forRemoval = true)
    public static Toast makeText(@NonNull CharSequence text, int duration) {
        return makeText(ModernUI.getInstance(), text, duration);
    }

    @NonNull
    public static Toast makeText(@NonNull Context context, @NonNull CharSequence text, int duration) {
        Toast toast = new Toast(context);
        toast.mText = text;
        toast.mDuration = duration;
        return toast;
    }

    public void show() {
        ((Activity) this.mContext).getToastManager().enqueueToast(this, this.mText, this.mDuration);
    }

    public void cancel() {
        ((Activity) this.mContext).getToastManager().cancelToast(this);
    }

    public void setDuration(int duration) {
        this.mDuration = duration;
    }

    public int getDuration() {
        return this.mDuration;
    }

    public void setText(@NonNull CharSequence s) {
        this.mText = s;
    }
}