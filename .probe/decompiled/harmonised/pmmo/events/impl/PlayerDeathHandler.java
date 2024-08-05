package harmonised.pmmo.events.impl;

import harmonised.pmmo.config.Config;
import harmonised.pmmo.core.Core;
import java.util.HashMap;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class PlayerDeathHandler {

    public static void handle(LivingDeathEvent event) {
        if (Config.LOSS_ON_DEATH.get() != 0.0) {
            Player player = (Player) event.getEntity();
            Core core = Core.get(player.m_9236_());
            new HashMap(core.getData().getXpMap(player.m_20148_())).forEach((skill, xp) -> {
                int currentLevel = core.getData().getLevelFromXP(xp);
                long levelXpThreshold = core.getData().getBaseXpForLevel(currentLevel);
                long safeXP = Config.LOSE_LEVELS_ON_DEATH.get() ? 0L : levelXpThreshold;
                long xpToCalculateLoss = Config.LOSE_ONLY_EXCESS.get() ? xp - safeXP : xp;
                long rawLoss = Double.valueOf((double) xpToCalculateLoss * Config.LOSS_ON_DEATH.get()).longValue();
                long finalXp = Math.max(safeXP, xp - rawLoss);
                core.getData().setXpRaw(player.m_20148_(), skill, finalXp);
            });
        }
    }
}