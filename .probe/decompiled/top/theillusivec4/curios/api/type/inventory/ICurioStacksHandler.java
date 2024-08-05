package top.theillusivec4.curios.api.type.inventory;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import top.theillusivec4.curios.api.type.capability.ICurio;

public interface ICurioStacksHandler {

    IDynamicStackHandler getStacks();

    IDynamicStackHandler getCosmeticStacks();

    NonNullList<Boolean> getRenders();

    default boolean canToggleRendering() {
        return true;
    }

    default ICurio.DropRule getDropRule() {
        return ICurio.DropRule.DEFAULT;
    }

    int getSlots();

    boolean isVisible();

    boolean hasCosmetic();

    CompoundTag serializeNBT();

    void deserializeNBT(CompoundTag var1);

    String getIdentifier();

    Map<UUID, AttributeModifier> getModifiers();

    Set<AttributeModifier> getPermanentModifiers();

    Set<AttributeModifier> getCachedModifiers();

    Collection<AttributeModifier> getModifiersByOperation(AttributeModifier.Operation var1);

    void addTransientModifier(AttributeModifier var1);

    void addPermanentModifier(AttributeModifier var1);

    void removeModifier(UUID var1);

    void clearModifiers();

    void clearCachedModifiers();

    void copyModifiers(ICurioStacksHandler var1);

    void update();

    CompoundTag getSyncTag();

    void applySyncTag(CompoundTag var1);

    @Deprecated
    int getSizeShift();

    @Deprecated
    void grow(int var1);

    @Deprecated
    void shrink(int var1);
}