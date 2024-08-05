package dev.xkmc.l2library.init.explosion;

import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

public class ExplosionHandler {

    public static void explode(BaseExplosion exp) {
        if (!exp.base.level().isClientSide()) {
            if (!ForgeEventFactory.onExplosionStart(exp.base.level(), exp)) {
                exp.m_46061_();
                Level level = exp.base.level();
                exp.m_46075_(level.isClientSide());
                double x = exp.base.x();
                double y = exp.base.y();
                double z = exp.base.z();
                float r = exp.base.r();
                boolean flag = exp.mc.type() == Explosion.BlockInteraction.KEEP;
                if (flag) {
                    exp.m_46080_();
                }
                for (Player player : level.m_6907_()) {
                    if (player instanceof ServerPlayer) {
                        ServerPlayer serverplayer = (ServerPlayer) player;
                        if (serverplayer.m_20275_(x, y, z) < 4096.0) {
                            serverplayer.connection.send(new ClientboundExplodePacket(x, y, z, r, exp.m_46081_(), (Vec3) exp.m_46078_().get(serverplayer)));
                        }
                    }
                }
            }
        }
    }
}