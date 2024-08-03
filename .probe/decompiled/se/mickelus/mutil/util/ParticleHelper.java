package se.mickelus.mutil.util;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

@ParametersAreNonnullByDefault
public class ParticleHelper {

    public static void spawnArmorParticles(LivingEntity entity) {
        spawnArmorParticles(entity, EquipmentSlot.values()[2 + entity.getRandom().nextInt(4)]);
    }

    public static void spawnArmorParticles(LivingEntity entity, EquipmentSlot slot) {
        RandomSource rand = entity.getRandom();
        ItemStack itemStack = entity.getItemBySlot(slot);
        if (!itemStack.isEmpty()) {
            ((ServerLevel) entity.m_9236_()).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, itemStack), entity.m_20185_() + (double) entity.m_20205_() * (0.3 + rand.nextGaussian() * 0.4), entity.m_20186_() + (double) entity.m_20206_() * (0.2 + rand.nextGaussian() * 0.4), entity.m_20189_() + (double) entity.m_20205_() * (0.3 + rand.nextGaussian() * 0.4), 10, 0.0, 0.0, 0.0, 0.0);
        }
    }
}