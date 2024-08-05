package com.rekindled.embers.api.upgrades;

import java.util.List;
import net.minecraft.core.Direction;

public interface IUpgradeProxy {

    void collectUpgrades(List<UpgradeContext> var1, int var2);

    boolean isSocket(Direction var1);

    boolean isProvider(Direction var1);
}