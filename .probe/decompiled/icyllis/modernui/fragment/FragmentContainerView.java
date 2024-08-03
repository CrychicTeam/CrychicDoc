package icyllis.modernui.fragment;

import icyllis.modernui.core.Context;
import icyllis.modernui.widget.FrameLayout;

public final class FragmentContainerView extends FrameLayout {

    private boolean mDrawDisappearingViewsFirst = true;

    public FragmentContainerView(Context context) {
        super(context);
    }

    void setDrawDisappearingViewsLast(boolean drawDisappearingViewsFirst) {
        this.mDrawDisappearingViewsFirst = drawDisappearingViewsFirst;
    }
}