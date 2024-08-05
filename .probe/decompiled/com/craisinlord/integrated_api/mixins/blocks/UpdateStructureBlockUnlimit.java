package com.craisinlord.integrated_api.mixins.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ServerboundSetStructureBlockPacket;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = { ServerboundSetStructureBlockPacket.class }, priority = 999)
public class UpdateStructureBlockUnlimit {

    @Shadow
    @Final
    @Mutable
    private BlockPos offset;

    @Shadow
    @Final
    @Mutable
    private Vec3i size;

    @Inject(method = { "<init>(Lnet/minecraft/network/FriendlyByteBuf;)V" }, at = { @At("RETURN") }, require = 0)
    public void readInts(FriendlyByteBuf buf, CallbackInfo ci) {
        int newStructureSize = 512;
        this.offset = new BlockPos(Mth.clamp(buf.readInt(), -512, 512), Mth.clamp(buf.readInt(), -512, 512), Mth.clamp(buf.readInt(), -512, 512));
        this.size = new BlockPos(Mth.clamp(buf.readInt(), 0, 512), Mth.clamp(buf.readInt(), 0, 512), Mth.clamp(buf.readInt(), 0, 512));
    }

    @Inject(method = { "write" }, at = { @At("RETURN") }, require = 0)
    public void writeInts(FriendlyByteBuf buf, CallbackInfo ci) {
        buf.writeInt(this.offset.m_123341_());
        buf.writeInt(this.offset.m_123342_());
        buf.writeInt(this.offset.m_123343_());
        buf.writeInt(this.size.getX());
        buf.writeInt(this.size.getY());
        buf.writeInt(this.size.getZ());
    }
}