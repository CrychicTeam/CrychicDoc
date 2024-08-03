package fr.frinn.custommachinery.common.component;

import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.ComponentIOMode;
import fr.frinn.custommachinery.api.component.IComparatorInputComponent;
import fr.frinn.custommachinery.api.component.IMachineComponentManager;
import fr.frinn.custommachinery.api.component.IMachineComponentTemplate;
import fr.frinn.custommachinery.api.component.ISerializableComponent;
import fr.frinn.custommachinery.api.component.ISideConfigComponent;
import fr.frinn.custommachinery.api.component.IVariableComponent;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.component.variant.IComponentVariant;
import fr.frinn.custommachinery.api.network.ISyncable;
import fr.frinn.custommachinery.api.network.ISyncableStuff;
import fr.frinn.custommachinery.common.component.variant.item.DefaultItemComponentVariant;
import fr.frinn.custommachinery.common.component.variant.item.FilterItemComponentVariant;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.common.network.syncable.ItemStackSyncable;
import fr.frinn.custommachinery.common.network.syncable.SideConfigSyncable;
import fr.frinn.custommachinery.common.util.Utils;
import fr.frinn.custommachinery.common.util.ingredient.IIngredient;
import fr.frinn.custommachinery.impl.component.AbstractMachineComponent;
import fr.frinn.custommachinery.impl.component.config.SideConfig;
import fr.frinn.custommachinery.impl.component.variant.ItemComponentVariant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

public class ItemMachineComponent extends AbstractMachineComponent implements ISerializableComponent, ISyncableStuff, IComparatorInputComponent, IVariableComponent<ItemComponentVariant>, ISideConfigComponent {

    private final String id;

    private final int capacity;

    private final int maxInput;

    private final int maxOutput;

    private final List<IIngredient<Item>> filter;

    private final boolean whitelist;

    private ItemStack stack = ItemStack.EMPTY;

    private final ItemComponentVariant variant;

    private final SideConfig config;

    private boolean locked;

    public ItemMachineComponent(IMachineComponentManager manager, ComponentIOMode mode, String id, int capacity, int maxInput, int maxOutput, List<IIngredient<Item>> filter, boolean whitelist, ItemComponentVariant variant, SideConfig.Template configTemplate, boolean locked) {
        super(manager, mode);
        this.id = id;
        this.capacity = capacity;
        this.maxInput = maxInput;
        this.maxOutput = maxOutput;
        this.filter = filter;
        this.whitelist = whitelist;
        this.variant = variant;
        if (this.variant == FilterItemComponentVariant.INSTANCE) {
            this.config = SideConfig.Template.DEFAULT_ALL_NONE_DISABLED.build(this);
        } else {
            this.config = configTemplate.build(this);
        }
        this.locked = locked;
    }

    @Override
    public MachineComponentType<ItemMachineComponent> getType() {
        return (MachineComponentType<ItemMachineComponent>) Registration.ITEM_MACHINE_COMPONENT.get();
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public SideConfig getConfig() {
        return this.config;
    }

    public ItemComponentVariant getVariant() {
        return this.variant;
    }

    public boolean isItemValid(ItemStack stack) {
        return this.filter.stream().anyMatch(ingredient -> ingredient.test(stack.getItem())) == this.whitelist && this.variant.canAccept(this.getManager(), stack);
    }

    public int getRemainingSpace() {
        return !this.stack.isEmpty() ? this.capacity - this.stack.getCount() : this.capacity;
    }

    public ItemStack getItemStack() {
        return this.stack;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public int insert(Item item, int amount, @Nullable CompoundTag nbt, boolean simulate) {
        return this.insert(item, amount, nbt, simulate, false);
    }

    public int insert(Item item, int amount, @Nullable CompoundTag nbt, boolean simulate, boolean byPassLimit) {
        if (amount > 0 && item != Items.AIR && this.isItemValid(Utils.makeItemStack(item, amount, nbt))) {
            if (!byPassLimit) {
                amount = Math.min(amount, this.maxInput);
            }
            amount = Math.min(amount, Utils.makeItemStack(item, amount, nbt).getMaxStackSize());
            amount = Math.min(amount, this.stack.getMaxStackSize() - this.stack.getCount());
            amount = Math.min(amount, this.capacity - this.stack.getCount());
            if (this.stack.isEmpty()) {
                if (!simulate) {
                    this.stack = Utils.makeItemStack(item, amount, nbt);
                    this.getManager().markDirty();
                    this.getManager().getTile().getUpgradeManager().markDirty();
                }
                return amount;
            } else if (this.stack.getItem() == item && (this.stack.getTag() == null || this.stack.getTag().equals(nbt))) {
                amount = Math.min(this.getRemainingSpace(), amount);
                if (!simulate) {
                    this.stack.grow(amount);
                    this.getManager().markDirty();
                    this.getManager().getTile().getUpgradeManager().markDirty();
                }
                return amount;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public ItemStack extract(int amount, boolean simulate) {
        return this.extract(amount, simulate, false);
    }

    public ItemStack extract(int amount, boolean simulate, boolean byPassLimit) {
        if (amount > 0 && !this.stack.isEmpty() && this.variant.canOutput(this.getManager())) {
            if (!byPassLimit) {
                amount = Math.min(amount, this.maxOutput);
            }
            amount = Math.min(amount, this.stack.getCount());
            ItemStack removed = Utils.makeItemStack(this.stack.getItem(), amount, this.stack.getTag());
            if (!simulate) {
                this.stack.shrink(amount);
                this.getManager().markDirty();
                this.getManager().getTile().getUpgradeManager().markDirty();
            }
            return removed;
        } else {
            return ItemStack.EMPTY;
        }
    }

    public void setItemStack(ItemStack stack) {
        this.stack = stack;
        this.getManager().markDirty();
        this.getManager().getTile().getUpgradeManager().markDirty();
    }

    public boolean isLocked() {
        return this.locked || this.variant == FilterItemComponentVariant.INSTANCE;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    @Override
    public void serialize(CompoundTag nbt) {
        if (!this.stack.isEmpty()) {
            this.stack.save(nbt);
        }
        nbt.put("config", this.config.serialize());
    }

    @Override
    public void deserialize(CompoundTag nbt) {
        this.stack = ItemStack.of(nbt);
        if (nbt.contains("config")) {
            this.config.deserialize(nbt.get("config"));
        }
    }

    @Override
    public void getStuffToSync(Consumer<ISyncable<?, ?>> container) {
        container.accept(ItemStackSyncable.create(() -> this.stack, stack -> this.stack = stack));
        container.accept(SideConfigSyncable.create(this::getConfig, this.config::set));
    }

    @Override
    public int getComparatorInput() {
        return AbstractContainerMenu.getRedstoneSignalFromContainer(new SimpleContainer(this.stack));
    }

    public static record Template(String id, ComponentIOMode mode, int capacity, int maxInput, int maxOutput, List<IIngredient<Item>> filter, boolean whitelist, ItemComponentVariant variant, SideConfig.Template config, boolean locked) implements IMachineComponentTemplate<ItemMachineComponent> {

        public static final NamedCodec<ItemMachineComponent.Template> CODEC = NamedCodec.record(itemMachineComponentTemplate -> itemMachineComponentTemplate.group(NamedCodec.STRING.fieldOf("id").forGetter(template -> template.id), ComponentIOMode.CODEC.optionalFieldOf("mode", ComponentIOMode.BOTH).forGetter(template -> template.mode), NamedCodec.INT.optionalFieldOf("capacity", 64).forGetter(template -> template.capacity), NamedCodec.INT.optionalFieldOf("max_input").forGetter(template -> template.maxInput == template.capacity ? Optional.empty() : Optional.of(template.maxInput)), NamedCodec.INT.optionalFieldOf("max_output").forGetter(template -> template.maxOutput == template.capacity ? Optional.empty() : Optional.of(template.maxOutput)), IIngredient.ITEM.listOf().optionalFieldOf("filter", Collections.emptyList()).forGetter(template -> template.filter), NamedCodec.BOOL.optionalFieldOf("whitelist", false).forGetter(template -> template.whitelist), IComponentVariant.codec(Registration.ITEM_MACHINE_COMPONENT).orElse(DefaultItemComponentVariant.INSTANCE).forGetter(template -> template.variant), SideConfig.Template.CODEC.optionalFieldOf("config").forGetter(template -> template.config == template.mode.getBaseConfig() ? Optional.empty() : Optional.of(template.config)), NamedCodec.BOOL.optionalFieldOf("locked", false).aliases("lock").forGetter(template -> template.locked)).apply(itemMachineComponentTemplate, (id, mode, capacity, maxInput, maxOutput, filter, whitelist, variant, config, locked) -> new ItemMachineComponent.Template(id, mode, capacity, (Integer) maxInput.orElse(capacity), (Integer) maxOutput.orElse(capacity), filter, whitelist, (ItemComponentVariant) variant, (SideConfig.Template) config.orElse(mode.getBaseConfig()), locked)), "Item machine component");

        public ItemComponentVariant getVariant() {
            return this.variant;
        }

        @Override
        public MachineComponentType<ItemMachineComponent> getType() {
            return (MachineComponentType<ItemMachineComponent>) Registration.ITEM_MACHINE_COMPONENT.get();
        }

        @Override
        public String getId() {
            return this.id;
        }

        @Override
        public boolean canAccept(Object ingredient, boolean isInput, IMachineComponentManager manager) {
            if (this.mode != ComponentIOMode.BOTH && isInput != this.mode.isInput()) {
                return false;
            } else if (!(ingredient instanceof ItemStack stack)) {
                return ingredient instanceof List<?> list ? list.stream().allMatch(object -> !(object instanceof ItemStack stackx) ? false : this.filter.stream().flatMap(i -> i.getAll().stream()).anyMatch(i -> i == stackx.getItem()) == this.whitelist && this.variant.canAccept(manager, stackx)) : false;
            } else {
                return this.filter.stream().flatMap(i -> i.getAll().stream()).anyMatch(i -> i == stack.getItem()) == this.whitelist && this.variant.canAccept(manager, stack);
            }
        }

        public ItemMachineComponent build(IMachineComponentManager manager) {
            return new ItemMachineComponent(manager, this.mode, this.id, this.capacity, this.maxInput, this.maxOutput, this.filter, this.whitelist, this.variant, this.config, this.locked);
        }
    }
}