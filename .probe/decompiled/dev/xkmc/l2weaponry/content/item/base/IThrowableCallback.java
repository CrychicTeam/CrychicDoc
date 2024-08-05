package dev.xkmc.l2weaponry.content.item.base;

import dev.xkmc.l2weaponry.content.entity.BaseThrownWeaponEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.Vec3;

public interface IThrowableCallback {

    default void onHitBlock(BaseThrownWeaponEntity<?> entity, ItemStack item) {
        if (this.causeThunder(entity)) {
            thunderHit(entity);
        }
    }

    default void onHitEntity(BaseThrownWeaponEntity<?> entity, ItemStack item, LivingEntity le) {
        if (this.causeThunder(entity)) {
            thunderHit(entity);
        }
    }

    default boolean causeThunder(BaseThrownWeaponEntity<?> entity) {
        return entity.m_9236_().isThundering() && entity.m_9236_().m_45527_(entity.m_20183_()) && entity.getItem().getEnchantmentLevel(Enchantments.CHANNELING) > 0;
    }

    static void thunderHit(BaseThrownWeaponEntity<?> entity) {
        if (!entity.m_9236_().isClientSide) {
            BlockPos blockpos = entity.m_20183_();
            LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(entity.m_9236_());
            ???;
        }
    }

    static {
        ???;
    }
}