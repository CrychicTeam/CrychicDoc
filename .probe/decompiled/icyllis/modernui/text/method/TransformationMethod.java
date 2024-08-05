package icyllis.modernui.text.method;

import icyllis.modernui.graphics.Rect;
import icyllis.modernui.view.View;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface TransformationMethod {

    @Nonnull
    CharSequence getTransformation(@Nonnull CharSequence var1, @Nonnull View var2);

    void onFocusChanged(@Nonnull View var1, @Nonnull CharSequence var2, boolean var3, int var4, @Nullable Rect var5);
}