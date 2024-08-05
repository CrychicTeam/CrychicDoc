package fr.frinn.custommachinery.api.component;

import net.minecraft.nbt.CompoundTag;

public interface ISerializableComponent extends IMachineComponent {

    void serialize(CompoundTag var1);

    void deserialize(CompoundTag var1);
}