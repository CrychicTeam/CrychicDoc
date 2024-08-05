package fr.frinn.custommachinery.common.component.handler;

import fr.frinn.custommachinery.PlatformHelper;
import fr.frinn.custommachinery.api.component.IDumpComponent;
import fr.frinn.custommachinery.api.component.IMachineComponentManager;
import fr.frinn.custommachinery.api.component.ISerializableComponent;
import fr.frinn.custommachinery.api.component.ITickableComponent;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.component.variant.ITickableComponentVariant;
import fr.frinn.custommachinery.api.network.ISyncable;
import fr.frinn.custommachinery.api.network.ISyncableStuff;
import fr.frinn.custommachinery.common.component.ItemMachineComponent;
import fr.frinn.custommachinery.common.component.variant.item.FilterItemComponentVariant;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.common.util.Utils;
import fr.frinn.custommachinery.common.util.transfer.ICommonItemHandler;
import fr.frinn.custommachinery.impl.component.AbstractComponentHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ItemComponentHandler extends AbstractComponentHandler<ItemMachineComponent> implements ISerializableComponent, ITickableComponent, ISyncableStuff, IDumpComponent {

    private final RandomSource rand = RandomSource.create();

    private final List<ItemMachineComponent> tickableVariants;

    private final ICommonItemHandler handler = PlatformHelper.createItemHandler(this);

    private final List<ItemMachineComponent> inputs = new ArrayList();

    private final List<ItemMachineComponent> outputs = new ArrayList();

    public ItemComponentHandler(IMachineComponentManager manager, List<ItemMachineComponent> components) {
        super(manager, components);
        components.forEach(component -> {
            component.getConfig().setCallback(this.handler::configChanged);
            if (component.getVariant() != FilterItemComponentVariant.INSTANCE) {
                if (component.getMode().isInput()) {
                    this.inputs.add(component);
                }
                if (component.getMode().isOutput()) {
                    this.outputs.add(component);
                }
            }
        });
        this.tickableVariants = components.stream().filter(component -> component.getVariant() instanceof ITickableComponentVariant).toList();
    }

    public ICommonItemHandler getCommonHandler() {
        return this.handler;
    }

    @Override
    public MachineComponentType<ItemMachineComponent> getType() {
        return (MachineComponentType<ItemMachineComponent>) Registration.ITEM_MACHINE_COMPONENT.get();
    }

    @Override
    public void onRemoved() {
        this.handler.invalidate();
    }

    @Override
    public Optional<ItemMachineComponent> getComponentForID(String id) {
        return this.getComponents().stream().filter(component -> component.getId().equals(id)).findFirst();
    }

    @Override
    public void serialize(CompoundTag nbt) {
        ListTag components = new ListTag();
        this.getComponents().forEach(component -> {
            CompoundTag componentNBT = new CompoundTag();
            component.serialize(componentNBT);
            componentNBT.putString("slotID", component.getId());
            components.add(componentNBT);
        });
        nbt.put("items", components);
    }

    @Override
    public void deserialize(CompoundTag nbt) {
        if (nbt.contains("items", 9)) {
            ListTag components = nbt.getList("items", 10);
            components.forEach(inbt -> {
                if (inbt instanceof CompoundTag componentNBT && componentNBT.contains("slotID", 8)) {
                    this.getComponents().stream().filter(component -> component.getId().equals(componentNBT.getString("slotID"))).findFirst().ifPresent(component -> component.deserialize(componentNBT));
                }
            });
        }
    }

    @Override
    public void serverTick() {
        this.handler.tick();
        this.tickableVariants.forEach(component -> ((ITickableComponentVariant) component.getVariant()).tick(component));
    }

    @Override
    public void getStuffToSync(Consumer<ISyncable<?, ?>> container) {
        this.getComponents().forEach(component -> component.getStuffToSync(container));
    }

    @Override
    public void dump(List<String> ids) {
        this.getComponents().stream().filter(component -> ids.contains(component.getId())).forEach(component -> component.setItemStack(ItemStack.EMPTY));
    }

    public int getItemAmount(String slot, Item item, @Nullable CompoundTag nbt) {
        Predicate<ItemMachineComponent> nbtPredicate = component -> nbt == null || nbt.isEmpty() || component.getItemStack().getTag() != null && Utils.testNBT(component.getItemStack().getTag(), nbt);
        Predicate<ItemMachineComponent> slotPredicate = component -> slot.isEmpty() || component.getId().equals(slot);
        return this.inputs.stream().filter(component -> component.getItemStack().getItem() == item && nbtPredicate.test(component) && slotPredicate.test(component)).mapToInt(component -> component.getItemStack().getCount()).sum();
    }

    public int getDurabilityAmount(String slot, Item item, @Nullable CompoundTag nbt) {
        Predicate<ItemMachineComponent> nbtPredicate = component -> nbt == null || nbt.isEmpty() || component.getItemStack().getTag() != null && Utils.testNBT(component.getItemStack().getTag(), nbt);
        Predicate<ItemMachineComponent> slotPredicate = component -> slot.isEmpty() || component.getId().equals(slot);
        return this.inputs.stream().filter(component -> component.getItemStack().getItem() == item && component.getItemStack().isDamageableItem() && nbtPredicate.test(component) && slotPredicate.test(component)).mapToInt(component -> component.getItemStack().getMaxDamage() - component.getItemStack().getDamageValue()).sum();
    }

    public int getSpaceForItem(String slot, Item item, @Nullable CompoundTag nbt) {
        int maxStackSize = Utils.makeItemStack(item, 1, nbt).getMaxStackSize();
        return this.outputs.stream().filter(component -> this.canPlaceOutput(component, slot, item, nbt)).mapToInt(component -> component.getItemStack().isEmpty() ? Math.min(component.getCapacity(), maxStackSize) : Math.min(component.getCapacity() - component.getItemStack().getCount(), maxStackSize - component.getItemStack().getCount())).sum();
    }

    private boolean canPlaceOutput(ItemMachineComponent component, @Nullable String slot, Item item, @Nullable CompoundTag nbt) {
        ItemStack stack = Utils.makeItemStack(item, 1, nbt);
        if (slot != null && !slot.isEmpty() && !component.getId().equals(slot)) {
            return false;
        } else if (!component.isItemValid(stack)) {
            return false;
        } else if (component.getItemStack().isEmpty()) {
            return true;
        } else if (component.getItemStack().getItem() != item) {
            return false;
        } else {
            return component.getItemStack().getCount() >= Math.min(stack.getMaxStackSize(), component.getCapacity()) ? false : ItemStack.isSameItemSameTags(component.getItemStack(), stack);
        }
    }

    public int getSpaceForDurability(String slot, Item item, @Nullable CompoundTag nbt) {
        Predicate<ItemMachineComponent> nbtPredicate = component -> nbt == null || nbt.isEmpty() || component.getItemStack().getTag() != null && Utils.testNBT(component.getItemStack().getTag(), nbt);
        Predicate<ItemMachineComponent> slotPredicate = component -> slot.isEmpty() || component.getId().equals(slot);
        return this.inputs.stream().filter(component -> component.getItemStack().getItem() == item && component.getItemStack().isDamageableItem() && nbtPredicate.test(component) && slotPredicate.test(component)).mapToInt(component -> component.getItemStack().getDamageValue()).sum();
    }

    public void removeFromInputs(String slot, Item item, int amount, @Nullable CompoundTag nbt) {
        AtomicInteger toRemove = new AtomicInteger(amount);
        Predicate<ItemMachineComponent> nbtPredicate = component -> nbt == null || nbt.isEmpty() || component.getItemStack().getTag() != null && Utils.testNBT(component.getItemStack().getTag(), nbt);
        Predicate<ItemMachineComponent> slotPredicate = component -> slot.isEmpty() || component.getId().equals(slot);
        this.inputs.stream().filter(component -> component.getItemStack().getItem() == item && nbtPredicate.test(component) && slotPredicate.test(component)).forEach(component -> {
            int maxExtract = Math.min(component.getItemStack().getCount(), toRemove.get());
            toRemove.addAndGet(-maxExtract);
            component.extract(maxExtract, false, true);
        });
        this.getManager().markDirty();
    }

    public void removeDurability(String slot, Item item, int amount, @Nullable CompoundTag nbt, boolean canBreak) {
        AtomicInteger toRemove = new AtomicInteger(amount);
        Predicate<ItemMachineComponent> nbtPredicate = component -> nbt == null || nbt.isEmpty() || component.getItemStack().getTag() != null && Utils.testNBT(component.getItemStack().getTag(), nbt);
        Predicate<ItemMachineComponent> slotPredicate = component -> slot.isEmpty() || component.getId().equals(slot);
        this.inputs.stream().filter(component -> component.getItemStack().getItem() == item && component.getItemStack().isDamageableItem() && nbtPredicate.test(component) && slotPredicate.test(component)).forEach(component -> {
            int maxRemove = Math.min(component.getItemStack().getMaxDamage() - component.getItemStack().getDamageValue(), toRemove.get());
            toRemove.addAndGet(-maxRemove);
            ItemStack stack = component.getItemStack();
            if (stack.hurt(maxRemove, this.rand, null) && canBreak) {
                stack.shrink(1);
                stack.setDamageValue(0);
            }
        });
        this.getManager().markDirty();
    }

    public void addToOutputs(String slot, Item item, int amount, @Nullable CompoundTag nbt) {
        AtomicInteger toAdd = new AtomicInteger(amount);
        this.outputs.stream().filter(component -> this.canPlaceOutput(component, slot, item, nbt)).forEach(component -> {
            int maxInsert = Math.min(component.insert(item, amount, nbt, true, true), toAdd.get());
            toAdd.addAndGet(-maxInsert);
            component.insert(item, maxInsert, nbt, false, true);
        });
        this.getManager().markDirty();
    }

    public void repairItem(String slot, Item item, int amount, @Nullable CompoundTag nbt) {
        AtomicInteger toRepair = new AtomicInteger(amount);
        Predicate<ItemMachineComponent> nbtPredicate = component -> nbt == null || nbt.isEmpty() || component.getItemStack().getTag() != null && Utils.testNBT(component.getItemStack().getTag(), nbt);
        Predicate<ItemMachineComponent> slotPredicate = component -> slot.isEmpty() || component.getId().equals(slot);
        this.inputs.stream().filter(component -> component.getItemStack().getItem() == item && component.getItemStack().isDamageableItem() && nbtPredicate.test(component) && slotPredicate.test(component)).forEach(component -> {
            int maxRepair = Math.min(component.getItemStack().getDamageValue(), toRepair.get());
            toRepair.addAndGet(-maxRepair);
            component.getItemStack().setDamageValue(component.getItemStack().getDamageValue() - maxRepair);
        });
        this.getManager().markDirty();
    }
}