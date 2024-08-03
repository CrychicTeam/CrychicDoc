package vectorwing.farmersdelight.common.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class ComfortEffect extends MobEffect {

    public ComfortEffect() {
        super(MobEffectCategory.BENEFICIAL, 0);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.hasEffect(MobEffects.REGENERATION)) {
            if (entity instanceof Player player && (double) player.getFoodData().getSaturationLevel() > 0.0) {
                return;
            }
            if (entity.getHealth() < entity.getMaxHealth()) {
                entity.heal(1.0F);
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 80 == 0;
    }
}