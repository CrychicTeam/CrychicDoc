package dev.ftb.mods.ftbquests.client.gui;

import dev.ftb.mods.ftblibrary.util.client.ClientUtils;

@FunctionalInterface
public interface IRewardListenerScreen {

    void rewardReceived(RewardKey var1, int var2);

    static boolean add(RewardKey key, int count) {
        IRewardListenerScreen gui = ClientUtils.getCurrentGuiAs(IRewardListenerScreen.class);
        if (gui != null) {
            gui.rewardReceived(key, count);
            return true;
        } else {
            return false;
        }
    }
}