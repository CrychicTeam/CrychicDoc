package com.almostreliable.morejs.features.villager;

import com.almostreliable.morejs.MoreJS;

public class ForgeTradingManager extends TradingManager {

    public static final ForgeTradingManager INSTANCE = new ForgeTradingManager();

    private boolean readyToReload = false;

    public void reset() {
        this.tradesBackup = null;
        this.wandererTradesBackup = null;
        this.readyToReload = false;
    }

    public void start() {
        this.reset();
        this.readyToReload = true;
        this.reload();
    }

    @Override
    public void reload() {
        if (this.readyToReload) {
            super.reload();
        } else {
            MoreJS.LOG.debug("Villager trades are not ready to reload yet. Waiting for the server to start.");
        }
    }
}