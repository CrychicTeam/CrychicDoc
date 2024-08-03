package net.minecraft.network;

import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ClientboundDisconnectPacket;
import org.slf4j.Logger;

public class RateKickingConnection extends Connection {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final Component EXCEED_REASON = Component.translatable("disconnect.exceeded_packet_rate");

    private final int rateLimitPacketsPerSecond;

    public RateKickingConnection(int int0) {
        super(PacketFlow.SERVERBOUND);
        this.rateLimitPacketsPerSecond = int0;
    }

    @Override
    protected void tickSecond() {
        super.tickSecond();
        float $$0 = this.m_129542_();
        if ($$0 > (float) this.rateLimitPacketsPerSecond) {
            LOGGER.warn("Player exceeded rate-limit (sent {} packets per second)", $$0);
            this.m_243124_(new ClientboundDisconnectPacket(EXCEED_REASON), PacketSendListener.thenRun(() -> this.m_129507_(EXCEED_REASON)));
            this.m_129540_();
        }
    }
}