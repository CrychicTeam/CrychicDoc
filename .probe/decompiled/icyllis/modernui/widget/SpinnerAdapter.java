package icyllis.modernui.widget;

import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface SpinnerAdapter extends Adapter {

    View getDropDownView(int var1, @Nullable View var2, @Nonnull ViewGroup var3);
}