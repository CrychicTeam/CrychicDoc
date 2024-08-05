package com.simibubi.create.content.fluids.transfer;

import com.simibubi.create.content.fluids.FluidFX;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class FluidSplashPacket extends SimplePacketBase {

    private BlockPos pos;

    private FluidStack fluid;

    public FluidSplashPacket(BlockPos pos, FluidStack fluid) {
        this.pos = pos;
        this.fluid = fluid;
    }

    public FluidSplashPacket(FriendlyByteBuf buffer) {
        this.pos = buffer.readBlockPos();
        this.fluid = buffer.readFluidStack();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.pos);
        buffer.writeFluidStack(this.fluid);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            if (!(Minecraft.getInstance().player.m_20182_().distanceTo(new Vec3((double) this.pos.m_123341_(), (double) this.pos.m_123342_(), (double) this.pos.m_123343_())) > 100.0)) {
                FluidFX.splash(this.pos, this.fluid);
            }
        }));
        return true;
    }
}