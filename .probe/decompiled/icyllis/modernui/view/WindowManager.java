package icyllis.modernui.view;

import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public interface WindowManager extends ViewManager {

    public static class LayoutParams extends ViewGroup.LayoutParams {

        public int x;

        public int y;

        public float horizontalWeight;

        public float verticalWeight;

        public int type = 1;

        public static final int FIRST_APPLICATION_WINDOW = 1;

        public static final int TYPE_BASE_APPLICATION = 1;

        public static final int LAST_APPLICATION_WINDOW = 99;

        public static final int FIRST_SUB_WINDOW = 1000;

        public static final int TYPE_APPLICATION_PANEL = 1000;

        public static final int TYPE_APPLICATION_SUB_PANEL = 1002;

        @Internal
        public static final int TYPE_APPLICATION_ABOVE_SUB_PANEL = 1005;

        public static final int LAST_SUB_WINDOW = 1999;

        public static final int FIRST_SYSTEM_WINDOW = 2000;

        @Internal
        public static final int TYPE_TOAST = 2005;

        public static final int LAST_SYSTEM_WINDOW = 2999;

        public static final int FLAG_NOT_FOCUSABLE = 8;

        public static final int FLAG_NOT_TOUCH_MODAL = 32;

        public int flags;

        public int gravity;

        public float horizontalMargin;

        public float verticalMargin;

        public LayoutParams() {
            super(-1, -1);
        }

        @Internal
        public boolean isModal() {
            return (this.flags & 40) == 0;
        }
    }
}