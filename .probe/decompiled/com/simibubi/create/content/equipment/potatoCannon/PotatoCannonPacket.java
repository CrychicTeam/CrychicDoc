package com.simibubi.create.content.equipment.potatoCannon;

import com.simibubi.create.CreateClient;
import com.simibubi.create.content.equipment.zapper.ShootGadgetPacket;
import com.simibubi.create.content.equipment.zapper.ShootableGadgetRenderHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PotatoCannonPacket extends ShootGadgetPacket {

    private float pitch;

    private Vec3 motion;

    private ItemStack item;

    public PotatoCannonPacket(Vec3 location, Vec3 motion, ItemStack item, InteractionHand hand, float pitch, boolean self) {
        super(location, hand, self);
        this.motion = motion;
        this.item = item;
        this.pitch = pitch;
    }

    public PotatoCannonPacket(FriendlyByteBuf buffer) {
        super(buffer);
    }

    @Override
    protected void readAdditional(FriendlyByteBuf buffer) {
        this.pitch = buffer.readFloat();
        this.motion = new Vec3((double) buffer.readFloat(), (double) buffer.readFloat(), (double) buffer.readFloat());
        this.item = buffer.readItem();
    }

    @Override
    protected void writeAdditional(FriendlyByteBuf buffer) {
        buffer.writeFloat(this.pitch);
        buffer.writeFloat((float) this.motion.x);
        buffer.writeFloat((float) this.motion.y);
        buffer.writeFloat((float) this.motion.z);
        buffer.writeItem(this.item);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    protected void handleAdditional() {
        CreateClient.POTATO_CANNON_RENDER_HANDLER.beforeShoot(this.pitch, this.location, this.motion, this.item);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    protected ShootableGadgetRenderHandler getHandler() {
        return CreateClient.POTATO_CANNON_RENDER_HANDLER;
    }
}