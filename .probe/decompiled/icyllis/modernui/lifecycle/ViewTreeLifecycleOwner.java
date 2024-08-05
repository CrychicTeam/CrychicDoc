package icyllis.modernui.lifecycle;

import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewParent;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ViewTreeLifecycleOwner {

    private ViewTreeLifecycleOwner() {
    }

    public static void set(@Nonnull View view, @Nullable LifecycleOwner lifecycleOwner) {
        view.setTag(50462721, lifecycleOwner);
    }

    @Nullable
    public static LifecycleOwner get(@Nonnull View view) {
        LifecycleOwner found = (LifecycleOwner) view.getTag(50462721);
        if (found != null) {
            return found;
        } else {
            ViewParent parent = view.getParent();
            while (found == null && parent instanceof View) {
                View parentView = (View) parent;
                found = (LifecycleOwner) parentView.getTag(50462721);
                parent = parentView.getParent();
            }
            return found;
        }
    }
}