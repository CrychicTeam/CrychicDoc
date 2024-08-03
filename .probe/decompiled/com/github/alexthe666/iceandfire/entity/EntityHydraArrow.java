package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;

public class EntityHydraArrow extends AbstractArrow {

    public EntityHydraArrow(EntityType<? extends AbstractArrow> t, Level worldIn) {
        super(t, worldIn);
        this.m_36781_(5.0);
    }

    public EntityHydraArrow(EntityType<? extends AbstractArrow> t, Level worldIn, double x, double y, double z) {
        this(t, worldIn);
        this.m_6034_(x, y, z);
        this.m_36781_(5.0);
    }

    public EntityHydraArrow(PlayMessages.SpawnEntity spawnEntity, Level worldIn) {
        this(IafEntityRegistry.HYDRA_ARROW.get(), worldIn);
    }

    @NotNull
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public EntityHydraArrow(EntityType t, Level worldIn, LivingEntity shooter) {
        super(t, shooter, worldIn);
        this.m_36781_(5.0);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.m_9236_().isClientSide && !this.f_36703_) {
            double d0 = this.f_19796_.nextGaussian() * 0.02;
            double d1 = this.f_19796_.nextGaussian() * 0.02;
            double d2 = this.f_19796_.nextGaussian() * 0.02;
            double d3 = 10.0;
            double xRatio = this.m_20184_().x * (double) this.m_20206_();
            double zRatio = this.m_20184_().z * (double) this.m_20206_();
            IceAndFire.PROXY.spawnParticle(EnumParticles.Hydra, this.m_20185_() + xRatio + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 1.0F) - (double) this.m_20205_() - d0 * 10.0, this.m_20186_() + (double) (this.f_19796_.nextFloat() * this.m_20206_()) - d1 * 10.0, this.m_20189_() + zRatio + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 1.0F) - (double) this.m_20205_() - d2 * 10.0, 0.1, 1.0, 0.1);
            IceAndFire.PROXY.spawnParticle(EnumParticles.Hydra, this.m_20185_() + xRatio + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 1.0F) - (double) this.m_20205_() - d0 * 10.0, this.m_20186_() + (double) (this.f_19796_.nextFloat() * this.m_20206_()) - d1 * 10.0, this.m_20189_() + zRatio + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 1.0F) - (double) this.m_20205_() - d2 * 10.0, 0.1, 1.0, 0.1);
        }
    }

    protected void damageShield(Player player, float damage) {
        if (damage >= 3.0F && player.m_21211_().getItem().canPerformAction(player.m_21211_(), ToolActions.SHIELD_BLOCK)) {
            ItemStack copyBeforeUse = player.m_21211_().copy();
            int i = 1 + Mth.floor(damage);
            player.m_21211_().hurtAndBreak(i, player, p_213360_0_ -> p_213360_0_.m_21166_(EquipmentSlot.CHEST));
            if (player.m_21211_().isEmpty()) {
                InteractionHand Hand = player.m_7655_();
                ForgeEventFactory.onPlayerDestroyItem(player, copyBeforeUse, Hand);
                if (Hand == InteractionHand.MAIN_HAND) {
                    this.m_8061_(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                } else {
                    this.m_8061_(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
                }
                player.m_5810_();
                this.m_5496_(SoundEvents.SHIELD_BREAK, 0.8F, 0.8F + this.m_9236_().random.nextFloat() * 0.4F);
            }
        }
    }

    @Override
    protected void doPostHurtEffects(@NotNull LivingEntity living) {
        if (living instanceof Player) {
            this.damageShield((Player) living, (float) this.m_36789_());
        }
        living.addEffect(new MobEffectInstance(MobEffects.POISON, 300, 0));
        Entity shootingEntity = this.m_19749_();
        if (shootingEntity instanceof LivingEntity) {
            ((LivingEntity) shootingEntity).heal((float) this.m_36789_());
        }
    }

    @NotNull
    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(IafItemRegistry.HYDRA_ARROW.get());
    }
}