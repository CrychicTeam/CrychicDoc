package top.theillusivec4.curios.api.type;

import java.util.Set;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import top.theillusivec4.curios.api.type.capability.ICurio;

public interface ISlotType extends Comparable<ISlotType> {

    String getIdentifier();

    ResourceLocation getIcon();

    int getOrder();

    int getSize();

    boolean useNativeGui();

    boolean hasCosmetic();

    boolean canToggleRendering();

    ICurio.DropRule getDropRule();

    default Set<ResourceLocation> getValidators() {
        return Set.of();
    }

    default CompoundTag writeNbt() {
        return new CompoundTag();
    }

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    default boolean isLocked() {
        return this.getSize() == 0;
    }

    @Deprecated(since = "1.20.1", forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.22")
    default int getPriority() {
        return this.getOrder();
    }

    @Deprecated(since = "1.20.1", forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.22")
    default boolean isVisible() {
        return this.useNativeGui();
    }
}