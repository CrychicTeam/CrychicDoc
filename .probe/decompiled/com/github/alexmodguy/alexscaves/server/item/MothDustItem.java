package com.github.alexmodguy.alexscaves.server.item;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class MothDustItem extends Item {

    public MothDustItem() {
        super(new Item.Properties());
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemstack = player.m_21120_(interactionHand);
        player.m_6672_(interactionHand);
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity living, int time) {
        if (living instanceof Player player) {
            int i = this.getUseDuration(itemStack) - time;
            float strength = getPowerForTime(i);
            float distance = strength * 5.0F;
            HitResult realHitResult = ProjectileUtil.getHitResultOnViewVector(living, Entity::m_271807_, (double) distance);
            Vec3 vec31 = realHitResult.getLocation();
            for (int j = 0; (double) j < Math.ceil((double) (distance * 3.0F)); j++) {
                Vec3 vec32 = vec31.add((double) (level.random.nextFloat() - 0.5F), (double) (level.random.nextFloat() - 0.5F), (double) (level.random.nextFloat() - 0.5F));
                Vec3 vec34 = player.m_146892_().add((double) (level.random.nextFloat() - 0.5F), (double) (level.random.nextFloat() - 0.5F), (double) (level.random.nextFloat() - 0.5F));
                Vec3 vec33 = vec32.subtract(player.m_146892_()).normalize().scale((double) (strength * 5.0F));
                level.addParticle(ACParticleRegistry.MOTH_DUST.get(), vec34.x, vec34.y, vec34.z, vec33.x, vec33.y, vec33.z);
            }
            if (realHitResult instanceof EntityHitResult hitEntityResult && hitEntityResult.getEntity() instanceof LivingEntity target && target.canBeSeenAsEnemy()) {
                AABB hitBox = new AABB(vec31.add(-32.0, -32.0, -32.0), vec31.add(32.0, 32.0, 32.0));
                for (Entity entity : level.getEntities(target, hitBox, Entity::m_271807_)) {
                    if (!target.m_7306_(entity) && !target.m_7307_(entity) && !entity.isAlliedTo(target) && !entity.isPassengerOfSameVehicle(target) && entity.getType().is(ACTagRegistry.MOTH_DUST_ENRAGES) && entity instanceof Mob mob) {
                        mob.setTarget(target);
                        mob.m_6703_(target);
                    }
                }
            }
            if (!player.isCreative()) {
                itemStack.shrink(1);
            }
        }
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    public static float getPowerForTime(int i) {
        float f = (float) i / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }
        return f;
    }
}