package com.simibubi.create.foundation.blockEntity.behaviour.inventory;

import com.google.common.base.Predicates;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.item.ItemHelper;
import java.util.function.Predicate;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class InvManipulationBehaviour extends CapManipulationBehaviourBase<IItemHandler, InvManipulationBehaviour> {

    public static final BehaviourType<InvManipulationBehaviour> TYPE = new BehaviourType<>();

    public static final BehaviourType<InvManipulationBehaviour> EXTRACT = new BehaviourType<>();

    public static final BehaviourType<InvManipulationBehaviour> INSERT = new BehaviourType<>();

    private BehaviourType<InvManipulationBehaviour> behaviourType;

    public static InvManipulationBehaviour forExtraction(SmartBlockEntity be, CapManipulationBehaviourBase.InterfaceProvider target) {
        return new InvManipulationBehaviour(EXTRACT, be, target);
    }

    public static InvManipulationBehaviour forInsertion(SmartBlockEntity be, CapManipulationBehaviourBase.InterfaceProvider target) {
        return new InvManipulationBehaviour(INSERT, be, target);
    }

    public InvManipulationBehaviour(SmartBlockEntity be, CapManipulationBehaviourBase.InterfaceProvider target) {
        this(TYPE, be, target);
    }

    private InvManipulationBehaviour(BehaviourType<InvManipulationBehaviour> type, SmartBlockEntity be, CapManipulationBehaviourBase.InterfaceProvider target) {
        super(be, target);
        this.behaviourType = type;
    }

    @Override
    protected Capability<IItemHandler> capability() {
        return ForgeCapabilities.ITEM_HANDLER;
    }

    public ItemStack extract() {
        return this.extract(this.getModeFromFilter(), this.getAmountFromFilter());
    }

    public ItemStack extract(ItemHelper.ExtractionCountMode mode, int amount) {
        return this.extract(mode, amount, Predicates.alwaysTrue());
    }

    public ItemStack extract(ItemHelper.ExtractionCountMode mode, int amount, Predicate<ItemStack> filter) {
        boolean shouldSimulate = this.simulateNext;
        this.simulateNext = false;
        if (this.getWorld().isClientSide) {
            return ItemStack.EMPTY;
        } else {
            IItemHandler inventory = this.targetCapability.orElse(null);
            if (inventory == null) {
                return ItemStack.EMPTY;
            } else {
                Predicate<ItemStack> test = this.getFilterTest(filter);
                ItemStack simulatedItems = ItemHelper.extract(inventory, test, mode, amount, true);
                return !shouldSimulate && !simulatedItems.isEmpty() ? ItemHelper.extract(inventory, test, mode, amount, false) : simulatedItems;
            }
        }
    }

    public ItemStack insert(ItemStack stack) {
        boolean shouldSimulate = this.simulateNext;
        this.simulateNext = false;
        IItemHandler inventory = this.targetCapability.orElse(null);
        return inventory == null ? stack : ItemHandlerHelper.insertItemStacked(inventory, stack, shouldSimulate);
    }

    protected Predicate<ItemStack> getFilterTest(Predicate<ItemStack> customFilter) {
        Predicate<ItemStack> test = customFilter;
        FilteringBehaviour filter = this.blockEntity.getBehaviour(FilteringBehaviour.TYPE);
        if (filter != null) {
            test = customFilter.and(filter::test);
        }
        return test;
    }

    @Override
    public BehaviourType<?> getType() {
        return this.behaviourType;
    }
}