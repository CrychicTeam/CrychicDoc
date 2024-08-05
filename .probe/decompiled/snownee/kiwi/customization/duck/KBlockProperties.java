package snownee.kiwi.customization.duck;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.NonExtendable;
import snownee.kiwi.customization.block.KBlockSettings;

public interface KBlockProperties {

    @Nullable
    @NonExtendable
    KBlockSettings kiwi$getSettings();

    @NonExtendable
    void kiwi$setSettings(KBlockSettings var1);
}