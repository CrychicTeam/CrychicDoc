package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.entity.EntityGorgon;
import com.github.alexthe666.iceandfire.entity.props.EntityDataProvider;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.IBlacklistedFromStatues;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class ItemCockatriceScepter extends Item {

    private final Random rand = new Random();

    private int specialWeaponDmg;

    public ItemCockatriceScepter() {
        super(new Item.Properties().durability(700));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        tooltip.add(Component.translatable("item.iceandfire.legendary_weapon.desc").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.iceandfire.cockatrice_scepter.desc_0").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.iceandfire.cockatrice_scepter.desc_1").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public void releaseUsing(@NotNull ItemStack stack, @NotNull Level worldIn, @NotNull LivingEntity livingEntity, int timeLeft) {
        if (this.specialWeaponDmg > 0) {
            stack.hurtAndBreak(this.specialWeaponDmg, livingEntity, player -> player.broadcastBreakEvent(livingEntity.getUsedItemHand()));
            this.specialWeaponDmg = 0;
        }
        EntityDataProvider.getCapability(livingEntity).ifPresent(data -> data.miscData.getTargetedByScepter().clear());
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack) {
        return 1;
    }

    @NotNull
    @Override
    public UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.BOW;
    }

    @NotNull
    @Override
    public InteractionResultHolder<ItemStack> use(@NotNull Level worldIn, Player playerIn, @NotNull InteractionHand hand) {
        ItemStack itemStackIn = playerIn.m_21120_(hand);
        playerIn.m_6672_(hand);
        return new InteractionResultHolder<>(InteractionResult.PASS, itemStackIn);
    }

    @Override
    public void onUseTick(Level level, LivingEntity player, ItemStack stack, int count) {
        if (player instanceof Player) {
            double dist = 32.0;
            Vec3 playerEyePosition = player.m_20299_(1.0F);
            Vec3 playerLook = player.m_20252_(1.0F);
            Vec3 Vector3d2 = playerEyePosition.add(playerLook.x * dist, playerLook.y * dist, playerLook.z * dist);
            Entity pointedEntity = null;
            List<Entity> nearbyEntities = level.getEntities(player, player.m_20191_().expandTowards(playerLook.x * dist, playerLook.y * dist, playerLook.z * dist).inflate(1.0, 1.0, 1.0), new Predicate<Entity>() {

                public boolean test(Entity entity) {
                    boolean blindness = entity instanceof LivingEntity && ((LivingEntity) entity).hasEffect(MobEffects.BLINDNESS) || entity instanceof IBlacklistedFromStatues && !((IBlacklistedFromStatues) entity).canBeTurnedToStone();
                    return entity != null && entity.isPickable() && !blindness && (entity instanceof Player || entity instanceof LivingEntity && DragonUtils.isAlive((LivingEntity) entity));
                }
            });
            double d2 = dist;
            for (Entity nearbyEntity : nearbyEntities) {
                AABB axisalignedbb = nearbyEntity.getBoundingBox().inflate((double) nearbyEntity.getPickRadius());
                Optional<Vec3> optional = axisalignedbb.clip(playerEyePosition, Vector3d2);
                if (axisalignedbb.contains(playerEyePosition)) {
                    if (d2 >= 0.0) {
                        pointedEntity = nearbyEntity;
                        d2 = 0.0;
                    }
                } else if (optional.isPresent()) {
                    double d3 = playerEyePosition.distanceTo((Vec3) optional.get());
                    if (d3 < d2 || d2 == 0.0) {
                        if (nearbyEntity.getRootVehicle() != player.m_20201_() || player.canRiderInteract()) {
                            pointedEntity = nearbyEntity;
                            d2 = d3;
                        } else if (d2 == 0.0) {
                            pointedEntity = nearbyEntity;
                        }
                    }
                }
            }
            if (pointedEntity instanceof LivingEntity target) {
                if (!target.isAlive()) {
                    return;
                }
                EntityDataProvider.getCapability(player).ifPresent(data -> data.miscData.addScepterTarget(target));
            }
            this.attackTargets(player);
        }
    }

    private void attackTargets(LivingEntity caster) {
        EntityDataProvider.getCapability(caster).ifPresent(data -> {
            for (LivingEntity target : new ArrayList(data.miscData.getTargetedByScepter())) {
                if (EntityGorgon.isEntityLookingAt(caster, target, 0.2F) && caster.isAlive() && target.isAlive()) {
                    target.addEffect(new MobEffectInstance(MobEffects.WITHER, 40, 2));
                    if (caster.f_19797_ % 20 == 0) {
                        this.specialWeaponDmg++;
                        target.hurt(caster.m_9236_().damageSources().wither(), 2.0F);
                    }
                    this.drawParticleBeam(caster, target);
                } else {
                    data.miscData.removeScepterTarget(target);
                }
            }
        });
    }

    private void drawParticleBeam(LivingEntity origin, LivingEntity target) {
        double d5 = 80.0;
        double d0 = target.m_20185_() - origin.m_20185_();
        double d1 = target.m_20186_() + (double) (target.m_20206_() * 0.5F) - (origin.m_20186_() + (double) origin.m_20192_() * 0.5);
        double d2 = target.m_20189_() - origin.m_20189_();
        double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
        d0 /= d3;
        d1 /= d3;
        d2 /= d3;
        double d4 = this.rand.nextDouble();
        while (d4 < d3) {
            origin.m_9236_().addParticle(ParticleTypes.ENTITY_EFFECT, origin.m_20185_() + d0 * ++d4, origin.m_20186_() + d1 * d4 + (double) origin.m_20192_() * 0.5, origin.m_20189_() + d2 * d4, 0.0, 0.0, 0.0);
        }
    }
}