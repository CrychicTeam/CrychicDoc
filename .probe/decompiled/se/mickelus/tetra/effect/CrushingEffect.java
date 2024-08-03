package se.mickelus.tetra.effect;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import se.mickelus.mutil.util.CastOptional;

@ParametersAreNonnullByDefault
public class CrushingEffect {

    public static void onLivingDamage(LivingDamageEvent event, int effectLevel) {
        if (effectLevel > 0 && event.getAmount() < (float) effectLevel) {
            float attackStrength = (Float) CastOptional.cast(event.getSource().getDirectEntity(), Player.class).map(EffectHelper::getCooledAttackStrength).orElse(1.0F);
            if ((double) attackStrength > 0.9) {
                event.setAmount((float) effectLevel);
            }
        }
    }
}