package snownee.kiwi.util;

import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public interface LocalizableItem {

    Component getDisplayName();

    @Nullable
    default Component getDescription() {
        return null;
    }
}