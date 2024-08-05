package net.mehvahdjukaar.moonlight.core.misc;

import net.mehvahdjukaar.moonlight.api.item.additional_placements.AdditionalItemPlacement;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public interface IExtendedItem {

    @Nullable
    AdditionalItemPlacement moonlight$getAdditionalBehavior();

    void moonlight$addAdditionalBehavior(AdditionalItemPlacement var1);

    @Nullable
    Object moonlight$getClientAnimationExtension();

    void moonlight$setClientAnimationExtension(Object var1);
}