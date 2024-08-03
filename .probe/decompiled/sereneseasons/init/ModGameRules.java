package sereneseasons.init;

import net.minecraft.world.level.GameRules;
import sereneseasons.api.SSGameRules;

public class ModGameRules {

    public static void init() {
        SSGameRules.RULE_DOSEASONCYCLE = GameRules.register("doSeasonCycle", GameRules.Category.UPDATES, GameRules.BooleanValue.create(true));
    }
}