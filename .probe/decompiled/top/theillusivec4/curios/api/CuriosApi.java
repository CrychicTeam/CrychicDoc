package top.theillusivec4.curios.api;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.logging.LogUtils;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.loading.FMLLoader;
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import org.slf4j.Logger;
import top.theillusivec4.curios.api.type.ISlotType;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.util.ICuriosHelper;
import top.theillusivec4.curios.api.type.util.IIconHelper;
import top.theillusivec4.curios.api.type.util.ISlotHelper;

public final class CuriosApi {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final String MODID = "curios";

    private static IIconHelper iconHelper;

    private static ICuriosHelper curiosHelper;

    private static ISlotHelper slotHelper;

    public static void registerCurio(Item item, ICurioItem curio) {
        apiError();
    }

    public static Optional<ISlotType> getSlot(String id, Level level) {
        return getSlot(id, level.isClientSide());
    }

    public static Optional<ISlotType> getSlot(String id, boolean isClient) {
        return Optional.ofNullable((ISlotType) getSlots(isClient).get(id));
    }

    public static Map<String, ISlotType> getSlots(Level level) {
        return getSlots(level.isClientSide());
    }

    public static Map<String, ISlotType> getSlots(boolean isClient) {
        apiError();
        return Map.of();
    }

    public static Map<String, ISlotType> getPlayerSlots(Level level) {
        return getPlayerSlots(level.isClientSide());
    }

    public static Map<String, ISlotType> getPlayerSlots(boolean isClient) {
        return getEntitySlots(EntityType.PLAYER, isClient);
    }

    public static Map<String, ISlotType> getPlayerSlots(Player player) {
        return getEntitySlots(player);
    }

    public static Map<String, ISlotType> getEntitySlots(LivingEntity livingEntity) {
        return livingEntity != null ? getEntitySlots(livingEntity.m_6095_(), livingEntity.m_9236_()) : Map.of();
    }

    public static Map<String, ISlotType> getEntitySlots(EntityType<?> type, Level level) {
        return getEntitySlots(type, level.isClientSide());
    }

    public static Map<String, ISlotType> getEntitySlots(EntityType<?> type, boolean isClient) {
        apiError();
        return Map.of();
    }

    public static Map<String, ISlotType> getItemStackSlots(ItemStack stack, Level level) {
        return getItemStackSlots(stack, level.isClientSide());
    }

    public static Map<String, ISlotType> getItemStackSlots(ItemStack stack, boolean isClient) {
        apiError();
        return Map.of();
    }

    public static Map<String, ISlotType> getItemStackSlots(ItemStack stack, LivingEntity livingEntity) {
        apiError();
        return Map.of();
    }

    public static LazyOptional<ICurio> getCurio(ItemStack stack) {
        apiError();
        return LazyOptional.empty();
    }

    @Nonnull
    public static ICapabilityProvider createCurioProvider(ICurio curio) {
        apiError();
        return Items.AIR.getDefaultInstance();
    }

    public static LazyOptional<ICuriosItemHandler> getCuriosInventory(LivingEntity livingEntity) {
        apiError();
        return LazyOptional.empty();
    }

    public static boolean isStackValid(SlotContext slotContext, ItemStack stack) {
        apiError();
        return false;
    }

    public static Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        apiError();
        return HashMultimap.create();
    }

    public static void addSlotModifier(Multimap<Attribute, AttributeModifier> map, String identifier, UUID uuid, double amount, AttributeModifier.Operation operation) {
        apiError();
    }

    public static void addSlotModifier(ItemStack stack, String identifier, String name, UUID uuid, double amount, AttributeModifier.Operation operation, String slot) {
        apiError();
    }

    public static void addModifier(ItemStack stack, Attribute attribute, String name, UUID uuid, double amount, AttributeModifier.Operation operation, String slot) {
        apiError();
    }

    public static void registerCurioPredicate(ResourceLocation resourceLocation, Predicate<SlotResult> predicate) {
        apiError();
    }

    public static Optional<Predicate<SlotResult>> getCurioPredicate(ResourceLocation resourceLocation) {
        apiError();
        return Optional.empty();
    }

    public static Map<ResourceLocation, Predicate<SlotResult>> getCurioPredicates() {
        apiError();
        return Map.of();
    }

    public static boolean testCurioPredicates(Set<ResourceLocation> predicates, SlotResult slotResult) {
        apiError();
        return true;
    }

    public static UUID getSlotUuid(SlotContext slotContext) {
        apiError();
        return UUID.randomUUID();
    }

    public static void broadcastCurioBreakEvent(SlotContext slotContext) {
        apiError();
    }

    static void apiError() {
        LOGGER.error("Missing Curios API implementation!");
    }

    @Deprecated(since = "1.20.1")
    @ScheduledForRemoval(inVersion = "1.22")
    public static Optional<ISlotType> getSlot(String id) {
        return getSlot(id, false);
    }

    /**
     * @deprecated
     */
    @Nonnull
    public static ResourceLocation getSlotIcon(String id) {
        return (ResourceLocation) getSlot(id, true).map(ISlotType::getIcon).orElse(new ResourceLocation("curios", "slot/empty_curio_slot"));
    }

    @Deprecated(since = "1.20.1")
    @ScheduledForRemoval(inVersion = "1.22")
    public static Map<String, ISlotType> getSlots() {
        return getSlots(false);
    }

    @Deprecated(since = "1.20.1")
    @ScheduledForRemoval(inVersion = "1.22")
    public static Map<String, ISlotType> getEntitySlots(EntityType<?> type) {
        return getEntitySlots(type, false);
    }

    @Deprecated(since = "1.20.1")
    @ScheduledForRemoval(inVersion = "1.22")
    public static Map<String, ISlotType> getPlayerSlots() {
        return getPlayerSlots(false);
    }

    @Deprecated(since = "1.20.1")
    @ScheduledForRemoval(inVersion = "1.22")
    public static Map<String, ISlotType> getItemStackSlots(ItemStack stack) {
        return getItemStackSlots(stack, FMLLoader.getDist() == Dist.CLIENT);
    }

    @Deprecated(since = "1.20.1")
    @ScheduledForRemoval(inVersion = "1.22")
    public static void setIconHelper(IIconHelper helper) {
        if (iconHelper == null) {
            iconHelper = helper;
        }
    }

    @Deprecated(since = "1.20.1")
    @ScheduledForRemoval(inVersion = "1.22")
    public static IIconHelper getIconHelper() {
        return iconHelper;
    }

    @Deprecated(since = "1.20.1")
    @ScheduledForRemoval(inVersion = "1.22")
    public static void setCuriosHelper(ICuriosHelper helper) {
        if (curiosHelper == null) {
            curiosHelper = helper;
        }
    }

    @Deprecated(since = "1.20.1")
    @ScheduledForRemoval(inVersion = "1.22")
    public static ICuriosHelper getCuriosHelper() {
        return curiosHelper;
    }

    @Deprecated(forRemoval = true, since = "1.20.1")
    @ScheduledForRemoval(inVersion = "1.22")
    public static ISlotHelper getSlotHelper() {
        return slotHelper;
    }

    @Deprecated(forRemoval = true, since = "1.20.1")
    @ScheduledForRemoval(inVersion = "1.22")
    public static void setSlotHelper(ISlotHelper helper) {
        slotHelper = helper;
    }
}