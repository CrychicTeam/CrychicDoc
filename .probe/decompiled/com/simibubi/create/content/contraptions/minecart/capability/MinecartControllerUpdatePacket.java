package com.simibubi.create.content.contraptions.minecart.capability;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class MinecartControllerUpdatePacket extends SimplePacketBase {

    int entityID;

    CompoundTag nbt;

    public MinecartControllerUpdatePacket(MinecartController controller) {
        this.entityID = controller.cart().m_19879_();
        this.nbt = controller.serializeNBT();
    }

    public MinecartControllerUpdatePacket(FriendlyByteBuf buffer) {
        this.entityID = buffer.readInt();
        this.nbt = buffer.readNbt();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityID);
        buffer.writeNbt(this.nbt);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> this::handleCL));
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    private void handleCL() {
        ClientLevel world = Minecraft.getInstance().level;
        if (world != null) {
            Entity entityByID = world.getEntity(this.entityID);
            if (entityByID != null) {
                entityByID.getCapability(CapabilityMinecartController.MINECART_CONTROLLER_CAPABILITY).ifPresent(mc -> mc.deserializeNBT(this.nbt));
            }
        }
    }
}