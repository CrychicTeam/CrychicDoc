package icyllis.modernui.view;

import icyllis.modernui.annotation.NonNull;

public class MeasureSpec {

    private static final int MODE_SHIFT = 30;

    private static final int MODE_MASK = -1073741824;

    public static final int UNSPECIFIED = 0;

    public static final int EXACTLY = 1073741824;

    public static final int AT_MOST = Integer.MIN_VALUE;

    public static int makeMeasureSpec(int size, int mode) {
        return size & 1073741823 | mode & -1073741824;
    }

    public static int getSize(int measureSpec) {
        return measureSpec & 1073741823;
    }

    public static int getMode(int measureSpec) {
        return measureSpec & -1073741824;
    }

    @NonNull
    public static String toString(int measureSpec) {
        int mode = getMode(measureSpec);
        int size = getSize(measureSpec);
        StringBuilder sb = new StringBuilder("MeasureSpec: ");
        switch(mode) {
            case Integer.MIN_VALUE:
                sb.append("AT_MOST ");
                break;
            case 0:
                sb.append("UNSPECIFIED ");
                break;
            case 1073741824:
                sb.append("EXACTLY ");
                break;
            default:
                sb.append(mode).append(" ");
        }
        sb.append(size);
        return sb.toString();
    }
}