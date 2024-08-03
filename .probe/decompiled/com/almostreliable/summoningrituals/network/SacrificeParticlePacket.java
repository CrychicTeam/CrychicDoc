package com.almostreliable.summoningrituals.network;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;

public class SacrificeParticlePacket extends ServerToClientPacket<SacrificeParticlePacket> {

    private List<BlockPos> positions;

    public SacrificeParticlePacket(List<BlockPos> positions) {
        this.positions = positions;
    }

    SacrificeParticlePacket() {
    }

    public void encode(SacrificeParticlePacket packet, FriendlyByteBuf buffer) {
        buffer.writeVarInt(packet.positions.size());
        packet.positions.forEach(buffer::m_130064_);
    }

    public SacrificeParticlePacket decode(FriendlyByteBuf buffer) {
        return new SacrificeParticlePacket(IntStream.range(0, buffer.readVarInt()).mapToObj(i -> buffer.readBlockPos()).toList());
    }

    protected void handlePacket(SacrificeParticlePacket packet, ClientLevel level) {
        Random random = new Random();
        for (BlockPos pos : packet.positions) {
            for (int i = 0; i < 10; i++) {
                level.addParticle(ParticleTypes.SOUL, (double) ((float) pos.m_123341_() + random.nextFloat()), (double) ((float) pos.m_123342_() + random.nextFloat()), (double) ((float) pos.m_123343_() + random.nextFloat()), 0.0, 0.05F, 0.0);
            }
        }
    }
}