package net.minecraftforge.common.extensions;

import java.util.Collection;
import net.minecraft.server.packs.PackResources;
import org.jetbrains.annotations.Nullable;

public interface IForgePackResources {

    default boolean isHidden() {
        return false;
    }

    @Nullable
    default Collection<PackResources> getChildren() {
        return null;
    }
}