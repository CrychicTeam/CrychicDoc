package vectorwing.farmersdelight.common.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.GameRules;

public class NourishmentEffect extends MobEffect {

    public NourishmentEffect() {
        super(MobEffectCategory.BENEFICIAL, 0);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.m_20193_().isClientSide && entity instanceof Player player) {
            FoodData foodData = player.getFoodData();
            boolean isPlayerHealingWithHunger = player.m_9236_().getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION) && player.isHurt() && foodData.getFoodLevel() >= 18;
            if (!isPlayerHealingWithHunger) {
                float exhaustion = foodData.getExhaustionLevel();
                float reduction = Math.min(exhaustion, 4.0F);
                if (exhaustion > 0.0F) {
                    player.causeFoodExhaustion(-reduction);
                }
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}