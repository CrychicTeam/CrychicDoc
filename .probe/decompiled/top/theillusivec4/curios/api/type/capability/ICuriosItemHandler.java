package top.theillusivec4.curios.api.type.capability;

import com.google.common.collect.Multimap;
import com.mojang.logging.LogUtils;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Tuple;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import org.slf4j.Logger;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

public interface ICuriosItemHandler {

    Logger LOGGER = LogUtils.getLogger();

    Map<String, ICurioStacksHandler> getCurios();

    void setCurios(Map<String, ICurioStacksHandler> var1);

    int getSlots();

    default int getVisibleSlots() {
        return this.getSlots();
    }

    void reset();

    Optional<ICurioStacksHandler> getStacksHandler(String var1);

    IItemHandlerModifiable getEquippedCurios();

    void setEquippedCurio(String var1, int var2, ItemStack var3);

    default boolean isEquipped(Item item) {
        return this.findFirstCurio(item).isPresent();
    }

    default boolean isEquipped(Predicate<ItemStack> filter) {
        return this.findFirstCurio(filter).isPresent();
    }

    Optional<SlotResult> findFirstCurio(Item var1);

    Optional<SlotResult> findFirstCurio(Predicate<ItemStack> var1);

    List<SlotResult> findCurios(Item var1);

    List<SlotResult> findCurios(Predicate<ItemStack> var1);

    List<SlotResult> findCurios(String... var1);

    Optional<SlotResult> findCurio(String var1, int var2);

    LivingEntity getWearer();

    void loseInvalidStack(ItemStack var1);

    void handleInvalidStacks();

    int getFortuneLevel(@Nullable LootContext var1);

    int getLootingLevel(DamageSource var1, LivingEntity var2, int var3);

    ListTag saveInventory(boolean var1);

    void loadInventory(ListTag var1);

    Set<ICurioStacksHandler> getUpdatingInventories();

    default void addTransientSlotModifier(String slot, UUID uuid, String name, double amount, AttributeModifier.Operation operation) {
        LOGGER.error("Missing method implementation!");
    }

    void addTransientSlotModifiers(Multimap<String, AttributeModifier> var1);

    default void addPermanentSlotModifier(String slot, UUID uuid, String name, double amount, AttributeModifier.Operation operation) {
        LOGGER.error("Missing method implementation!");
    }

    void addPermanentSlotModifiers(Multimap<String, AttributeModifier> var1);

    default void removeSlotModifier(String slot, UUID uuid) {
        LOGGER.error("Missing method implementation!");
    }

    void removeSlotModifiers(Multimap<String, AttributeModifier> var1);

    void clearSlotModifiers();

    Multimap<String, AttributeModifier> getModifiers();

    Tag writeTag();

    void readTag(Tag var1);

    void clearCachedSlotModifiers();

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    default Set<String> getLockedSlots() {
        return new HashSet();
    }

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    default void unlockSlotType(String identifier, int amount, boolean visible, boolean cosmetic) {
        this.growSlotType(identifier, amount);
    }

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    default void lockSlotType(String identifier) {
        this.shrinkSlotType(identifier, 1);
    }

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    default void processSlots() {
    }

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    default int getFortuneBonus() {
        return 0;
    }

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    default int getLootingBonus() {
        return 0;
    }

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    default void setEnchantmentBonuses(Tuple<Integer, Integer> fortuneAndLooting) {
    }

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    void growSlotType(String var1, int var2);

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    void shrinkSlotType(String var1, int var2);
}