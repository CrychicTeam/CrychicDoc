package com.github.alexmodguy.alexscaves.server.entity.util;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.message.UpdateBossBarMessage;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;

public class ACBossEvent extends ServerBossEvent {

    private final int renderType;

    public ACBossEvent(Component component, int renderType) {
        super(component, BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);
        this.renderType = renderType;
    }

    public int getRenderType() {
        return this.renderType;
    }

    @Override
    public void addPlayer(ServerPlayer serverPlayer) {
        AlexsCaves.sendNonLocal(new UpdateBossBarMessage(this.m_18860_(), this.renderType), serverPlayer);
        super.addPlayer(serverPlayer);
    }

    @Override
    public void removePlayer(ServerPlayer serverPlayer) {
        AlexsCaves.sendNonLocal(new UpdateBossBarMessage(this.m_18860_(), -1), serverPlayer);
        super.removePlayer(serverPlayer);
    }
}