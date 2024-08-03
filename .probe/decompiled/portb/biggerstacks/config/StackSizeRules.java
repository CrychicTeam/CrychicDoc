package portb.biggerstacks.config;

import portb.configlib.xml.RuleSet;

public class StackSizeRules {

    public static int maxRegisteredItemStackSize = 64;

    private static RuleSet ruleSet;

    public static RuleSet getRuleSet() {
        return ruleSet;
    }

    public static void setRuleSet(RuleSet ruleSet) {
        StackSizeRules.ruleSet = ruleSet;
    }

    public static int getMaxStackSize() {
        return ruleSet != null ? Math.max(ruleSet.getMaxStacksize(), maxRegisteredItemStackSize) : maxRegisteredItemStackSize;
    }
}