package fr.frinn.custommachinery.common.component;

import fr.frinn.custommachinery.api.component.ComponentIOMode;
import fr.frinn.custommachinery.api.component.IMachineComponentManager;
import fr.frinn.custommachinery.api.component.ISerializableComponent;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.network.DataType;
import fr.frinn.custommachinery.api.network.ISyncable;
import fr.frinn.custommachinery.api.network.ISyncableStuff;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.impl.component.AbstractMachineComponent;
import java.util.function.Consumer;
import net.minecraft.nbt.CompoundTag;

public class DataMachineComponent extends AbstractMachineComponent implements ISerializableComponent, ISyncableStuff {

    private CompoundTag nbt = new CompoundTag();

    public DataMachineComponent(IMachineComponentManager manager) {
        super(manager, ComponentIOMode.NONE);
    }

    public CompoundTag getData() {
        return this.nbt;
    }

    public void setData(CompoundTag nbt) {
        this.nbt = nbt;
    }

    @Override
    public MachineComponentType<DataMachineComponent> getType() {
        return (MachineComponentType<DataMachineComponent>) Registration.DATA_MACHINE_COMPONENT.get();
    }

    @Override
    public void serialize(CompoundTag nbt) {
        nbt.put("data_component", this.nbt);
    }

    @Override
    public void deserialize(CompoundTag nbt) {
        if (nbt.contains("data_component", 10)) {
            this.nbt = nbt.getCompound("data_component");
        }
    }

    @Override
    public void getStuffToSync(Consumer<ISyncable<?, ?>> container) {
        container.accept(DataType.createSyncable(CompoundTag.class, this::getData, this::setData));
    }
}