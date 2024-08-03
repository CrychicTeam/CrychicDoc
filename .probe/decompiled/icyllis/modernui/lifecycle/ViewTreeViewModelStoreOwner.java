package icyllis.modernui.lifecycle;

import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewParent;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ViewTreeViewModelStoreOwner {

    private ViewTreeViewModelStoreOwner() {
    }

    public static void set(@Nonnull View view, @Nullable ViewModelStoreOwner viewModelStoreOwner) {
        view.setTag(50462722, viewModelStoreOwner);
    }

    @Nullable
    public static ViewModelStoreOwner get(@Nonnull View view) {
        ViewModelStoreOwner found = (ViewModelStoreOwner) view.getTag(50462722);
        if (found != null) {
            return found;
        } else {
            ViewParent parent = view.getParent();
            while (found == null && parent instanceof View) {
                View parentView = (View) parent;
                found = (ViewModelStoreOwner) parentView.getTag(50462722);
                parent = parentView.getParent();
            }
            return found;
        }
    }
}