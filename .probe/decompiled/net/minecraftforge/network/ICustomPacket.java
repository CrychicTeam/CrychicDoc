package net.minecraftforge.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public interface ICustomPacket<T extends Packet<?>> {

    @Nullable
    FriendlyByteBuf getInternalData();

    ResourceLocation getName();

    int getIndex();

    default NetworkDirection getDirection() {
        return NetworkDirection.directionFor(this.getClass());
    }

    default T getThis() {
        return (T) this;
    }
}