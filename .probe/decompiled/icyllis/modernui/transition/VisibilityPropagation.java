package icyllis.modernui.transition;

import icyllis.modernui.view.View;
import javax.annotation.Nonnull;

public abstract class VisibilityPropagation extends TransitionPropagation {

    private static final String PROPNAME_VISIBILITY = "modernui:visibilityPropagation:visibility";

    private static final String PROPNAME_VIEW_CENTER = "modernui:visibilityPropagation:center";

    private static final String[] VISIBILITY_PROPAGATION_VALUES = new String[] { "modernui:visibilityPropagation:visibility", "modernui:visibilityPropagation:center" };

    @Override
    public void captureValues(@Nonnull TransitionValues values) {
        View view = values.view;
        Integer visibility = (Integer) values.values.get("modernui:visibility:visibility");
        if (visibility == null) {
            visibility = view.getVisibility();
        }
        values.values.put("modernui:visibilityPropagation:visibility", visibility);
        int[] loc = new int[2];
        view.getLocationInWindow(loc);
        loc[0] += Math.round(view.getTranslationX());
        loc[0] += view.getWidth() / 2;
        loc[1] += Math.round(view.getTranslationY());
        loc[1] += view.getHeight() / 2;
        values.values.put("modernui:visibilityPropagation:center", loc);
    }

    @Override
    public String[] getPropagationProperties() {
        return VISIBILITY_PROPAGATION_VALUES;
    }

    public int getViewVisibility(TransitionValues values) {
        if (values == null) {
            return 8;
        } else {
            Integer visibility = (Integer) values.values.get("modernui:visibilityPropagation:visibility");
            return visibility == null ? 8 : visibility;
        }
    }

    public int getViewX(TransitionValues values) {
        return getViewCoordinate(values, 0);
    }

    public int getViewY(TransitionValues values) {
        return getViewCoordinate(values, 1);
    }

    private static int getViewCoordinate(TransitionValues values, int coordinateIndex) {
        if (values == null) {
            return -1;
        } else {
            int[] coordinates = (int[]) values.values.get("modernui:visibilityPropagation:center");
            return coordinates == null ? -1 : coordinates[coordinateIndex];
        }
    }
}