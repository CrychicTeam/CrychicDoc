package fr.frinn.custommachinery.common.network.syncable;

import fr.frinn.custommachinery.common.network.data.SideConfigData;
import fr.frinn.custommachinery.impl.component.config.RelativeSide;
import fr.frinn.custommachinery.impl.component.config.SideConfig;
import fr.frinn.custommachinery.impl.network.AbstractSyncable;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class SideConfigSyncable extends AbstractSyncable<SideConfigData, SideConfig> {

    public SideConfigData getData(short id) {
        return new SideConfigData(id, this.get());
    }

    @Override
    public boolean needSync() {
        SideConfig value = this.get();
        if (this.lastKnownValue == null) {
            this.lastKnownValue = value.copy();
            return true;
        } else {
            for (RelativeSide side : RelativeSide.values()) {
                if (this.lastKnownValue.getSideMode(side) != value.getSideMode(side)) {
                    this.lastKnownValue = value.copy();
                    return true;
                }
            }
            if (this.lastKnownValue.isAutoInput() == value.isAutoInput() && this.lastKnownValue.isAutoOutput() == value.isAutoOutput()) {
                return false;
            } else {
                this.lastKnownValue = value.copy();
                return true;
            }
        }
    }

    public static SideConfigSyncable create(Supplier<SideConfig> supplier, Consumer<SideConfig> consumer) {
        return new SideConfigSyncable() {

            public SideConfig get() {
                return (SideConfig) supplier.get();
            }

            public void set(SideConfig value) {
                consumer.accept(value);
            }
        };
    }
}