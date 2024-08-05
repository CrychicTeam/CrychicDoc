package com.simibubi.create.foundation.blockEntity.behaviour.inventory;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraftforge.items.IItemHandler;

public class VersionedInventoryTrackerBehaviour extends BlockEntityBehaviour {

    public static final BehaviourType<VersionedInventoryTrackerBehaviour> TYPE = new BehaviourType<>();

    private int ignoredId;

    private int ignoredVersion;

    public VersionedInventoryTrackerBehaviour(SmartBlockEntity be) {
        super(be);
        this.reset();
    }

    public boolean stillWaiting(InvManipulationBehaviour behaviour) {
        return behaviour.hasInventory() && this.stillWaiting(behaviour.getInventory());
    }

    public boolean stillWaiting(IItemHandler handler) {
        return !(handler instanceof VersionedInventoryWrapper viw) ? false : viw.getId() == this.ignoredId && viw.getVersion() == this.ignoredVersion;
    }

    public void awaitNewVersion(InvManipulationBehaviour behaviour) {
        if (behaviour.hasInventory()) {
            this.awaitNewVersion(behaviour.getInventory());
        }
    }

    public void awaitNewVersion(IItemHandler handler) {
        if (handler instanceof VersionedInventoryWrapper viw) {
            this.ignoredId = viw.getId();
            this.ignoredVersion = viw.getVersion();
        }
    }

    public void reset() {
        this.ignoredVersion = -1;
        this.ignoredId = -1;
    }

    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }
}