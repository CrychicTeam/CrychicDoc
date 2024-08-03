package portb.biggerstacks.util;

import portb.biggerstacks.config.StackSizeRules;

public class ModularRoutersHelper {

    public static int getMaxStackUpgrades() {
        return (int) Math.max(1.0, Math.ceil(Math.log((double) StackSizeRules.getMaxStackSize()) / Math.log(2.0)));
    }
}