package net.blay09.mods.waystones.network.message;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class TeleportEffectMessage {

    private final BlockPos pos;

    public TeleportEffectMessage(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(TeleportEffectMessage message, FriendlyByteBuf buf) {
        buf.writeBlockPos(message.pos);
    }

    public static TeleportEffectMessage decode(FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        return new TeleportEffectMessage(pos);
    }

    public static void handle(Player player, TeleportEffectMessage message) {
        Level level = player.m_9236_();
        if (level != null) {
            for (int i = 0; i < 128; i++) {
                level.addParticle(ParticleTypes.PORTAL, (double) message.pos.m_123341_() + (level.random.nextDouble() - 0.5) * 3.0, (double) message.pos.m_123342_() + level.random.nextDouble() * 3.0, (double) message.pos.m_123343_() + (level.random.nextDouble() - 0.5) * 3.0, (level.random.nextDouble() - 0.5) * 2.0, -level.random.nextDouble(), (level.random.nextDouble() - 0.5) * 2.0);
            }
        }
    }
}