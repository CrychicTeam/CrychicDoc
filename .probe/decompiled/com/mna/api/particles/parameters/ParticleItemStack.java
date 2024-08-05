package com.mna.api.particles.parameters;

import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

public class ParticleItemStack {

    private final ItemStack value;

    public ParticleItemStack(ItemStack stack) {
        this.value = stack;
    }

    public ParticleItemStack() {
        this.value = ItemStack.EMPTY;
    }

    public ItemStack value() {
        return this.value;
    }

    public String serialize() {
        return this.value.serializeNBT().toString();
    }

    @Nullable
    public static ParticleItemStack deserialize(String string) {
        return string == null ? null : new ParticleItemStack(ItemStack.EMPTY);
    }

    @Nullable
    public static ParticleItemStack deserialize(FriendlyByteBuf packetBuffer) {
        return packetBuffer.readBoolean() ? new ParticleItemStack(ItemStack.of(packetBuffer.readNbt())) : null;
    }

    public static void serialize(@Nullable ParticleItemStack inst, FriendlyByteBuf packetBuffer) {
        if (inst != null && !inst.value.isEmpty()) {
            packetBuffer.writeBoolean(true);
            packetBuffer.writeNbt(inst.value().serializeNBT());
        } else {
            packetBuffer.writeBoolean(false);
        }
    }
}