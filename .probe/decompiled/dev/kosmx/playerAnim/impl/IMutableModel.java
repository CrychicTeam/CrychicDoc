package dev.kosmx.playerAnim.impl;

import dev.kosmx.playerAnim.core.impl.AnimationProcessor;
import dev.kosmx.playerAnim.core.util.SetableSupplier;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public interface IMutableModel {

    void setEmoteSupplier(SetableSupplier<AnimationProcessor> var1);

    SetableSupplier<AnimationProcessor> getEmoteSupplier();
}