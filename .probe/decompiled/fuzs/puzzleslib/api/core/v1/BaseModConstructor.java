package fuzs.puzzleslib.api.core.v1;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public interface BaseModConstructor {

    @Nullable
    default ResourceLocation getPairingIdentifier() {
        return null;
    }

    default ContentRegistrationFlags[] getContentRegistrationFlags() {
        return new ContentRegistrationFlags[0];
    }
}