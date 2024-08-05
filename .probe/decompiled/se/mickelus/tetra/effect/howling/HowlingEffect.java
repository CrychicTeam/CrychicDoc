package se.mickelus.tetra.effect.howling;

import java.util.Optional;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.TetraMod;
import se.mickelus.tetra.effect.EffectHelper;
import se.mickelus.tetra.effect.ItemEffect;

@ParametersAreNonnullByDefault
public class HowlingEffect {

    public static void sendPacket() {
        TetraMod.packetHandler.sendToServer(new HowlingPacket());
    }

    public static void trigger(ItemStack itemStack, LivingEntity player, int effectLevel) {
        int duration = Math.round(EffectHelper.getEffectEfficiency(itemStack, ItemEffect.howling) * 20.0F);
        int currentAmplifier = (Integer) Optional.ofNullable(player.getEffect(HowlingPotionEffect.instance)).map(MobEffectInstance::m_19564_).orElse(-1);
        player.addEffect(new MobEffectInstance(HowlingPotionEffect.instance, duration, Math.min(currentAmplifier + effectLevel, 11), false, false));
    }

    public static void deflectProjectile(ProjectileImpactEvent event, Projectile projectile, HitResult rayTraceResult) {
        Optional.ofNullable(rayTraceResult).filter(result -> result.getType() == HitResult.Type.ENTITY).map(result -> (EntityHitResult) result).map(EntityHitResult::m_82443_).flatMap(entity -> CastOptional.cast(entity, LivingEntity.class)).filter(entity -> willDeflect(entity.getEffect(HowlingPotionEffect.instance), entity.getRandom())).ifPresent(entity -> {
            Vec3 newDir;
            if ((double) entity.getEffect(HowlingPotionEffect.instance).getAmplifier() * 0.02 < entity.getRandom().nextDouble()) {
                Vec3 normal = entity.m_20182_().add(0.0, (double) (entity.m_20206_() / 2.0F), 0.0).subtract(projectile.m_20182_()).normalize();
                newDir = projectile.m_20184_().subtract(normal.scale(2.0 * projectile.m_20184_().dot(normal)));
            } else {
                newDir = projectile.m_20184_().scale(-0.8);
                CastOptional.cast(projectile.getOwner(), LivingEntity.class).ifPresent(shooter -> shooter.setLastHurtMob(entity));
                projectile.setOwner(entity);
            }
            projectile.shoot(newDir.x, newDir.y, newDir.z, (float) projectile.m_20184_().length(), 0.1F);
            event.setCanceled(true);
        });
    }

    private static boolean willDeflect(MobEffectInstance effectInstance, RandomSource random) {
        return effectInstance != null && random.nextDouble() < (double) effectInstance.getAmplifier() * 0.125;
    }
}