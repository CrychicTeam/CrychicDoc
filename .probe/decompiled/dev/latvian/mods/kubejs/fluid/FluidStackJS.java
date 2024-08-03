package dev.latvian.mods.kubejs.fluid;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.architectury.fluid.FluidStack;
import dev.latvian.mods.kubejs.item.ingredient.TagContext;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.util.MapJS;
import dev.latvian.mods.kubejs.util.Tags;
import dev.latvian.mods.kubejs.util.UtilsJS;
import dev.latvian.mods.kubejs.util.WrappedJS;
import dev.latvian.mods.rhino.mod.util.NBTUtils;
import dev.latvian.mods.rhino.util.HideFromJS;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

public abstract class FluidStackJS implements WrappedJS, InputFluid, OutputFluid {

    private double chance = Double.NaN;

    public static FluidStackJS of(@Nullable Object o) {
        if (o == null) {
            return EmptyFluidStackJS.INSTANCE;
        } else if (o instanceof FluidStackJS) {
            return (FluidStackJS) o;
        } else if (o instanceof FluidStack fluidStack) {
            return new BoundFluidStackJS(fluidStack);
        } else if (o instanceof Fluid fluid) {
            UnboundFluidStackJS f = new UnboundFluidStackJS(RegistryInfo.FLUID.getId(fluid));
            return (FluidStackJS) (f.kjs$isEmpty() ? EmptyFluidStackJS.INSTANCE : f);
        } else if (o instanceof JsonElement json) {
            return fromJson(json);
        } else if (!(o instanceof CharSequence) && !(o instanceof ResourceLocation)) {
            Map<?, ?> map = MapJS.of(o);
            if (map != null && map.containsKey("fluid")) {
                FluidStackJS stack = new UnboundFluidStackJS(new ResourceLocation(map.get("fluid").toString()));
                if (map.get("amount") instanceof Number num) {
                    stack.setAmount(num.longValue());
                }
                if (map.containsKey("nbt")) {
                    stack.setNbt(NBTUtils.toTagCompound(map.get("nbt")));
                }
                return stack;
            } else {
                return EmptyFluidStackJS.INSTANCE;
            }
        } else {
            String s = o.toString();
            if (!s.isEmpty() && !s.equals("-") && !s.equals("empty") && !s.equals("minecraft:empty")) {
                String[] s1 = s.split(" ", 2);
                return new UnboundFluidStackJS(new ResourceLocation(s1[0])).withAmount(UtilsJS.parseLong(s1.length == 2 ? s1[1] : "", FluidStack.bucketAmount()));
            } else {
                return EmptyFluidStackJS.INSTANCE;
            }
        }
    }

    public static FluidStackJS of(@Nullable Object o, long amount, @Nullable CompoundTag nbt) {
        FluidStackJS stack = of(o);
        stack.setAmount(amount);
        stack.setNbt(nbt);
        return stack;
    }

    public static FluidStackJS fromJson(JsonElement e) {
        if (!e.isJsonObject()) {
            return of(e.getAsString());
        } else {
            JsonObject json = e.getAsJsonObject();
            FluidStackJS fluid = of(json.get("fluid").getAsString());
            if (fluid.kjs$isEmpty()) {
                throw new RecipeExceptionJS(json + " is not a valid fluid!");
            } else {
                long amount = FluidStack.bucketAmount();
                CompoundTag nbt = null;
                if (json.has("amount")) {
                    amount = (long) json.get("amount").getAsInt();
                } else if (json.has("count")) {
                    amount = (long) json.get("count").getAsInt();
                }
                if (json.has("nbt")) {
                    if (json.get("nbt").isJsonObject()) {
                        nbt = NBTUtils.toTagCompound(json.get("nbt"));
                    } else {
                        nbt = NBTUtils.toTagCompound(json.get("nbt").getAsString());
                    }
                }
                return of(fluid, amount, nbt);
            }
        }
    }

    public abstract String getId();

    public Collection<ResourceLocation> getTags() {
        return (Collection<ResourceLocation>) Tags.byFluid(this.getFluid()).map(TagKey::f_203868_).collect(Collectors.toSet());
    }

    public boolean hasTag(ResourceLocation tag) {
        return ((TagContext) TagContext.INSTANCE.getValue()).contains(Tags.fluid(tag), this.getFluid());
    }

    public Fluid getFluid() {
        Fluid f = RegistryInfo.FLUID.getValue(new ResourceLocation(this.getId()));
        return f == null ? Fluids.EMPTY : f;
    }

    public abstract FluidStack getFluidStack();

    @Override
    public abstract long kjs$getAmount();

    @HideFromJS
    public final long getAmount() {
        return this.kjs$getAmount();
    }

    public abstract void setAmount(long var1);

    public final FluidStackJS withAmount(long amount) {
        if (amount <= 0L) {
            return EmptyFluidStackJS.INSTANCE;
        } else {
            FluidStackJS fs = this.copy();
            fs.setAmount(amount);
            return fs;
        }
    }

    @Nullable
    public abstract CompoundTag getNbt();

    public abstract void setNbt(@Nullable CompoundTag var1);

    public final FluidStackJS withNBT(@Nullable CompoundTag nbt) {
        FluidStackJS fs = this.copy();
        fs.setNbt(nbt);
        return fs;
    }

    public FluidStackJS copy() {
        return this.kjs$copy(this.kjs$getAmount());
    }

    public abstract FluidStackJS kjs$copy(long var1);

    public boolean hasChance() {
        return !Double.isNaN(this.chance);
    }

    public void removeChance() {
        this.setChance(Double.NaN);
    }

    public void setChance(double c) {
        this.chance = c;
    }

    public double getChance() {
        return this.chance;
    }

    public final FluidStackJS withChance(double c) {
        if ((!Double.isNaN(this.chance) || !Double.isNaN(c)) && this.chance != c) {
            FluidStackJS is = this.copy();
            is.setChance(c);
            return is;
        } else {
            return this;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.getFluid(), this.getNbt() });
    }

    public boolean equals(Object o) {
        if (o instanceof CharSequence) {
            return this.getId().equals(o.toString());
        } else {
            FluidStackJS f = of(o);
            return f.kjs$isEmpty() ? false : this.getFluid() == f.getFluid() && Objects.equals(this.getNbt(), f.getNbt());
        }
    }

    public boolean strongEquals(Object o) {
        FluidStackJS f = of(o);
        return f.kjs$isEmpty() ? false : this.kjs$getAmount() == f.kjs$getAmount() && this.getFluid() == f.getFluid() && Objects.equals(this.getNbt(), f.getNbt());
    }

    public String toString() {
        long amount = this.kjs$getAmount();
        CompoundTag nbt = this.getNbt();
        StringBuilder builder = new StringBuilder();
        builder.append("Fluid.of('");
        builder.append(this.getId());
        if (amount != FluidStack.bucketAmount()) {
            builder.append(", ");
            builder.append(amount);
        }
        if (nbt != null) {
            builder.append(", ");
            NBTUtils.quoteAndEscapeForJS(builder, nbt.toString());
        }
        builder.append("')");
        if (this.hasChance()) {
            builder.append(".withChance(");
            builder.append(this.getChance());
            builder.append(')');
        }
        return builder.toString();
    }

    public JsonObject toJson() {
        JsonObject o = new JsonObject();
        o.addProperty("fluid", this.getId());
        o.addProperty("amount", this.kjs$getAmount());
        if (this.getNbt() != null) {
            o.addProperty("nbt", this.getNbt().toString());
        }
        if (this.hasChance()) {
            o.addProperty("chance", this.getChance());
        }
        return o;
    }

    public CompoundTag toNBT() {
        CompoundTag tag = new CompoundTag();
        this.getFluidStack().write(tag);
        return tag;
    }

    @Override
    public boolean matches(FluidLike other) {
        if (other instanceof FluidStackJS fs && this.getFluid() == fs.getFluid() && Objects.equals(this.getNbt(), fs.getNbt())) {
            return true;
        }
        return false;
    }
}