package net.mehvahdjukaar.moonlight.api.fluids;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.mehvahdjukaar.moonlight.api.fluids.forge.SoftFluidStackImpl;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.util.PotionNBTHelper;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.moonlight.core.fluid.SoftFluidInternal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public class SoftFluidStack {

    public static final Codec<SoftFluidStack> CODEC = RecordCodecBuilder.create(i -> i.group(SoftFluid.HOLDER_CODEC.fieldOf("id").forGetter(SoftFluidStack::getHolder), Codec.INT.optionalFieldOf("count", 1).forGetter(SoftFluidStack::getCount), CompoundTag.CODEC.optionalFieldOf("tag", null).forGetter(SoftFluidStack::getTag)).apply(i, SoftFluidStack::of));

    private static SoftFluidStack cachedEmptyInstance = null;

    private final Holder<SoftFluid> fluidHolder;

    private final SoftFluid fluid;

    private int count;

    @Nullable
    private CompoundTag tag;

    private boolean isEmptyCache;

    @Deprecated(forRemoval = true)
    @Internal
    public SoftFluidStack(Holder<SoftFluid> fluid, int count, @Nullable CompoundTag tag) {
        this.fluidHolder = fluid;
        this.fluid = this.fluidHolder.value();
        this.tag = tag;
        this.setCount(count);
        if (fluid.is(BuiltInSoftFluids.POTION.getID()) && (this.tag == null || PotionNBTHelper.getPotionType(this.tag) == null)) {
            PotionNBTHelper.Type.REGULAR.applyToTag(this.getOrCreateTag());
        }
    }

    @Deprecated(forRemoval = true)
    public SoftFluidStack(Holder<SoftFluid> fluid, int count) {
        this(fluid, count, null);
    }

    @Deprecated(forRemoval = true)
    public SoftFluidStack(Holder<SoftFluid> fluid) {
        this(fluid, 1, null);
    }

    @ExpectPlatform
    @Transformed
    public static SoftFluidStack of(Holder<SoftFluid> fluid, int count, @Nullable CompoundTag tag) {
        return SoftFluidStackImpl.of(fluid, count, tag);
    }

    public static SoftFluidStack of(Holder<SoftFluid> fluid, int count) {
        return of(fluid, count, null);
    }

    public static SoftFluidStack of(Holder<SoftFluid> fluid) {
        return of(fluid, 1, null);
    }

    public static SoftFluidStack bucket(Holder<SoftFluid> fluid) {
        return of(fluid, SoftFluid.BUCKET_COUNT);
    }

    public static SoftFluidStack bowl(Holder<SoftFluid> fluid) {
        return of(fluid, SoftFluid.BOWL_COUNT);
    }

    public static SoftFluidStack bottle(Holder<SoftFluid> fluid) {
        return of(fluid, SoftFluid.BOTTLE_COUNT);
    }

    public static SoftFluidStack empty() {
        if (cachedEmptyInstance == null) {
            cachedEmptyInstance = of(SoftFluidRegistry.getEmpty(), 0, null);
        }
        return cachedEmptyInstance;
    }

    @Internal
    public static void invalidateEmptyInstance() {
        cachedEmptyInstance = null;
    }

    public CompoundTag save(CompoundTag compoundTag) {
        compoundTag.putString("id", ((ResourceKey) this.getHolder().unwrapKey().get()).location().toString());
        compoundTag.putByte("count", (byte) this.count);
        if (this.tag != null) {
            compoundTag.put("tag", this.tag.copy());
        }
        return compoundTag;
    }

    public static SoftFluidStack load(CompoundTag tag) {
        if (tag.contains("Fluid")) {
            tag.putString("id", tag.getString("Fluid"));
            tag.remove("Fluid");
        }
        if (tag.contains("NBT")) {
            tag.put("tag", tag.get("NBT"));
            tag.remove("NBT");
        }
        if (tag.contains("Count")) {
            tag.putByte("count", (byte) tag.getInt("Count"));
            tag.remove("count");
        }
        Holder<SoftFluid> fluid = SoftFluidRegistry.getHolder(new ResourceLocation(tag.getString("id")));
        byte amount = tag.getByte("count");
        CompoundTag nbt = null;
        if (tag.contains("tag", 10)) {
            nbt = tag.getCompound("tag");
        }
        return of(fluid, amount, nbt);
    }

    public boolean is(TagKey<SoftFluid> tag) {
        return this.getHolder().is(tag);
    }

    public boolean is(SoftFluid fluid) {
        return this.fluid() == fluid;
    }

    public boolean is(Holder<SoftFluid> fluid) {
        return this.is(fluid.value());
    }

    @Deprecated(forRemoval = true)
    public final Holder<SoftFluid> getFluid() {
        return this.isEmptyCache ? SoftFluidRegistry.getEmpty() : this.fluidHolder;
    }

    public final Holder<SoftFluid> getHolder() {
        return this.isEmptyCache ? SoftFluidRegistry.getEmpty() : this.fluidHolder;
    }

    public final SoftFluid fluid() {
        return this.isEmptyCache ? SoftFluidRegistry.empty() : this.fluid;
    }

    public boolean isEmpty() {
        return this.isEmptyCache;
    }

    protected void updateEmpty() {
        this.isEmptyCache = this.fluid.isEmptyFluid() || this.count <= 0;
    }

    public int getCount() {
        return this.isEmptyCache ? 0 : this.count;
    }

    public void setCount(int count) {
        if (this == cachedEmptyInstance) {
            if (PlatHelper.isDev()) {
                throw new AssertionError();
            }
        } else {
            this.count = count;
            this.updateEmpty();
        }
    }

    public void grow(int amount) {
        this.setCount(this.count + amount);
    }

    public void shrink(int amount) {
        this.setCount(this.count - amount);
    }

    public boolean hasTag() {
        return this.tag != null;
    }

    @Nullable
    public CompoundTag getTag() {
        return this.tag;
    }

    public void setTag(@Nullable CompoundTag tag) {
        if (this == cachedEmptyInstance) {
            if (PlatHelper.isDev()) {
                throw new AssertionError();
            }
        } else {
            this.tag = tag;
        }
    }

    public CompoundTag getOrCreateTag() {
        if (this.tag == null) {
            this.setTag(new CompoundTag());
        }
        return this.tag;
    }

    public CompoundTag getOrCreateTagElement(String key) {
        if (this.tag != null && this.tag.contains(key, 10)) {
            return this.tag.getCompound(key);
        } else {
            CompoundTag compoundTag = new CompoundTag();
            this.addTagElement(key, compoundTag);
            return compoundTag;
        }
    }

    public void addTagElement(String key, Tag tag) {
        this.getOrCreateTag().put(key, tag);
    }

    public SoftFluidStack copy() {
        return of(this.getHolder(), this.count, this.tag == null ? null : this.tag.copy());
    }

    public SoftFluidStack copyWithCount(int count) {
        SoftFluidStack stack = this.copy();
        if (!stack.isEmpty()) {
            stack.setCount(count);
        }
        return stack;
    }

    public SoftFluidStack split(int amount) {
        int i = Math.min(amount, this.getCount());
        SoftFluidStack stack = this.copyWithCount(i);
        if (!this.isEmpty()) {
            this.shrink(i);
        }
        return stack;
    }

    public boolean isFluidEqual(SoftFluidStack other) {
        return this.fluid() == other.fluid() && this.isFluidStackTagEqual(other);
    }

    public boolean isFluidStackTagEqual(SoftFluidStack other) {
        return Objects.equals(this.tag, other.tag);
    }

    public final int hashCode() {
        int code = 1;
        code = 31 * code + this.fluid().hashCode();
        if (this.tag != null) {
            code = 31 * code + this.tag.hashCode();
        }
        return code;
    }

    public final boolean equals(Object o) {
        return o instanceof SoftFluidStack ss ? this.isFluidEqual(ss) : false;
    }

    public String toString() {
        String s = this.count + " " + ((ResourceKey) this.getHolder().unwrapKey().get()).location();
        if (this.tag != null) {
            s = s + " [" + this.tag + "]";
        }
        return s;
    }

    @NotNull
    public static SoftFluidStack fromFluid(Fluid fluid, int amount, @Nullable CompoundTag tag) {
        Holder<SoftFluid> f = (Holder<SoftFluid>) SoftFluidInternal.FLUID_MAP.get(fluid);
        return f == null ? empty() : of(f, amount, tag);
    }

    @NotNull
    public static SoftFluidStack fromFluid(FluidState fluid) {
        return fluid.is(FluidTags.WATER) ? fromFluid(fluid.getType(), 3, null) : fromFluid(fluid.getType(), SoftFluid.BUCKET_COUNT, null);
    }

    @Nullable
    public static Pair<SoftFluidStack, FluidContainerList.Category> fromItem(ItemStack itemStack) {
        Item filledContainer = itemStack.getItem();
        Holder<SoftFluid> fluid = (Holder<SoftFluid>) SoftFluidInternal.ITEM_MAP.get(filledContainer);
        if (fluid != null && !fluid.value().isEmptyFluid()) {
            Optional<FluidContainerList.Category> category = fluid.value().getContainerList().getCategoryFromFilled(filledContainer);
            if (category.isPresent()) {
                int count = ((FluidContainerList.Category) category.get()).getAmount();
                CompoundTag fluidTag = new CompoundTag();
                CompoundTag itemTag = itemStack.getTag();
                Potion potion = PotionUtils.getPotion(itemStack);
                boolean hasCustomPot = itemTag != null && itemTag.contains("CustomPotionEffects");
                if (potion == Potions.WATER && !hasCustomPot) {
                    fluid = BuiltInSoftFluids.WATER.getHolder();
                } else if (potion != Potions.EMPTY || hasCustomPot) {
                    PotionNBTHelper.Type type = PotionNBTHelper.getPotionType(filledContainer);
                    if (type == null) {
                        type = PotionNBTHelper.Type.REGULAR;
                    }
                    type.applyToTag(fluidTag);
                }
                if (itemTag != null) {
                    for (String k : fluid.value().getNbtKeyFromItem()) {
                        Tag c = itemTag.get(k);
                        if (c != null) {
                            fluidTag.put(k, c);
                        }
                    }
                }
                if (fluidTag.isEmpty()) {
                    fluidTag = null;
                }
                return Pair.of(of(fluid, count, fluidTag), (FluidContainerList.Category) category.get());
            }
        }
        return null;
    }

    @Nullable
    public Pair<ItemStack, FluidContainerList.Category> toItem(ItemStack emptyContainer, boolean dontModifyStack) {
        Optional<FluidContainerList.Category> opt = this.fluid().getContainerList().getCategoryFromEmpty(emptyContainer.getItem());
        if (opt.isPresent()) {
            FluidContainerList.Category category = (FluidContainerList.Category) opt.get();
            int shrinkAmount = category.getAmount();
            if (shrinkAmount <= this.getCount()) {
                ItemStack filledStack = new ItemStack((ItemLike) category.getFirstFilled().get());
                if (this.is(BuiltInSoftFluids.POTION.getHolder()) && this.tag != null) {
                    PotionNBTHelper.Type type = PotionNBTHelper.getPotionType(this.tag);
                    if (type != null && !Utils.getID(emptyContainer.getItem()).getNamespace().equals("inspirations") && type != PotionNBTHelper.Type.REGULAR) {
                        filledStack = type.getDefaultItem();
                    }
                }
                if (emptyContainer.is(Items.GLASS_BOTTLE) && this.is(BuiltInSoftFluids.WATER.get())) {
                    filledStack = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER);
                }
                this.applyNBTtoItemStack(filledStack);
                if (!dontModifyStack) {
                    this.shrink(shrinkAmount);
                }
                return Pair.of(filledStack, category);
            }
        }
        return null;
    }

    protected void applyNBTtoItemStack(ItemStack stack) {
        List<String> nbtKey = this.fluid().getNbtKeyFromItem();
        if (this.tag != null && !this.tag.isEmpty()) {
            CompoundTag newCom = new CompoundTag();
            for (String s : nbtKey) {
                Tag c = this.tag.get(s);
                if (c != null && !s.equals("Bottle")) {
                    newCom.put(s, c);
                }
            }
            if (!newCom.isEmpty()) {
                stack.setTag(newCom);
            }
        }
    }

    public FluidContainerList getContainerList() {
        return this.fluid().getContainerList();
    }

    public FoodProvider getFoodProvider() {
        return this.fluid().getFoodProvider();
    }

    public boolean isEquivalent(Fluid fluid) {
        return this.fluid().isEquivalent(fluid);
    }

    public Fluid getVanillaFluid() {
        return this.fluid().getVanillaFluid();
    }

    public int getStillColor(@Nullable BlockAndTintGetter world, @Nullable BlockPos pos) {
        SoftFluid fluid = this.fluid();
        SoftFluid.TintMethod method = fluid.getTintMethod();
        if (method == SoftFluid.TintMethod.NO_TINT) {
            return -1;
        } else {
            int specialColor = SoftFluidColors.getSpecialColor(this, world, pos);
            return specialColor != 0 ? specialColor : fluid.getTintColor();
        }
    }

    public int getFlowingColor(@Nullable BlockAndTintGetter world, @Nullable BlockPos pos) {
        SoftFluid.TintMethod method = this.fluid().getTintMethod();
        return method == SoftFluid.TintMethod.FLOWING ? this.getParticleColor(world, pos) : this.getStillColor(world, pos);
    }

    public int getParticleColor(@Nullable BlockAndTintGetter world, @Nullable BlockPos pos) {
        int tintColor = this.getStillColor(world, pos);
        return tintColor == -1 ? this.fluid().getAverageTextureTintColor() : tintColor;
    }
}