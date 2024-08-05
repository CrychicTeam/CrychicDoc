package net.minecraftforge.fluids;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class FluidStack {

    private static final Logger LOGGER = LogManager.getLogger();

    public static final FluidStack EMPTY = new FluidStack(Fluids.EMPTY, 0);

    public static final Codec<FluidStack> CODEC = RecordCodecBuilder.create(instance -> instance.group(BuiltInRegistries.FLUID.m_194605_().fieldOf("FluidName").forGetter(FluidStack::getFluid), Codec.INT.fieldOf("Amount").forGetter(FluidStack::getAmount), CompoundTag.CODEC.optionalFieldOf("Tag").forGetter(stack -> Optional.ofNullable(stack.getTag()))).apply(instance, (fluid, amount, tag) -> {
        FluidStack stack = new FluidStack(fluid, amount);
        tag.ifPresent(stack::setTag);
        return stack;
    }));

    private boolean isEmpty;

    private int amount;

    private CompoundTag tag;

    private Holder.Reference<Fluid> fluidDelegate;

    public FluidStack(Fluid fluid, int amount) {
        if (fluid == null) {
            LOGGER.fatal("Null fluid supplied to fluidstack. Did you try and create a stack for an unregistered fluid?");
            throw new IllegalArgumentException("Cannot create a fluidstack from a null fluid");
        } else if (ForgeRegistries.FLUIDS.getKey(fluid) == null) {
            LOGGER.fatal("Failed attempt to create a FluidStack for an unregistered Fluid {} (type {})", ForgeRegistries.FLUIDS.getKey(fluid), fluid.getClass().getName());
            throw new IllegalArgumentException("Cannot create a fluidstack from an unregistered fluid");
        } else {
            this.fluidDelegate = ForgeRegistries.FLUIDS.getDelegateOrThrow(fluid);
            this.amount = amount;
            this.updateEmpty();
        }
    }

    public FluidStack(Fluid fluid, int amount, CompoundTag nbt) {
        this(fluid, amount);
        if (nbt != null) {
            this.tag = nbt.copy();
        }
    }

    public FluidStack(FluidStack stack, int amount) {
        this(stack.getFluid(), amount, stack.tag);
    }

    public static FluidStack loadFluidStackFromNBT(CompoundTag nbt) {
        if (nbt == null) {
            return EMPTY;
        } else if (!nbt.contains("FluidName", 8)) {
            return EMPTY;
        } else {
            ResourceLocation fluidName = new ResourceLocation(nbt.getString("FluidName"));
            Fluid fluid = ForgeRegistries.FLUIDS.getValue(fluidName);
            if (fluid == null) {
                return EMPTY;
            } else {
                FluidStack stack = new FluidStack(fluid, nbt.getInt("Amount"));
                if (nbt.contains("Tag", 10)) {
                    stack.tag = nbt.getCompound("Tag");
                }
                return stack;
            }
        }
    }

    public CompoundTag writeToNBT(CompoundTag nbt) {
        nbt.putString("FluidName", ForgeRegistries.FLUIDS.getKey(this.getFluid()).toString());
        nbt.putInt("Amount", this.amount);
        if (this.tag != null) {
            nbt.put("Tag", this.tag);
        }
        return nbt;
    }

    public void writeToPacket(FriendlyByteBuf buf) {
        buf.writeRegistryId(ForgeRegistries.FLUIDS, this.getFluid());
        buf.writeVarInt(this.getAmount());
        buf.writeNbt(this.tag);
    }

    public static FluidStack readFromPacket(FriendlyByteBuf buf) {
        Fluid fluid = (Fluid) buf.readRegistryId();
        int amount = buf.readVarInt();
        CompoundTag tag = buf.readNbt();
        return fluid == Fluids.EMPTY ? EMPTY : new FluidStack(fluid, amount, tag);
    }

    public final Fluid getFluid() {
        return this.isEmpty ? Fluids.EMPTY : (Fluid) this.fluidDelegate.get();
    }

    public final Fluid getRawFluid() {
        return (Fluid) this.fluidDelegate.get();
    }

    public boolean isEmpty() {
        return this.isEmpty;
    }

    protected void updateEmpty() {
        this.isEmpty = this.getRawFluid() == Fluids.EMPTY || this.amount <= 0;
    }

    public int getAmount() {
        return this.isEmpty ? 0 : this.amount;
    }

    public void setAmount(int amount) {
        if (this.getRawFluid() == Fluids.EMPTY) {
            throw new IllegalStateException("Can't modify the empty stack.");
        } else {
            this.amount = amount;
            this.updateEmpty();
        }
    }

    public void grow(int amount) {
        this.setAmount(this.amount + amount);
    }

    public void shrink(int amount) {
        this.setAmount(this.amount - amount);
    }

    public boolean hasTag() {
        return this.tag != null;
    }

    public CompoundTag getTag() {
        return this.tag;
    }

    public void setTag(CompoundTag tag) {
        if (this.getRawFluid() == Fluids.EMPTY) {
            throw new IllegalStateException("Can't modify the empty stack.");
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

    public CompoundTag getChildTag(String childName) {
        return this.tag == null ? null : this.tag.getCompound(childName);
    }

    public CompoundTag getOrCreateChildTag(String childName) {
        this.getOrCreateTag();
        CompoundTag child = this.tag.getCompound(childName);
        if (!this.tag.contains(childName, 10)) {
            this.tag.put(childName, child);
        }
        return child;
    }

    public void removeChildTag(String childName) {
        if (this.tag != null) {
            this.tag.remove(childName);
        }
    }

    public Component getDisplayName() {
        return this.getFluid().getFluidType().getDescription(this);
    }

    public String getTranslationKey() {
        return this.getFluid().getFluidType().getDescriptionId(this);
    }

    public FluidStack copy() {
        return new FluidStack(this.getFluid(), this.amount, this.tag);
    }

    public boolean isFluidEqual(@NotNull FluidStack other) {
        return this.getFluid() == other.getFluid() && this.isFluidStackTagEqual(other);
    }

    private boolean isFluidStackTagEqual(FluidStack other) {
        return this.tag == null ? other.tag == null : other.tag != null && this.tag.equals(other.tag);
    }

    public static boolean areFluidStackTagsEqual(@NotNull FluidStack stack1, @NotNull FluidStack stack2) {
        return stack1.isFluidStackTagEqual(stack2);
    }

    public boolean containsFluid(@NotNull FluidStack other) {
        return this.isFluidEqual(other) && this.amount >= other.amount;
    }

    public boolean isFluidStackIdentical(FluidStack other) {
        return this.isFluidEqual(other) && this.amount == other.amount;
    }

    public boolean isFluidEqual(@NotNull ItemStack other) {
        return (Boolean) FluidUtil.getFluidContained(other).map(this::isFluidEqual).orElse(false);
    }

    public final int hashCode() {
        int code = 1;
        code = 31 * code + this.getFluid().hashCode();
        if (this.tag != null) {
            code = 31 * code + this.tag.hashCode();
        }
        return code;
    }

    public final boolean equals(Object o) {
        return !(o instanceof FluidStack) ? false : this.isFluidEqual((FluidStack) o);
    }
}