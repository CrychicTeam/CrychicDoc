package org.violetmoon.quark.api;

import org.jetbrains.annotations.Nullable;

public interface ISortingLockedSlots extends IQuarkButtonAllowed {

    @Nullable
    int[] getSortingLockedSlots(boolean var1);
}