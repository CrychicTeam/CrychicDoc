package dev.architectury.fluid;

import dev.architectury.fluid.forge.FluidStackImpl;
import dev.architectury.hooks.fluid.FluidStackHooks;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public final class FluidStack {

    private static final FluidStack.FluidStackAdapter<Object> ADAPTER = adapt(FluidStack::getValue, FluidStack::new);

    private static final FluidStack EMPTY = create(Fluids.EMPTY, 0L);

    private Object value;

    private FluidStack(Supplier<Fluid> fluid, long amount, CompoundTag tag) {
        this(ADAPTER.create(fluid, amount, tag));
    }

    private FluidStack(Object value) {
        this.value = Objects.requireNonNull(value);
    }

    private Object getValue() {
        return this.value;
    }

    @ExpectPlatform
    @Transformed
    private static FluidStack.FluidStackAdapter<Object> adapt(Function<FluidStack, Object> toValue, Function<Object, FluidStack> fromValue) {
        return FluidStackImpl.adapt(toValue, fromValue);
    }

    public static FluidStack empty() {
        return EMPTY;
    }

    public static FluidStack create(Fluid fluid, long amount, @Nullable CompoundTag tag) {
        return create(() -> fluid, amount, tag);
    }

    public static FluidStack create(Fluid fluid, long amount) {
        return create(fluid, amount, null);
    }

    public static FluidStack create(Supplier<Fluid> fluid, long amount, @Nullable CompoundTag tag) {
        return new FluidStack(fluid, amount, tag);
    }

    public static FluidStack create(Supplier<Fluid> fluid, long amount) {
        return create(fluid, amount, null);
    }

    public static FluidStack create(FluidStack stack, long amount) {
        return create(stack.getRawFluidSupplier(), amount, stack.getTag());
    }

    public static long bucketAmount() {
        return FluidStackHooks.bucketAmount();
    }

    public Fluid getFluid() {
        return this.isEmpty() ? Fluids.EMPTY : this.getRawFluid();
    }

    @Nullable
    public Fluid getRawFluid() {
        return ADAPTER.getFluid(this.value);
    }

    public Supplier<Fluid> getRawFluidSupplier() {
        return ADAPTER.getRawFluidSupplier(this.value);
    }

    public boolean isEmpty() {
        return this.getRawFluid() == Fluids.EMPTY || ADAPTER.getAmount(this.value) <= 0L;
    }

    public long getAmount() {
        return this.isEmpty() ? 0L : ADAPTER.getAmount(this.value);
    }

    public void setAmount(long amount) {
        ADAPTER.setAmount(this.value, amount);
    }

    public void grow(long amount) {
        this.setAmount(this.getAmount() + amount);
    }

    public void shrink(long amount) {
        this.setAmount(this.getAmount() - amount);
    }

    public boolean hasTag() {
        return this.getTag() != null;
    }

    @Nullable
    public CompoundTag getTag() {
        return ADAPTER.getTag(this.value);
    }

    public void setTag(@Nullable CompoundTag tag) {
        ADAPTER.setTag(this.value, tag);
    }

    public CompoundTag getOrCreateTag() {
        CompoundTag tag = this.getTag();
        if (tag == null) {
            tag = new CompoundTag();
            this.setTag(tag);
            return tag;
        } else {
            return tag;
        }
    }

    @Nullable
    public CompoundTag getChildTag(String childName) {
        CompoundTag tag = this.getTag();
        return tag == null ? null : tag.getCompound(childName);
    }

    public CompoundTag getOrCreateChildTag(String childName) {
        CompoundTag tag = this.getOrCreateTag();
        CompoundTag child = tag.getCompound(childName);
        if (!tag.contains(childName, 10)) {
            tag.put(childName, child);
        }
        return child;
    }

    public void removeChildTag(String childName) {
        CompoundTag tag = this.getTag();
        if (tag != null) {
            tag.remove(childName);
        }
    }

    public Component getName() {
        return FluidStackHooks.getName(this);
    }

    public String getTranslationKey() {
        return FluidStackHooks.getTranslationKey(this);
    }

    public FluidStack copy() {
        return new FluidStack(ADAPTER.copy(this.value));
    }

    public int hashCode() {
        return ADAPTER.hashCode(this.value);
    }

    public boolean equals(Object o) {
        return !(o instanceof FluidStack) ? false : this.isFluidStackEqual((FluidStack) o);
    }

    public boolean isFluidStackEqual(FluidStack other) {
        return this.getFluid() == other.getFluid() && this.getAmount() == other.getAmount() && this.isTagEqual(other);
    }

    public boolean isFluidEqual(FluidStack other) {
        return this.getFluid() == other.getFluid();
    }

    public boolean isTagEqual(FluidStack other) {
        CompoundTag tag = this.getTag();
        CompoundTag otherTag = other.getTag();
        return Objects.equals(tag, otherTag);
    }

    public static FluidStack read(FriendlyByteBuf buf) {
        return FluidStackHooks.read(buf);
    }

    public static FluidStack read(CompoundTag tag) {
        return FluidStackHooks.read(tag);
    }

    public void write(FriendlyByteBuf buf) {
        FluidStackHooks.write(this, buf);
    }

    public CompoundTag write(CompoundTag tag) {
        return FluidStackHooks.write(this, tag);
    }

    public FluidStack copyWithAmount(long amount) {
        return this.isEmpty() ? this : new FluidStack(this.getRawFluidSupplier(), amount, this.getTag());
    }

    @Internal
    public static void init() {
    }

    @Internal
    public interface FluidStackAdapter<T> {

        T create(Supplier<Fluid> var1, long var2, CompoundTag var4);

        Supplier<Fluid> getRawFluidSupplier(T var1);

        Fluid getFluid(T var1);

        long getAmount(T var1);

        void setAmount(T var1, long var2);

        CompoundTag getTag(T var1);

        void setTag(T var1, CompoundTag var2);

        T copy(T var1);

        int hashCode(T var1);
    }
}