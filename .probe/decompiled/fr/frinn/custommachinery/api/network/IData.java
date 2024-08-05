package fr.frinn.custommachinery.api.network;

import fr.frinn.custommachinery.api.ICustomMachineryAPI;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public interface IData<T> {

    DataType<?, T> getType();

    short getID();

    T getValue();

    default void writeData(FriendlyByteBuf buffer) {
        if (this.getType().getId() == null) {
            throw new IllegalStateException("Attempting to write invalid data to Custom Machine container syncing packet : " + this.getType().toString() + " is not registered !");
        } else {
            buffer.writeResourceLocation(this.getType().getId());
            buffer.writeShort(this.getID());
        }
    }

    static IData<?> readData(FriendlyByteBuf buffer) {
        ResourceLocation typeId = buffer.readResourceLocation();
        DataType<?, ?> type = ICustomMachineryAPI.INSTANCE.dataRegistrar().get(typeId);
        if (type == null) {
            throw new IllegalStateException("Attempting to read invalid IData : " + typeId + " is not a valid registered DataType !");
        } else {
            short id = buffer.readShort();
            return type.readData(id, buffer);
        }
    }
}