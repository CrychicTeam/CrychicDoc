package se.mickelus.tetra.effect;

import java.util.Optional;
import net.minecraft.core.Vec3i;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import se.mickelus.tetra.items.modular.ItemModularHandheld;

public class ReachingEffect {

    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        if (event.getPosition().isPresent()) {
            Optional.of(event.getEntity().m_21205_()).filter(itemStack -> !itemStack.isEmpty()).filter(itemStack -> itemStack.getItem() instanceof ItemModularHandheld).ifPresent(itemStack -> {
                int level = EffectHelper.getEffectLevel(itemStack, ItemEffect.reaching);
                if (level > 0) {
                    double distance = event.getEntity().m_20182_().distanceToSqr(Vec3.atCenterOf((Vec3i) event.getPosition().get()));
                    if (distance > 1.0) {
                        event.setNewSpeed(event.getNewSpeed() * getMultiplier(level, distance, 1.0F));
                    }
                }
            });
        }
    }

    public static void onLivingDamage(LivingDamageEvent event, int level, float efficiency) {
        double distance = event.getSource().getEntity().distanceToSqr(event.getEntity());
        float multiplier = event.getSource().is(DamageTypeTags.IS_PROJECTILE) ? efficiency : 1.0F;
        if (distance > 1.0) {
            event.setAmount(event.getAmount() * getMultiplier(level, distance, multiplier));
        }
    }

    public static float getMultiplier(int level, double squareDistance, float offsetMultiplier) {
        return level > 0 && squareDistance > 0.0 ? 1.0F + getOffset(level, squareDistance) * offsetMultiplier : 1.0F;
    }

    public static float getOffset(int level, double squareDistance) {
        return level > 0 ? (float) ((double) ((float) level / 100.0F) * Math.log(squareDistance * squareDistance)) : 0.0F;
    }
}