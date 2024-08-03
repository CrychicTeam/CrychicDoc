package fr.frinn.custommachinery.common.component;

import dev.architectury.fluid.FluidStack;
import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.ComponentIOMode;
import fr.frinn.custommachinery.api.component.IComparatorInputComponent;
import fr.frinn.custommachinery.api.component.IMachineComponentManager;
import fr.frinn.custommachinery.api.component.IMachineComponentTemplate;
import fr.frinn.custommachinery.api.component.ISerializableComponent;
import fr.frinn.custommachinery.api.component.ISideConfigComponent;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.network.ISyncable;
import fr.frinn.custommachinery.api.network.ISyncableStuff;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.common.network.syncable.FluidStackSyncable;
import fr.frinn.custommachinery.common.network.syncable.SideConfigSyncable;
import fr.frinn.custommachinery.common.util.Utils;
import fr.frinn.custommachinery.common.util.ingredient.IIngredient;
import fr.frinn.custommachinery.impl.component.AbstractMachineComponent;
import fr.frinn.custommachinery.impl.component.config.SideConfig;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

public class FluidMachineComponent extends AbstractMachineComponent implements ISerializableComponent, ISyncableStuff, IComparatorInputComponent, ISideConfigComponent {

    private final String id;

    private final long capacity;

    private final long maxInput;

    private final long maxOutput;

    private final List<IIngredient<Fluid>> filter;

    private final boolean whitelist;

    private final SideConfig config;

    private final boolean unique;

    private FluidStack fluidStack = FluidStack.empty();

    public FluidMachineComponent(IMachineComponentManager manager, ComponentIOMode mode, String id, long capacity, long maxInput, long maxOutput, List<IIngredient<Fluid>> filter, boolean whitelist, SideConfig.Template configTemplate, boolean unique) {
        super(manager, mode);
        this.id = id;
        this.capacity = capacity;
        this.maxInput = maxInput;
        this.maxOutput = maxOutput;
        this.filter = filter;
        this.whitelist = whitelist;
        this.config = configTemplate.build(this);
        this.unique = unique;
    }

    @Override
    public MachineComponentType<FluidMachineComponent> getType() {
        return (MachineComponentType<FluidMachineComponent>) Registration.FLUID_MACHINE_COMPONENT.get();
    }

    @Override
    public String getId() {
        return this.id;
    }

    public long getMaxInput() {
        return this.maxInput;
    }

    public long getMaxOutput() {
        return this.maxOutput;
    }

    @Override
    public SideConfig getConfig() {
        return this.config;
    }

    @Override
    public void serialize(CompoundTag nbt) {
        if (!this.fluidStack.isEmpty()) {
            nbt.put("stack", this.fluidStack.write(new CompoundTag()));
        }
        nbt.put("config", this.config.serialize());
    }

    @Override
    public void deserialize(CompoundTag nbt) {
        if (nbt.contains("stack", 10)) {
            this.fluidStack = FluidStack.read(nbt.getCompound("stack"));
        }
        if (nbt.contains("config")) {
            this.config.deserialize(nbt.get("config"));
        }
    }

    @Override
    public void getStuffToSync(Consumer<ISyncable<?, ?>> container) {
        container.accept(FluidStackSyncable.create(() -> this.fluidStack, fluidStack -> this.fluidStack = fluidStack));
        container.accept(SideConfigSyncable.create(this::getConfig, this.config::set));
    }

    @Override
    public int getComparatorInput() {
        return (int) (15.0 * ((double) this.fluidStack.getAmount() / (double) this.capacity));
    }

    public FluidStack getFluidStack() {
        return this.fluidStack.copy();
    }

    public void setFluidStack(FluidStack fluidStack) {
        this.fluidStack = fluidStack.copy();
        this.getManager().markDirty();
    }

    public long getCapacity() {
        return this.capacity;
    }

    public long getRemainingSpace() {
        return !this.fluidStack.isEmpty() ? Math.min(this.capacity - this.fluidStack.getAmount(), this.getMaxInput()) : Math.min(this.capacity, this.getMaxInput());
    }

    public boolean isFluidValid(@NotNull FluidStack stack) {
        if (this.unique && this.fluidStack.isEmpty() && this.getManager().getComponentHandler((MachineComponentType) Registration.FLUID_MACHINE_COMPONENT.get()).stream().flatMap(handler -> handler.getComponents().stream()).anyMatch(component -> component != this && component.getFluidStack().isFluidEqual(stack))) {
            return false;
        } else {
            return this.filter.stream().anyMatch(ingredient -> ingredient.test(stack.getFluid())) != this.whitelist ? false : this.fluidStack.isEmpty() || stack.isFluidEqual(this.fluidStack);
        }
    }

    public long insert(Fluid fluid, long amount, CompoundTag nbt, boolean simulate) {
        if (amount <= 0L) {
            return 0L;
        } else {
            if (this.fluidStack.isEmpty()) {
                amount = Math.min(amount, this.getMaxInput());
                if (!simulate) {
                    this.fluidStack = FluidStack.create(fluid, amount, nbt);
                    this.getManager().markDirty();
                }
            } else {
                amount = Math.min(Math.min(this.getRemainingSpace(), this.getMaxInput()), amount);
                if (!simulate) {
                    this.fluidStack.grow(amount);
                    this.getManager().markDirty();
                }
            }
            return amount;
        }
    }

    public FluidStack extract(long amount, boolean simulate) {
        if (amount > 0L && !this.fluidStack.isEmpty()) {
            amount = Utils.clamp(amount, 0L, Math.min(this.fluidStack.getAmount(), this.getMaxOutput()));
            FluidStack removed = FluidStack.create(this.fluidStack.getFluid(), amount, this.fluidStack.getTag());
            if (!simulate) {
                this.fluidStack.shrink(amount);
                this.getManager().markDirty();
            }
            return removed;
        } else {
            return FluidStack.empty();
        }
    }

    public long getRecipeRemainingSpace() {
        return !this.fluidStack.isEmpty() ? this.capacity - this.fluidStack.getAmount() : this.capacity;
    }

    public void recipeInsert(Fluid fluid, long amount, CompoundTag nbt) {
        if (amount > 0L) {
            if (this.fluidStack.isEmpty()) {
                this.fluidStack = FluidStack.create(fluid, amount, nbt);
            } else {
                amount = Utils.clamp(amount, 0L, this.getRecipeRemainingSpace());
                this.fluidStack.grow(amount);
            }
            this.getManager().markDirty();
        }
    }

    public void recipeExtract(long amount) {
        if (amount > 0L) {
            amount = Utils.clamp(amount, 0L, this.fluidStack.getAmount());
            this.fluidStack.shrink(amount);
            this.getManager().markDirty();
        }
    }

    public static record Template(String id, long capacity, long maxInput, long maxOutput, List<IIngredient<Fluid>> filter, boolean whitelist, ComponentIOMode mode, SideConfig.Template config, boolean unique) implements IMachineComponentTemplate<FluidMachineComponent> {

        public static final NamedCodec<FluidMachineComponent.Template> CODEC = NamedCodec.record(fluidMachineComponentTemplate -> fluidMachineComponentTemplate.group(NamedCodec.STRING.fieldOf("id").forGetter(template -> template.id), NamedCodec.LONG.fieldOf("capacity").forGetter(template -> template.capacity), NamedCodec.LONG.optionalFieldOf("maxInput").forGetter(template -> template.maxInput == template.capacity ? Optional.empty() : Optional.of(template.maxInput)), NamedCodec.LONG.optionalFieldOf("maxOutput").forGetter(template -> template.maxOutput == template.capacity ? Optional.empty() : Optional.of(template.maxOutput)), IIngredient.FLUID.listOf().optionalFieldOf("filter", Collections.emptyList()).forGetter(template -> template.filter), NamedCodec.BOOL.optionalFieldOf("whitelist", false).forGetter(template -> template.whitelist), ComponentIOMode.CODEC.optionalFieldOf("mode", ComponentIOMode.BOTH).forGetter(template -> template.mode), SideConfig.Template.CODEC.optionalFieldOf("config").forGetter(template -> template.config == template.mode.getBaseConfig() ? Optional.empty() : Optional.of(template.config)), NamedCodec.BOOL.optionalFieldOf("unique", false).forGetter(template -> template.unique)).apply(fluidMachineComponentTemplate, (id, capacity, maxInput, maxOutput, filter, whitelist, mode, config, unique) -> new FluidMachineComponent.Template(id, capacity, (Long) maxInput.orElse(capacity), (Long) maxOutput.orElse(capacity), filter, whitelist, mode, (SideConfig.Template) config.orElse(mode.getBaseConfig()), unique)), "Fluid machine component");

        @Override
        public MachineComponentType<FluidMachineComponent> getType() {
            return (MachineComponentType<FluidMachineComponent>) Registration.FLUID_MACHINE_COMPONENT.get();
        }

        @Override
        public String getId() {
            return this.id;
        }

        @Override
        public boolean canAccept(Object ingredient, boolean isInput, IMachineComponentManager manager) {
            if (this.mode != ComponentIOMode.BOTH && isInput != this.mode.isInput()) {
                return false;
            } else if (ingredient instanceof FluidStack stack) {
                return this.filter.stream().flatMap(f -> f.getAll().stream()).anyMatch(f -> f == stack.getFluid()) == this.whitelist;
            } else {
                return ingredient instanceof List<?> list ? list.stream().allMatch(object -> object instanceof FluidStack stackx ? this.filter.stream().flatMap(f -> f.getAll().stream()).anyMatch(f -> f == stackx.getFluid()) == this.whitelist : false) : false;
            }
        }

        public FluidMachineComponent build(IMachineComponentManager manager) {
            return new FluidMachineComponent(manager, this.mode, this.id, this.capacity, this.maxInput, this.maxOutput, this.filter, this.whitelist, this.config, this.unique);
        }
    }
}