package top.theillusivec4.curios.api.type.util;

import com.google.common.collect.Multimap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

@Deprecated(since = "1.20.1", forRemoval = true)
@ScheduledForRemoval(inVersion = "1.22")
public interface ICuriosHelper {

    @Deprecated(since = "1.20.1", forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.22")
    LazyOptional<ICurio> getCurio(ItemStack var1);

    @Deprecated(since = "1.20.1", forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.22")
    LazyOptional<ICuriosItemHandler> getCuriosHandler(LivingEntity var1);

    @Deprecated(since = "1.20.1", forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.22")
    Set<String> getCurioTags(Item var1);

    @Deprecated(since = "1.20.1", forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.22")
    LazyOptional<IItemHandlerModifiable> getEquippedCurios(LivingEntity var1);

    @Deprecated(since = "1.20.1", forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.22")
    void setEquippedCurio(@Nonnull LivingEntity var1, String var2, int var3, ItemStack var4);

    @Deprecated(since = "1.20.1", forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.22")
    Optional<SlotResult> findFirstCurio(@Nonnull LivingEntity var1, Item var2);

    @Deprecated(since = "1.20.1", forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.22")
    Optional<SlotResult> findFirstCurio(@Nonnull LivingEntity var1, Predicate<ItemStack> var2);

    @Deprecated(since = "1.20.1", forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.22")
    List<SlotResult> findCurios(@Nonnull LivingEntity var1, Item var2);

    @Deprecated(since = "1.20.1", forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.22")
    List<SlotResult> findCurios(@Nonnull LivingEntity var1, Predicate<ItemStack> var2);

    @Deprecated(since = "1.20.1", forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.22")
    List<SlotResult> findCurios(@Nonnull LivingEntity var1, String... var2);

    @Deprecated(since = "1.20.1", forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.22")
    Optional<SlotResult> findCurio(@Nonnull LivingEntity var1, String var2, int var3);

    @Deprecated(since = "1.20.1", forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.22")
    Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext var1, UUID var2, ItemStack var3);

    @Deprecated(since = "1.20.1", forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.22")
    void addSlotModifier(Multimap<Attribute, AttributeModifier> var1, String var2, UUID var3, double var4, AttributeModifier.Operation var6);

    @Deprecated(since = "1.20.1", forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.22")
    void addSlotModifier(ItemStack var1, String var2, String var3, UUID var4, double var5, AttributeModifier.Operation var7, String var8);

    @Deprecated(since = "1.20.1", forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.22")
    void addModifier(ItemStack var1, Attribute var2, String var3, UUID var4, double var5, AttributeModifier.Operation var7, String var8);

    @Deprecated(since = "1.20.1", forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.22")
    boolean isStackValid(SlotContext var1, ItemStack var2);

    @Nonnull
    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    Optional<ImmutableTriple<String, Integer, ItemStack>> findEquippedCurio(Item var1, @Nonnull LivingEntity var2);

    @Nonnull
    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    Optional<ImmutableTriple<String, Integer, ItemStack>> findEquippedCurio(Predicate<ItemStack> var1, @Nonnull LivingEntity var2);

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    void onBrokenCurio(String var1, int var2, LivingEntity var3);

    @Deprecated(since = "1.20.1", forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.22")
    void onBrokenCurio(SlotContext var1);

    @Deprecated(since = "1.20.1", forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.22")
    void setBrokenCurioConsumer(Consumer<SlotContext> var1);

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    void setBrokenCurioConsumer(TriConsumer<String, Integer, LivingEntity> var1);

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    Multimap<Attribute, AttributeModifier> getAttributeModifiers(String var1, ItemStack var2);
}