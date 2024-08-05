package net.minecraft.server.level;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBossEventPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;

public class ServerBossEvent extends BossEvent {

    private final Set<ServerPlayer> players = Sets.newHashSet();

    private final Set<ServerPlayer> unmodifiablePlayers = Collections.unmodifiableSet(this.players);

    private boolean visible = true;

    public ServerBossEvent(Component component0, BossEvent.BossBarColor bossEventBossBarColor1, BossEvent.BossBarOverlay bossEventBossBarOverlay2) {
        super(Mth.createInsecureUUID(), component0, bossEventBossBarColor1, bossEventBossBarOverlay2);
    }

    @Override
    public void setProgress(float float0) {
        if (float0 != this.f_146638_) {
            super.setProgress(float0);
            this.broadcast(ClientboundBossEventPacket::m_178649_);
        }
    }

    @Override
    public void setColor(BossEvent.BossBarColor bossEventBossBarColor0) {
        if (bossEventBossBarColor0 != this.f_18842_) {
            super.setColor(bossEventBossBarColor0);
            this.broadcast(ClientboundBossEventPacket::m_178653_);
        }
    }

    @Override
    public void setOverlay(BossEvent.BossBarOverlay bossEventBossBarOverlay0) {
        if (bossEventBossBarOverlay0 != this.f_18843_) {
            super.setOverlay(bossEventBossBarOverlay0);
            this.broadcast(ClientboundBossEventPacket::m_178653_);
        }
    }

    @Override
    public BossEvent setDarkenScreen(boolean boolean0) {
        if (boolean0 != this.f_18844_) {
            super.setDarkenScreen(boolean0);
            this.broadcast(ClientboundBossEventPacket::m_178655_);
        }
        return this;
    }

    @Override
    public BossEvent setPlayBossMusic(boolean boolean0) {
        if (boolean0 != this.f_18845_) {
            super.setPlayBossMusic(boolean0);
            this.broadcast(ClientboundBossEventPacket::m_178655_);
        }
        return this;
    }

    @Override
    public BossEvent setCreateWorldFog(boolean boolean0) {
        if (boolean0 != this.f_18846_) {
            super.setCreateWorldFog(boolean0);
            this.broadcast(ClientboundBossEventPacket::m_178655_);
        }
        return this;
    }

    @Override
    public void setName(Component component0) {
        if (!Objects.equal(component0, this.f_18840_)) {
            super.setName(component0);
            this.broadcast(ClientboundBossEventPacket::m_178651_);
        }
    }

    private void broadcast(Function<BossEvent, ClientboundBossEventPacket> functionBossEventClientboundBossEventPacket0) {
        if (this.visible) {
            ClientboundBossEventPacket $$1 = (ClientboundBossEventPacket) functionBossEventClientboundBossEventPacket0.apply(this);
            for (ServerPlayer $$2 : this.players) {
                $$2.connection.send($$1);
            }
        }
    }

    public void addPlayer(ServerPlayer serverPlayer0) {
        if (this.players.add(serverPlayer0) && this.visible) {
            serverPlayer0.connection.send(ClientboundBossEventPacket.createAddPacket(this));
        }
    }

    public void removePlayer(ServerPlayer serverPlayer0) {
        if (this.players.remove(serverPlayer0) && this.visible) {
            serverPlayer0.connection.send(ClientboundBossEventPacket.createRemovePacket(this.m_18860_()));
        }
    }

    public void removeAllPlayers() {
        if (!this.players.isEmpty()) {
            for (ServerPlayer $$0 : Lists.newArrayList(this.players)) {
                this.removePlayer($$0);
            }
        }
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean boolean0) {
        if (boolean0 != this.visible) {
            this.visible = boolean0;
            for (ServerPlayer $$1 : this.players) {
                $$1.connection.send(boolean0 ? ClientboundBossEventPacket.createAddPacket(this) : ClientboundBossEventPacket.createRemovePacket(this.m_18860_()));
            }
        }
    }

    public Collection<ServerPlayer> getPlayers() {
        return this.unmodifiablePlayers;
    }
}