package icyllis.modernui.view;

import icyllis.modernui.graphics.Rect;
import org.jetbrains.annotations.ApiStatus.Internal;

public abstract class ActionMode {

    public static final int TYPE_PRIMARY = 0;

    public static final int TYPE_FLOATING = 1;

    public static final int DEFAULT_HIDE_DURATION = -1;

    private Object mTag;

    private boolean mTitleOptionalHint;

    private int mType = 0;

    public void setTag(Object tag) {
        this.mTag = tag;
    }

    public Object getTag() {
        return this.mTag;
    }

    public abstract void setTitle(CharSequence var1);

    public abstract void setSubtitle(CharSequence var1);

    public void setTitleOptionalHint(boolean titleOptional) {
        this.mTitleOptionalHint = titleOptional;
    }

    public boolean getTitleOptionalHint() {
        return this.mTitleOptionalHint;
    }

    public boolean isTitleOptional() {
        return false;
    }

    public abstract void setCustomView(View var1);

    public void setType(int type) {
        this.mType = type;
    }

    public int getType() {
        return this.mType;
    }

    public abstract void invalidate();

    public void invalidateContentRect() {
    }

    public void hide(long duration) {
    }

    public abstract void finish();

    public abstract Menu getMenu();

    public abstract CharSequence getTitle();

    public abstract CharSequence getSubtitle();

    public abstract View getCustomView();

    public void onWindowFocusChanged(boolean hasWindowFocus) {
    }

    @Internal
    public boolean isUiFocusable() {
        return true;
    }

    public interface Callback {

        boolean onCreateActionMode(ActionMode var1, Menu var2);

        boolean onPrepareActionMode(ActionMode var1, Menu var2);

        boolean onActionItemClicked(ActionMode var1, MenuItem var2);

        void onDestroyActionMode(ActionMode var1);

        default void onGetContentRect(ActionMode mode, View view, Rect outRect) {
            if (view != null) {
                outRect.set(0, 0, view.getWidth(), view.getHeight());
            } else {
                outRect.set(0, 0, 0, 0);
            }
        }
    }
}