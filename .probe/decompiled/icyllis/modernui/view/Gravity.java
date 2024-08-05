package icyllis.modernui.view;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.graphics.Rect;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.StringJoiner;
import org.jetbrains.annotations.ApiStatus.Internal;

public class Gravity {

    public static final int NO_GRAVITY = 0;

    public static final int AXIS_SPECIFIED = 1;

    public static final int AXIS_PULL_BEFORE = 2;

    public static final int AXIS_PULL_AFTER = 4;

    public static final int AXIS_CLIP = 8;

    public static final int AXIS_X_SHIFT = 0;

    public static final int AXIS_Y_SHIFT = 4;

    public static final int TOP = 48;

    public static final int BOTTOM = 80;

    public static final int LEFT = 3;

    public static final int RIGHT = 5;

    public static final int CENTER_VERTICAL = 16;

    public static final int FILL_VERTICAL = 112;

    public static final int CENTER_HORIZONTAL = 1;

    public static final int FILL_HORIZONTAL = 7;

    public static final int CENTER = 17;

    public static final int FILL = 119;

    public static final int CLIP_VERTICAL = 128;

    public static final int CLIP_HORIZONTAL = 8;

    public static final int RELATIVE_LAYOUT_DIRECTION = 8388608;

    public static final int HORIZONTAL_GRAVITY_MASK = 7;

    public static final int VERTICAL_GRAVITY_MASK = 112;

    public static final int DISPLAY_CLIP_VERTICAL = 268435456;

    public static final int DISPLAY_CLIP_HORIZONTAL = 16777216;

    public static final int START = 8388611;

    public static final int END = 8388613;

    public static final int RELATIVE_HORIZONTAL_GRAVITY_MASK = 8388615;

    public static void apply(int gravity, int w, int h, @NonNull Rect container, @NonNull Rect outRect) {
        apply(gravity, w, h, container, 0, 0, outRect);
    }

    public static void apply(int gravity, int w, int h, @NonNull Rect container, @NonNull Rect outRect, int layoutDirection) {
        int absGravity = getAbsoluteGravity(gravity, layoutDirection);
        apply(absGravity, w, h, container, 0, 0, outRect);
    }

    public static void apply(int gravity, int w, int h, @NonNull Rect container, int xAdj, int yAdj, @NonNull Rect outRect) {
        switch(gravity & 6) {
            case 0:
                outRect.left = container.left + (container.right - container.left - w) / 2 + xAdj;
                outRect.right = outRect.left + w;
                if ((gravity & 8) == 8) {
                    if (outRect.left < container.left) {
                        outRect.left = container.left;
                    }
                    if (outRect.right > container.right) {
                        outRect.right = container.right;
                    }
                }
                break;
            case 1:
            case 3:
            default:
                outRect.left = container.left + xAdj;
                outRect.right = container.right + xAdj;
                break;
            case 2:
                outRect.left = container.left + xAdj;
                outRect.right = outRect.left + w;
                if ((gravity & 8) == 8 && outRect.right > container.right) {
                    outRect.right = container.right;
                }
                break;
            case 4:
                outRect.right = container.right - xAdj;
                outRect.left = outRect.right - w;
                if ((gravity & 8) == 8 && outRect.left < container.left) {
                    outRect.left = container.left;
                }
        }
        switch(gravity & 96) {
            case 0:
                outRect.top = container.top + (container.bottom - container.top - h) / 2 + yAdj;
                outRect.bottom = outRect.top + h;
                if ((gravity & 128) == 128) {
                    if (outRect.top < container.top) {
                        outRect.top = container.top;
                    }
                    if (outRect.bottom > container.bottom) {
                        outRect.bottom = container.bottom;
                    }
                }
                break;
            case 32:
                outRect.top = container.top + yAdj;
                outRect.bottom = outRect.top + h;
                if ((gravity & 128) == 128 && outRect.bottom > container.bottom) {
                    outRect.bottom = container.bottom;
                }
                break;
            case 64:
                outRect.bottom = container.bottom - yAdj;
                outRect.top = outRect.bottom - h;
                if ((gravity & 128) == 128 && outRect.top < container.top) {
                    outRect.top = container.top;
                }
                break;
            default:
                outRect.top = container.top + yAdj;
                outRect.bottom = container.bottom + yAdj;
        }
    }

    public static void apply(int gravity, int w, int h, @NonNull Rect container, int xAdj, int yAdj, @NonNull Rect outRect, int layoutDirection) {
        int absGravity = getAbsoluteGravity(gravity, layoutDirection);
        apply(absGravity, w, h, container, xAdj, yAdj, outRect);
    }

    public static void applyDisplay(int gravity, @NonNull Rect display, @NonNull Rect inoutObj) {
        if ((gravity & 268435456) != 0) {
            if (inoutObj.top < display.top) {
                inoutObj.top = display.top;
            }
            if (inoutObj.bottom > display.bottom) {
                inoutObj.bottom = display.bottom;
            }
        } else {
            int off = 0;
            if (inoutObj.top < display.top) {
                off = display.top - inoutObj.top;
            } else if (inoutObj.bottom > display.bottom) {
                off = display.bottom - inoutObj.bottom;
            }
            if (off != 0) {
                if (inoutObj.height() > display.bottom - display.top) {
                    inoutObj.top = display.top;
                    inoutObj.bottom = display.bottom;
                } else {
                    inoutObj.top += off;
                    inoutObj.bottom += off;
                }
            }
        }
        if ((gravity & 16777216) != 0) {
            if (inoutObj.left < display.left) {
                inoutObj.left = display.left;
            }
            if (inoutObj.right > display.right) {
                inoutObj.right = display.right;
            }
        } else {
            int offx = 0;
            if (inoutObj.left < display.left) {
                offx = display.left - inoutObj.left;
            } else if (inoutObj.right > display.right) {
                offx = display.right - inoutObj.right;
            }
            if (offx != 0) {
                if (inoutObj.width() > display.right - display.left) {
                    inoutObj.left = display.left;
                    inoutObj.right = display.right;
                } else {
                    inoutObj.left += offx;
                    inoutObj.right += offx;
                }
            }
        }
    }

    public static void applyDisplay(int gravity, @NonNull Rect display, @NonNull Rect inoutObj, int layoutDirection) {
        int absGravity = getAbsoluteGravity(gravity, layoutDirection);
        applyDisplay(absGravity, display, inoutObj);
    }

    public static boolean isVertical(int gravity) {
        return gravity > 0 && (gravity & 112) != 0;
    }

    public static boolean isHorizontal(int gravity) {
        return gravity > 0 && (gravity & 8388615) != 0;
    }

    public static int getAbsoluteGravity(int gravity, int layoutDirection) {
        int result = gravity;
        if ((gravity & 8388608) > 0) {
            if ((gravity & 8388611) == 8388611) {
                result = gravity & -8388612;
                if (layoutDirection == 1) {
                    result |= 5;
                } else {
                    result |= 3;
                }
            } else if ((gravity & 8388613) == 8388613) {
                result = gravity & -8388614;
                if (layoutDirection == 1) {
                    result |= 3;
                } else {
                    result |= 5;
                }
            }
            result &= -8388609;
        }
        return result;
    }

    @Internal
    public static String toString(int gravity) {
        StringJoiner result = new StringJoiner(" ");
        if ((gravity & 119) == 119) {
            result.add("FILL");
        } else {
            if ((gravity & 112) == 112) {
                result.add("FILL_VERTICAL");
            } else {
                if ((gravity & 48) == 48) {
                    result.add("TOP");
                }
                if ((gravity & 80) == 80) {
                    result.add("BOTTOM");
                }
            }
            if ((gravity & 7) == 7) {
                result.add("FILL_HORIZONTAL");
            } else {
                if ((gravity & 8388611) == 8388611) {
                    result.add("START");
                } else if ((gravity & 3) == 3) {
                    result.add("LEFT");
                }
                if ((gravity & 8388613) == 8388613) {
                    result.add("END");
                } else if ((gravity & 5) == 5) {
                    result.add("RIGHT");
                }
            }
        }
        if ((gravity & 17) == 17) {
            result.add("CENTER");
        } else {
            if ((gravity & 16) == 16) {
                result.add("CENTER_VERTICAL");
            }
            if ((gravity & 1) == 1) {
                result.add("CENTER_HORIZONTAL");
            }
        }
        if (result.length() == 0) {
            result.add("NO GRAVITY");
        }
        if ((gravity & 268435456) == 268435456) {
            result.add("DISPLAY_CLIP_VERTICAL");
        }
        if ((gravity & 16777216) == 16777216) {
            result.add("DISPLAY_CLIP_HORIZONTAL");
        }
        return result.toString();
    }

    @Retention(RetentionPolicy.SOURCE)
    @Internal
    public @interface GravityFlags {
    }
}