package org.embeddedt.modernfix.forge.mixin.bugfix.forge_vehicle_packets;

import net.minecraft.network.protocol.game.ServerboundMoveVehiclePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ ServerGamePacketListenerImpl.class })
public class ServerGamePacketListenerImplMixin {

    @Shadow
    public ServerPlayer player;

    @Redirect(method = { "handleMoveVehicle" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;absMoveTo(DDDFF)V"), require = 0)
    private void movePlayerUsingPositionRider(ServerPlayer player, double x, double y, double z, float yRot, float xRot, ServerboundMoveVehiclePacket packet) {
        if (player == this.player) {
            Vec3 oldPos = this.player.m_20182_();
            yRot = this.player.m_146908_();
            xRot = this.player.m_146909_();
            float yHeadRot = this.player.m_6080_();
            this.player.m_20201_().positionRider(this.player);
            this.player.m_146922_(yRot);
            this.player.m_146926_(xRot);
            this.player.m_5616_(yHeadRot);
            this.player.f_19854_ = oldPos.x;
            this.player.f_19855_ = oldPos.y;
            this.player.f_19856_ = oldPos.z;
        } else {
            player.m_19890_(x, y, z, yRot, xRot);
        }
    }
}