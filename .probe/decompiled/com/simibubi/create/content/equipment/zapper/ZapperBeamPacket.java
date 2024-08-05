package com.simibubi.create.content.equipment.zapper;

import com.simibubi.create.CreateClient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ZapperBeamPacket extends ShootGadgetPacket {

    public Vec3 target;

    public ZapperBeamPacket(Vec3 start, Vec3 target, InteractionHand hand, boolean self) {
        super(start, hand, self);
        this.target = target;
    }

    public ZapperBeamPacket(FriendlyByteBuf buffer) {
        super(buffer);
    }

    @Override
    protected void readAdditional(FriendlyByteBuf buffer) {
        this.target = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
    }

    @Override
    protected void writeAdditional(FriendlyByteBuf buffer) {
        buffer.writeDouble(this.target.x);
        buffer.writeDouble(this.target.y);
        buffer.writeDouble(this.target.z);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    protected ShootableGadgetRenderHandler getHandler() {
        return CreateClient.ZAPPER_RENDER_HANDLER;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    protected void handleAdditional() {
        CreateClient.ZAPPER_RENDER_HANDLER.addBeam(new ZapperRenderHandler.LaserBeam(this.location, this.target));
    }
}