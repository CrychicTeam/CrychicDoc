package se.mickelus.tetra.effect;

import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import se.mickelus.mutil.util.CastOptional;

@ParametersAreNonnullByDefault
public class EnderReverbEffect {

    public static void perform(LivingEntity entity, ItemStack itemStack, double multiplier) {
        if (!entity.m_9236_().isClientSide) {
            double effectProbability = (double) EffectHelper.getEffectEfficiency(itemStack, ItemEffect.enderReverb);
            if (effectProbability > 0.0 && !(Boolean) CastOptional.cast(entity, Player.class).map(Player::m_7500_).orElse(false) && entity.getRandom().nextDouble() < effectProbability * multiplier) {
                AABB aabb = new AABB(entity.m_20183_()).inflate(24.0);
                List<LivingEntity> nearbyTargets = entity.m_9236_().m_6443_(LivingEntity.class, aabb, target -> target instanceof EnderMan || target instanceof Endermite || target instanceof Shulker || target instanceof EnderDragon);
                if (nearbyTargets.size() > 0) {
                    ((LivingEntity) nearbyTargets.get(entity.getRandom().nextInt(nearbyTargets.size()))).setLastHurtByMob(entity);
                }
            }
        }
    }
}