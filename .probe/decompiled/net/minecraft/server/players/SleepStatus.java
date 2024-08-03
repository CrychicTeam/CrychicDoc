package net.minecraft.server.players;

import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public class SleepStatus {

    private int activePlayers;

    private int sleepingPlayers;

    public boolean areEnoughSleeping(int int0) {
        return this.sleepingPlayers >= this.sleepersNeeded(int0);
    }

    public boolean areEnoughDeepSleeping(int int0, List<ServerPlayer> listServerPlayer1) {
        int $$2 = (int) listServerPlayer1.stream().filter(Player::m_36317_).count();
        return $$2 >= this.sleepersNeeded(int0);
    }

    public int sleepersNeeded(int int0) {
        return Math.max(1, Mth.ceil((float) (this.activePlayers * int0) / 100.0F));
    }

    public void removeAllSleepers() {
        this.sleepingPlayers = 0;
    }

    public int amountSleeping() {
        return this.sleepingPlayers;
    }

    public boolean update(List<ServerPlayer> listServerPlayer0) {
        int $$1 = this.activePlayers;
        int $$2 = this.sleepingPlayers;
        this.activePlayers = 0;
        this.sleepingPlayers = 0;
        for (ServerPlayer $$3 : listServerPlayer0) {
            if (!$$3.isSpectator()) {
                this.activePlayers++;
                if ($$3.m_5803_()) {
                    this.sleepingPlayers++;
                }
            }
        }
        return ($$2 > 0 || this.sleepingPlayers > 0) && ($$1 != this.activePlayers || $$2 != this.sleepingPlayers);
    }
}