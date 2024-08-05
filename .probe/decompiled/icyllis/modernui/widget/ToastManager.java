package icyllis.modernui.widget;

import icyllis.modernui.ModernUI;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.app.Activity;
import icyllis.modernui.core.Core;
import icyllis.modernui.graphics.drawable.ShapeDrawable;
import icyllis.modernui.text.TextUtils;
import icyllis.modernui.view.WindowManager;
import java.util.ArrayDeque;
import javax.annotation.concurrent.GuardedBy;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public final class ToastManager {

    static final Marker MARKER = MarkerManager.getMarker("Toast");

    private static final int MAX_TOASTS = 5;

    static final int LONG_DELAY = 3500;

    static final int SHORT_DELAY = 2000;

    private final WindowManager mWindowManager;

    private final ArrayDeque<ToastManager.ToastRecord> mToastQueue = new ArrayDeque(5);

    @GuardedBy("mToastQueue")
    private Toast mCurrentToastShown;

    private final Runnable mDurationReached = this::onDurationReached;

    private final TextView mTextView;

    private final WindowManager.LayoutParams mParams;

    private final ShapeDrawable mBackground = new ShapeDrawable();

    public ToastManager(Activity activity) {
        this.mWindowManager = activity.getWindowManager();
        this.mTextView = new TextView(activity);
        this.mParams = new WindowManager.LayoutParams();
        this.mParams.width = -2;
        this.mParams.height = -2;
        this.mParams.gravity = 81;
        this.mParams.flags |= 8;
        this.mParams.type = 2005;
        this.mTextView.setEllipsize(TextUtils.TruncateAt.END);
        this.mTextView.setMaxLines(2);
        this.mBackground.setShape(0);
        this.mBackground.setColor(-1073741824);
        this.mTextView.setBackground(this.mBackground);
    }

    @Nullable
    @GuardedBy("mToastQueue")
    private ToastManager.ToastRecord getToastLocked(@NonNull Toast token) {
        for (ToastManager.ToastRecord r : this.mToastQueue) {
            if (r.mToken == token) {
                return r;
            }
        }
        return null;
    }

    private void showNextToastLocked() {
        if (this.mCurrentToastShown == null) {
            ToastManager.ToastRecord r = (ToastManager.ToastRecord) this.mToastQueue.getFirst();
            this.mTextView.setText(r.mText);
            this.mTextView.setTextSize(14.0F);
            this.mTextView.setTypeface(ModernUI.getSelectedTypeface());
            this.mTextView.setMaxWidth(this.mTextView.dp(300.0F));
            this.mTextView.setPadding(this.mTextView.dp(16.0F), this.mTextView.dp(12.0F), this.mTextView.dp(16.0F), this.mTextView.dp(12.0F));
            this.mParams.y = this.mTextView.dp(64.0F);
            this.mBackground.setCornerRadius((float) this.mTextView.dp(28.0F));
            this.mWindowManager.addView(this.mTextView, this.mParams);
            int delay = r.getDuration() == 1 ? 3500 : 2000;
            delay += 300;
            Core.getUiHandlerAsync().postDelayed(this.mDurationReached, (long) delay);
            this.mCurrentToastShown = r.mToken;
        }
    }

    private void onDurationReached() {
        synchronized (this.mToastQueue) {
            if (this.mCurrentToastShown != null) {
                ToastManager.ToastRecord record = this.getToastLocked(this.mCurrentToastShown);
                this.mCurrentToastShown = null;
                if (record != null) {
                    this.cancelToastLocked(record);
                }
            }
        }
    }

    private void cancelToastLocked(@NonNull ToastManager.ToastRecord record) {
        this.mWindowManager.removeView(this.mTextView);
        this.mToastQueue.remove(record);
        if (this.mToastQueue.size() > 0) {
            this.showNextToastLocked();
        }
    }

    public void enqueueToast(@NonNull Toast token, @NonNull CharSequence text, int duration) {
        synchronized (this.mToastQueue) {
            ToastManager.ToastRecord record = this.getToastLocked(token);
            if (record != null) {
                record.update(duration);
            } else {
                if (this.mToastQueue.size() >= 5) {
                    ModernUI.LOGGER.error(MARKER, "System has already queued {} toasts. Not showing more.", this.mToastQueue.size());
                    return;
                }
                record = new ToastManager.ToastRecord(token, text, duration);
                this.mToastQueue.addLast(record);
            }
            if (this.mToastQueue.size() == 1) {
                this.showNextToastLocked();
            }
        }
    }

    public void cancelToast(@NonNull Toast token) {
        synchronized (this.mToastQueue) {
            ToastManager.ToastRecord r = this.getToastLocked(token);
            if (r != null) {
                this.cancelToastLocked(r);
            } else {
                ModernUI.LOGGER.warn(MARKER, "Toast already cancelled. token={}", token);
            }
        }
    }

    static final class ToastRecord {

        public final Toast mToken;

        public final CharSequence mText;

        private int mDuration;

        ToastRecord(Toast token, CharSequence text, int duration) {
            this.mToken = token;
            this.mText = text;
            this.mDuration = duration;
        }

        public int getDuration() {
            return this.mDuration;
        }

        public void update(int duration) {
            this.mDuration = duration;
        }
    }
}