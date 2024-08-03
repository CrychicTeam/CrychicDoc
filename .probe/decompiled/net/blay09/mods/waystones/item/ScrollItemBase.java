package net.blay09.mods.waystones.item;

import java.util.Random;
import net.blay09.mods.waystones.compat.Compat;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class ScrollItemBase extends Item {

    private static final Random random = new Random();

    public ScrollItemBase(Item.Properties properties) {
        super(properties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return this.m_8105_(itemStack) > 0 && !Compat.isVivecraftInstalled ? UseAnim.BOW : UseAnim.NONE;
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack itemStack, int remainingTicks) {
        if (level.isClientSide) {
            int duration = this.m_8105_(itemStack);
            float progress = (float) (duration - remainingTicks) / (float) duration;
            int maxParticles = Math.max(4, (int) (progress * 48.0F));
            if (remainingTicks % 5 == 0) {
                for (int i = 0; i < maxParticles; i++) {
                    level.addParticle(ParticleTypes.REVERSE_PORTAL, entity.m_20185_() + (random.nextDouble() - 0.5) * 1.5, entity.m_20186_() + random.nextDouble(), entity.m_20189_() + (random.nextDouble() - 0.5) * 1.5, 0.0, random.nextDouble(), 0.0);
                }
                if (progress >= 0.25F) {
                    for (int i = 0; i < maxParticles; i++) {
                        level.addParticle(ParticleTypes.CRIMSON_SPORE, entity.m_20185_() + (random.nextDouble() - 0.5) * 1.5, entity.m_20186_() + random.nextDouble(), entity.m_20189_() + (random.nextDouble() - 0.5) * 1.5, 0.0, random.nextDouble(), 0.0);
                    }
                }
                if (progress >= 0.5F) {
                    for (int i = 0; i < maxParticles / 3; i++) {
                        level.addParticle(ParticleTypes.WITCH, entity.m_20185_() + (random.nextDouble() - 0.5) * 1.5, entity.m_20186_() + 0.5 + random.nextDouble(), entity.m_20189_() + (random.nextDouble() - 0.5) * 1.5, 0.0, random.nextDouble(), 0.0);
                    }
                }
            }
            if (remainingTicks == 1) {
                for (int i = 0; i < maxParticles; i++) {
                    level.addParticle(ParticleTypes.PORTAL, entity.m_20185_() + (random.nextDouble() - 0.5) * 1.5, entity.m_20186_() + random.nextDouble() + 1.0, entity.m_20189_() + (random.nextDouble() - 0.5) * 1.5, (random.nextDouble() - 0.5) * 0.0, random.nextDouble(), (random.nextDouble() - 0.5) * 0.0);
                }
            }
        }
    }
}