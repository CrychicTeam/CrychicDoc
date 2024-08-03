package com.simibubi.create.content.equipment.zapper;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

public abstract class ShootGadgetPacket extends SimplePacketBase {

    public Vec3 location;

    public InteractionHand hand;

    public boolean self;

    public ShootGadgetPacket(Vec3 location, InteractionHand hand, boolean self) {
        this.location = location;
        this.hand = hand;
        this.self = self;
    }

    public ShootGadgetPacket(FriendlyByteBuf buffer) {
        this.hand = buffer.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        this.self = buffer.readBoolean();
        this.location = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        this.readAdditional(buffer);
    }

    @Override
    public final void write(FriendlyByteBuf buffer) {
        buffer.writeBoolean(this.hand == InteractionHand.MAIN_HAND);
        buffer.writeBoolean(this.self);
        buffer.writeDouble(this.location.x);
        buffer.writeDouble(this.location.y);
        buffer.writeDouble(this.location.z);
        this.writeAdditional(buffer);
    }

    protected abstract void readAdditional(FriendlyByteBuf var1);

    protected abstract void writeAdditional(FriendlyByteBuf var1);

    @OnlyIn(Dist.CLIENT)
    protected abstract void handleAdditional();

    @OnlyIn(Dist.CLIENT)
    protected abstract ShootableGadgetRenderHandler getHandler();

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            Entity renderViewEntity = Minecraft.getInstance().getCameraEntity();
            if (renderViewEntity != null) {
                if (!(renderViewEntity.position().distanceTo(this.location) > 100.0)) {
                    ShootableGadgetRenderHandler handler = this.getHandler();
                    this.handleAdditional();
                    if (this.self) {
                        handler.shoot(this.hand, this.location);
                    } else {
                        handler.playSound(this.hand, this.location);
                    }
                }
            }
        });
        return true;
    }
}