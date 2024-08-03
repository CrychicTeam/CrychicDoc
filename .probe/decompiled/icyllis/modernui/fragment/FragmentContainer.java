package icyllis.modernui.fragment;

import icyllis.modernui.view.View;
import javax.annotation.Nullable;

interface FragmentContainer {

    @Nullable
    View onFindViewById(int var1);

    boolean onHasView();
}